package com.romanidze.transportik.modules.main

import java.util.concurrent.Executors

import cats.effect.{ Async, Blocker, Concurrent, ContextShift, Resource }
import com.datastax.driver.core.ProtocolOptions.Compression
import com.evolutiongaming.nel.Nel
import com.evolutiongaming.scassandra.util.FromGFuture
import com.evolutiongaming.scassandra.{ CassandraCluster, CassandraConfig, CassandraSession, PoolingConfig, QueryConfig, ReconnectionConfig, SocketConfig }
import com.romanidze.transportik.config.{ JdbcConfig, CassandraConfig => CassandraAppConfig }
import com.zaxxer.hikari.HikariDataSource
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux

import scala.concurrent.ExecutionContext

class ApplicationInitializer[F[_]: Concurrent: Async: ContextShift: FromGFuture] {

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

  def prepareCassandraConfig(config: CassandraAppConfig): Resource[F, CassandraSession[F]] = {

    val clusterConfig = CassandraConfig(
      name = "transportik-config",
      port = config.port,
      contactPoints = Nel(config.host),
      protocolVersion = None,
      pooling = PoolingConfig.Default,
      query = QueryConfig.Default,
      reconnection = ReconnectionConfig.Default,
      socket = SocketConfig.Default,
      authentication = None,
      loadBalancing = None,
      speculativeExecution = None,
      compression = Compression.NONE,
      logQueries = false
    )

    for {
      cluster <- CassandraCluster.of[F](clusterConfig, clusterId = 1)
      session <- cluster.connect
    } yield session

  }

}
