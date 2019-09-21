package com.romanidze.transportik.application

import cats.Applicative
import cats.effect._
import cats.implicits._
import fs2.Stream
import org.http4s.server.blaze.BlazeServerBuilder
import com.romanidze.transportik.config.{ ApplicationConfig, ConfigurationLoader }
import com.romanidze.transportik.modules.test.TestModule
import org.http4s.server.middleware.Logger
import org.http4s.syntax.kleisli._

object Server {

  val appConfig: ApplicationConfig = ConfigurationLoader.load
    .fold(e => sys.error(s"Failed to load configuration:\n${e.toList.mkString("\n")}"), identity)

  def launch[F[_]: ConcurrentEffect: Applicative: ContextShift: Timer]: Stream[F, ExitCode] =
    for {
      mod          <- Stream.eval(new TestModule(appConfig.server).pure[F])
      apiV1App     = mod.routes.orNotFound
      finalHttpApp = Logger(logHeaders = true, logBody = true, null)(apiV1App)

      exitCode <- BlazeServerBuilder[F]
                   .bindHttp(appConfig.server.port, appConfig.server.host)
                   .withHttpApp(finalHttpApp)
                   .serve
    } yield exitCode

}
