package com.romanidze.transportik.modules.main

import cats.data.Kleisli
import cats.implicits._
import cats.effect.{ Async, Blocker, Concurrent, ContextShift }
import com.romanidze.transportik.config.ApplicationConfig
import com.romanidze.transportik.modules.user.UserModule
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux
import org.http4s.server.Router
import org.http4s.syntax.kleisli._
import org.http4s.{ Headers, Request, Response }
import org.http4s.server.middleware.{ CORS, Logger }

class ApplicationModule[F[_]: Concurrent: Async: ContextShift](config: ApplicationConfig) {

  //TODO: сделать асинхронным
  val transactor: Aux[F, Unit] = Transactor.fromDriverManager[F](
    config.jdbc.driver,
    config.jdbc.url,
    config.jdbc.user,
    config.jdbc.password,
    Blocker.liftExecutionContext(ExecutionContexts.synchronous)
  )
  val userModule = new UserModule[F](transactor)

  private val router: Kleisli[F, Request[F], Response[F]] =
    Router[F](
      config.server.prefix -> userModule.routes
    ).orNotFound

  private val loggedRouter = Logger.httpApp[F](
    logHeaders = true,
    logBody = true,
    Headers.SensitiveHeaders.contains
  )(router)

  val httpApp = CORS(loggedRouter)

}
