package com.romanidze.transportik.modules.test

import cats.effect.{ Async, ContextShift }
import org.http4s.HttpRoutes

class TestModule[F[_]: Async: ContextShift] {

  val routes: HttpRoutes[F] = new TestRoutes[F]().routes

}
