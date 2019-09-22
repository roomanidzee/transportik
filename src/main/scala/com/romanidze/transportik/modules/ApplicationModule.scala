package com.romanidze.transportik.modules

import cats.data.Kleisli
import cats.effect.{ Async, ContextShift }
import com.romanidze.transportik.config.ApplicationConfig
import com.romanidze.transportik.modules.test.TestModule
import org.http4s.{ Request, Response }
import org.http4s.server.Router
import org.http4s.syntax.kleisli._

class ApplicationModule[F[_]: Async: ContextShift](config: ApplicationConfig) {

  val testModule = new TestModule[F]

  val router: Kleisli[F, Request[F], Response[F]] = Router[F](config.server.prefix -> testModule.routes).orNotFound

}
