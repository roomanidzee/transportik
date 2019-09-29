package com.romanidze.transportik.modules.main

import cats.data.Kleisli
import cats.implicits._
import cats.effect.{Async, Blocker, Concurrent, ContextShift}
import com.romanidze.transportik.config.ApplicationConfig
import com.romanidze.transportik.modules.user.UserModule
import com.zaxxer.hikari.HikariDataSource
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux
import org.http4s.server.Router
import org.http4s.syntax.kleisli._
import org.http4s.{Headers, Request, Response}
import org.http4s.server.middleware.{CORS, Logger}

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

class ApplicationModule[F[_]: Concurrent: Async: ContextShift](config: ApplicationConfig) {

  Class.forName(config.jdbc.driver)

  val datasource = new HikariDataSource

  datasource.setJdbcUrl(config.jdbc.url)
  datasource.setUsername(config.jdbc.user)
  datasource.setPassword(config.jdbc.password)
  datasource.setMaximumPoolSize(config.jdbc.poolSize)
  datasource.setConnectionTimeout(config.jdbc.connectionTimeout)

  val transactor: Aux[F, HikariDataSource] = Transactor.fromDataSource[F](
    datasource,
    ExecutionContext.fromExecutor(Executors.newFixedThreadPool(config.jdbc.threadNumber)),
    Blocker.liftExecutorService(Executors.newCachedThreadPool())
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
