package com.romanidze.transportik.modules.transport

import cats.effect.{ Async, ContextShift }
import cats.implicits._
import com.romanidze.transportik.modules.transport.analysis.TransportAnalysisModule
import com.romanidze.transportik.modules.transport.core.TransportCoreModule
import com.romanidze.transportik.modules.transport.position.TransportPositionModule
import doobie.util.transactor.Transactor
import org.http4s.HttpRoutes

class TransportModule[F[_]: Async: ContextShift](transactor: Transactor[F]) {

  private val analysisModule = new TransportAnalysisModule[F]
  private val coreModule     = new TransportCoreModule[F](transactor)
  private val positionModule = new TransportPositionModule[F](transactor)

  val routes: HttpRoutes[F] = analysisModule.routes <+> coreModule.routes <+> positionModule.routes

}
