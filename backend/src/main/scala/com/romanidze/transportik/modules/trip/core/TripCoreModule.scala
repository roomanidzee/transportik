package com.romanidze.transportik.modules.trip.core

import cats.effect.{Async, ContextShift}
import com.romanidze.transportik.modules.trip.core.http.TripRoutes
import com.romanidze.transportik.modules.trip.core.repositories.TripRepository
import com.romanidze.transportik.modules.trip.core.services.TripService
import doobie.util.transactor.Transactor
import org.http4s.HttpRoutes

class TripCoreModule[F[_]: Async: ContextShift](transactor: Transactor[F]) {

  private val repository = new TripRepository[F](transactor)
  private val service = new TripService[F](repository)

  val routes: HttpRoutes[F] = new TripRoutes[F](service).routes

}
