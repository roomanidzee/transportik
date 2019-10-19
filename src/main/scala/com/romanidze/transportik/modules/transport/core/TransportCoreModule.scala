package com.romanidze.transportik.modules.transport.core

import cats.effect.{ Async, ContextShift }
import com.romanidze.transportik.modules.transport.core.http.TransportRoutes
import com.romanidze.transportik.modules.transport.core.repositories.TransportRepository
import com.romanidze.transportik.modules.transport.core.services.TransportService
import doobie.util.transactor.Transactor
import org.http4s.HttpRoutes

class TransportCoreModule[F[_]: Async: ContextShift](transactor: Transactor[F]) {

  private val repository = new TransportRepository[F](transactor)
  private val service    = new TransportService[F](repository)

  val routes: HttpRoutes[F] = new TransportRoutes[F](service).routes

}
