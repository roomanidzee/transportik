package com.romanidze.transportik.config

case class LiquibaseConfig(
  changelog: String,
  logTable: String,
  lockTable: String
)
