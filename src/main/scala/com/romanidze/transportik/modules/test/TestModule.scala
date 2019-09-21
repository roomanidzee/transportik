package com.romanidze.transportik.modules.test

import cats.effect.{ Async, ContextShift }
import com.romanidze.transportik.config.ServerConfig
import com.softwaremill.macwire.wire
import org.http4s.HttpRoutes
import org.http4s.server.Router

class TestModule[F[_]: Async: ContextShift](config: ServerConfig) {

  val routes: HttpRoutes[F] = Router[F](config.prefix -> wire[TestRoutes[F]].routes)

}
