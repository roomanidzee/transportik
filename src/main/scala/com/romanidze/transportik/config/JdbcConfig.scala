package com.romanidze.transportik.config

case class JdbcConfig(
  url: String,
  driver: String,
  user: String,
  password: String,
  poolSize: Int,
  connectionTimeout: Int,
  threadNumber: Int
)
