package com.romanidze.transportik.application

import cats.Applicative
import cats.effect._
import com.romanidze.transportik.components.db.DBMigrations
import fs2.Stream
import org.http4s.server.blaze.BlazeServerBuilder
import com.romanidze.transportik.config.{ ApplicationConfig, ConfigurationLoader }
import com.romanidze.transportik.modules.main.ApplicationModule

object Server {

  val appConfig: ApplicationConfig = ConfigurationLoader.load
    .fold(
      e => sys.error(s"Failed to load configuration:\n${e.toList.mkString("\n")}"),
      identity
    )

  def launch[F[_]: ConcurrentEffect: Applicative: ContextShift: Timer]: Stream[F, ExitCode] = {

    val module     = new ApplicationModule[F](appConfig)
    val migrations = new DBMigrations[F](appConfig.jdbc, appConfig.liquibase)
    migrations.run()

    for {
      exitCode <- BlazeServerBuilder[F]
                   .bindHttp(appConfig.server.port, appConfig.server.host)
                   .withHttpApp(module.httpApp)
                   .serve
    } yield exitCode
  }

}
