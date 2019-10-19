package com.romanidze.transportik.modules.transport.position

import cats.effect.{ Async, ContextShift }
import com.romanidze.transportik.modules.transport.position.http.TransportPositionRoutes
import com.romanidze.transportik.modules.transport.position.repositories.TransportPositionRepository
import com.romanidze.transportik.modules.transport.position.services.TransportPositionService
import doobie.util.transactor.Transactor
import org.http4s.HttpRoutes

class TransportPositionModule[F[_]: Async: ContextShift](transactor: Transactor[F]) {

  private val repository = new TransportPositionRepository[F](transactor)
  private val service    = new TransportPositionService[F](repository)

  val routes: HttpRoutes[F] = new TransportPositionRoutes[F](service).routes

}
