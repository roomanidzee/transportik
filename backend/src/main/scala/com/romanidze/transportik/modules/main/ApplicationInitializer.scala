package com.romanidze.transportik.modules.main

import java.util.concurrent.Executors

import cats.effect.{ Async, Blocker, Concurrent, ContextShift }
import com.romanidze.transportik.config.JdbcConfig
import com.zaxxer.hikari.HikariDataSource
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux

import scala.concurrent.ExecutionContext

class ApplicationInitializer[F[_]: Concurrent: Async: ContextShift] {

  def prepareTransactor(config: JdbcConfig): Aux[F, HikariDataSource] = {

    Class.forName(config.driver)

    val datasource = new HikariDataSource

    datasource.setJdbcUrl(config.url)
    datasource.setUsername(config.user)
    datasource.setPassword(config.password)
    datasource.setMaximumPoolSize(config.poolSize)
    datasource.setConnectionTimeout(config.connectionTimeout)

    Transactor.fromDataSource[F](
      datasource,
      ExecutionContext.fromExecutor(Executors.newFixedThreadPool(config.threadNumber)),
      Blocker.liftExecutorService(Executors.newCachedThreadPool())
    )

  }

}
