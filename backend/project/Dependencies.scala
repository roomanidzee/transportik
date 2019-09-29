import sbt._

object Dependencies {

  private val versions: Map[String, String] = Map(

    "http4s" -> "0.20.10",

    "circe" -> "0.12.1",

    "pureconfig" -> "0.12.0",
    "scala-test" -> "3.0.8",
    "scala-mock" -> "4.4.0",

    "cats" -> "2.0.0",

    "macwire" -> "2.3.3",

    "doobie" -> "0.8.2",
    "postgresql" -> "42.2.6",
    "postgis-jdbc" -> "2.3.0",
    "liquibase" -> "3.8.0",

    "scala-logging" -> "3.9.2",
    "logback" -> "1.2.3",

    "cassandra" -> "1.1.4"

  )

  //http4s
  private val http4s: Seq[ModuleID] = Seq(
    "org.http4s" %% "http4s-blaze-server" % versions("http4s"),
    "org.http4s" %% "http4s-blaze-client" % versions("http4s"),
    "org.http4s" %% "http4s-circe" % versions("http4s"),
    "org.http4s" %% "http4s-dsl" % versions("http4s")
  )

  //circe
  private val circe: Seq[ModuleID] = Seq(
    "io.circe" %% "circe-generic" % versions("circe"),
    "io.circe" %% "circe-literal" % versions("circe"),
    "io.circe" %% "circe-parser" % versions("circe")
  )

  //web
  private val web: Seq[ModuleID] = http4s.union(circe)

  //pureconfig
  private val pureConfig: Seq[ModuleID] = Seq(
    "com.github.pureconfig" %% "pureconfig"             % versions("pureconfig"),
    "com.github.pureconfig" %% "pureconfig-cats-effect" % versions("pureconfig")
  )

  //logging
  private val logging: Seq[ModuleID] = Seq(
    "com.typesafe.scala-logging" %% "scala-logging" % versions("scala-logging"),
    "ch.qos.logback" % "logback-classic" % versions("logback")
  )

  //cats
  private val cats: Seq[ModuleID] = Seq(
    "org.typelevel" %% "cats-core" % versions("cats")
  )

  //doobie
  private val doobie: Seq[ModuleID] = Seq(
    "org.tpolecat" %% "doobie-core" % versions("doobie"),
    "org.tpolecat" %% "doobie-postgres" % versions("doobie"),
    "org.tpolecat" %% "doobie-hikari" % versions("doobie")
  )

  //jdbc
  private val jdbc: Seq[ModuleID] = Seq(

    "org.postgresql" % "postgresql" % versions("postgresql"),
    "org.liquibase" % "liquibase-core" % versions("liquibase"),
    "net.postgis" % "postgis-jdbc" % versions("postgis-jdbc")

  ).union(doobie)

  //cassandra
  private val cassandra: Seq[ModuleID] = Seq(
    "com.evolutiongaming" %% "scassandra" % versions("cassandra")
  )

  //tests
  private val scalaTest: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % versions("scala-test"),
    "org.scalactic" %% "scalactic" % versions("scala-test")
  ).map(_ % Test)

  private val scalaMock: Seq[ModuleID] = Seq(
    "org.scalamock" %% "scalamock" % versions("scala-mock")
  ).map(_ % Test)

  private val doobieTest: Seq[ModuleID] = Seq(
    "org.tpolecat" %% "doobie-scalatest" % versions("doobie")
  ).map(_ % Test)

  val mainDeps: Seq[ModuleID] =
    web.union(pureConfig)
       .union(logging)
       .union(cats)
       .union(jdbc)
       .union(cassandra)

  val testDeps: Seq[ModuleID] =
    scalaTest.union(scalaMock)
             .union(doobieTest)

}
