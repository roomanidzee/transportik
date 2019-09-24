package com.romanidze.transportik.components

import java.sql.{ Connection, DriverManager }

import cats.effect.{ Async, ContextShift }
import com.romanidze.transportik.config.{ JdbcConfig, LiquibaseConfig }
import com.typesafe.scalalogging.StrictLogging
import liquibase.Liquibase
import liquibase.database.{ Database, DatabaseFactory }
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor

class DBMigrations[F[_]: Async: ContextShift](db: JdbcConfig, liquibase: LiquibaseConfig) extends StrictLogging {

  def prepareDB(connection: Connection): Liquibase = {

    val database: Database = DatabaseFactory.getInstance.findCorrectDatabaseImplementation(
      new JdbcConnection(connection)
    )

    database.setDatabaseChangeLogTableName(liquibase.logTable)
    database.setDatabaseChangeLogLockTableName(liquibase.lockTable)

    val classLoader      = classOf[DBMigrations[F]].getClassLoader
    val resourceAccessor = new ClassLoaderResourceAccessor(classLoader)

    new Liquibase(liquibase.changelog, resourceAccessor, database)

  }

  def run(): Unit = {

    var connection: Connection  = null
    var dbConnection: Liquibase = null

    try {

      Class.forName(db.driver)

      connection = DriverManager.getConnection(
        db.url,
        db.user,
        db.password
      )

      dbConnection = prepareDB(connection)

      dbConnection.update("main")

    } catch {
      case e: Throwable =>
        logger.error(s"Exception when running DB migrations: $e")
        throw e
    } finally {
      dbConnection.forceReleaseLocks()
      connection.close()
    }

  }

}
