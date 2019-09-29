package com.romanidze.transportik.modules.main

import cats.data.Kleisli
import cats.implicits._
import cats.effect.{ Async, Concurrent, ContextShift }
import com.romanidze.transportik.config.ApplicationConfig
import com.romanidze.transportik.modules.user.UserModule
import org.http4s.server.Router
import org.http4s.syntax.kleisli._
import org.http4s.{ Headers, Request, Response }
import org.http4s.server.middleware.{ CORS, Logger }
import com.romanidze.transportik.modules.profile.ProfileModule
import com.romanidze.transportik.modules.transport.TransportModule
import com.romanidze.transportik.modules.trip.TripModule

class ApplicationModule[F[_]: Concurrent: Async: ContextShift](config: ApplicationConfig) {

  private val initializer = new ApplicationInitializer[F]

  private val transactor = initializer.prepareTransactor(config.jdbc)

  private val userModule      = new UserModule[F](transactor)
  private val profileModule   = new ProfileModule[F](transactor)
  private val tripModule      = new TripModule[F](transactor)
  private val transportModule = new TransportModule[F](transactor)

  private val routes = userModule.routes <+> profileModule.routes <+> tripModule.routes <+> transportModule.routes

  private val router: Kleisli[F, Request[F], Response[F]] =
    Router[F](
      config.server.prefix -> routes
    ).orNotFound

  private val loggedRouter = Logger.httpApp[F](
    logHeaders = true,
    logBody = true,
    Headers.SensitiveHeaders.contains
  )(router)

  val httpApp = CORS(loggedRouter)

}
