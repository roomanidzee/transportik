name := "transportik"

organization := "com.romanidze"
description := "Backend for db course"

version := "0.0.1"
scalaOrganization := "org.scala-lang"
scalaVersion := "2.12.8"

scalafmtOnCompile := true

resolvers ++= Seq(
  Resolver.mavenCentral,
  Resolver.mavenLocal,
  Resolver.bintrayRepo("sbt-assembly", "maven")
)

assemblyJarName in assembly := "app.jar"
test in assembly := {}
mainClass in assembly := Some("com.romanidze.transportik.application.Main")

assemblyMergeStrategy in assembly := {
  case x if x.contains("io.netty.versions.properties") => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification"
)
maxErrors := 5

libraryDependencies ++=
  Dependencies.mainDeps ++
    Dependencies.testDeps
