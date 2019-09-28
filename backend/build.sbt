name := "transportik"

organization := "com.romanidze"
description := "Backend for db course"

version := "0.0.1"
scalaOrganization := "org.scala-lang"
scalaVersion := "2.12.8"

scalafmtOnCompile := true

resolvers ++= Seq(
  Resolver.mavenCentral,
  Resolver.mavenLocal
)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
)
maxErrors := 5

libraryDependencies ++=
  Dependencies.mainDeps ++
  Dependencies.testDeps
