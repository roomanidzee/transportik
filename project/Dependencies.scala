import sbt._

object Dependencies {

  private val versions: Map[String, String] = Map(

    "http4s" -> "0.20.10",
    "circe" -> "0.12.1",

    "pureconfig" -> "0.12.0",
    "scala_test" -> "3.0.8",
    "scala_mock" -> "4.4.0",

    "cats" -> "2.0.0",

    "macwire" -> "2.3.3",

    "doobie" -> "0.8.2",
    "postgresql" -> "42.2.6",
    "liquibase" -> "3.8.0",

    "scala-logging" -> "3.9.2",
    "logback" -> "1.2.3"

  )

  //web
  private val web: Seq[ModuleID] = Seq(

    "org.http4s" %% "http4s-blaze-server" % versions("http4s"),
    "org.http4s" %% "http4s-blaze-client" % versions("http4s"),
    "org.http4s" %% "http4s-circe" % versions("http4s"),
    "org.http4s" %% "http4s-dsl" % versions("http4s"),

    "io.circe" %% "circe-generic" % versions("circe"),
    "io.circe" %% "circe-literal" % versions("circe"),
    "io.circe" %% "circe-parser" % versions("circe")
  )

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

  //db
  private val db: Seq[ModuleID] = Seq(

    "org.tpolecat" %% "doobie-core" % versions("doobie"),
    "org.tpolecat" %% "doobie-postgres" % versions("doobie"),
    "org.tpolecat" %% "doobie-hikari" % versions("doobie"),

    "org.postgresql" % "postgresql" % versions("postgresql"),

    "org.liquibase" % "liquibase-core" % versions("liquibase")

  )

  //tests
  private val scalaTest: Seq[ModuleID] = Seq(
    "org.scalatest" %% "scalatest" % versions("scala_test"),
    "org.scalactic" %% "scalactic" % versions("scala_test")
  ).map(_ % Test)

  private val scalaMock: Seq[ModuleID] = Seq(
    "org.scalamock" %% "scalamock" % versions("scala_mock")
  ).map(_ % Test)

  private val doobie: Seq[ModuleID] = Seq(
    "org.tpolecat" %% "doobie-scalatest" % versions("doobie")
  ).map(_ % Test)

  val mainDeps: Seq[ModuleID] =
    web.union(pureConfig)
       .union(logging)
       .union(cats)
       .union(db)

  val testDeps: Seq[ModuleID] =
    scalaTest.union(scalaMock)
             .union(doobie)

}
