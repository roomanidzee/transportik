package com.romanidze.transportik.application

import cats.effect.{ ExitCode, IO, IOApp }
import cats.implicits._

import scala.concurrent.{ ExecutionContext, ExecutionContextExecutor }

object Main extends IOApp {
  implicit val executor: ExecutionContextExecutor = ExecutionContext.global

  override def run(args: List[String]): IO[ExitCode] =
    Server.launch[IO].compile.drain.as(ExitCode.Success)
}
