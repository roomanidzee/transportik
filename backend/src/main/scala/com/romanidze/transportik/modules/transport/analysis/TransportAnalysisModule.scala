package com.romanidze.transportik.modules.transport.analysis

import cats.effect.{ Async, ContextShift }
import com.romanidze.transportik.modules.transport.analysis.http.TransportAnalysisRoutes
import org.http4s.HttpRoutes

class TransportAnalysisModule[F[_]: Async: ContextShift] {

  val routes: HttpRoutes[F] = new TransportAnalysisRoutes[F]().routes

}
