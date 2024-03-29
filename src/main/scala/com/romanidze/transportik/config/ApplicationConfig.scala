package com.romanidze.transportik.config

case class ApplicationConfig(
  server: ServerConfig,
  jdbc: JdbcConfig,
  liquibase: LiquibaseConfig,
  cassandra: CassandraConfig
)
