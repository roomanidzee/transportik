package com.romanidze.transportik.modules.main

import cats.data.Kleisli
import cats.implicits._
import cats.effect.{ Async, Blocker, ContextShift, Resource }
import com.romanidze.transportik.config.ApplicationConfig
import com.romanidze.transportik.modules.test.TestModule
import com.romanidze.transportik.modules.user.UserModule
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux
import org.http4s.server.Router
import org.http4s.syntax.kleisli._
import org.http4s.{ Request, Response }

class ApplicationModule[F[_]: Async: ContextShift](config: ApplicationConfig) {

  //TODO: сделать асинхронным
  val transactor: Aux[F, Unit] = Transactor.fromDriverManager[F](
    config.jdbc.driver,
    config.jdbc.url,
    config.jdbc.user,
    config.jdbc.password,
    Blocker.liftExecutionContext(ExecutionContexts.synchronous)
  )

  val testModule = new TestModule[F]
  val userModule = new UserModule[F](transactor)

  val router: Kleisli[F, Request[F], Response[F]] =
    Router[F](config.server.prefix -> (testModule.routes <+> userModule.routes)).orNotFound

}
