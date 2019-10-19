package com.romanidze.transportik.modules.trip.info

import cats.effect.{ Async, ContextShift }
import com.romanidze.transportik.modules.trip.info.http.TripInfoRoutes
import com.romanidze.transportik.modules.trip.info.repositories.TripInfoRepository
import com.romanidze.transportik.modules.trip.info.services.TripInfoService
import doobie.util.transactor.Transactor
import org.http4s.HttpRoutes

class TripInfoModule[F[_]: Async: ContextShift](transactor: Transactor[F]) {

  private val repository = new TripInfoRepository[F](transactor)
  private val service    = new TripInfoService[F](repository)

  val routes: HttpRoutes[F] = new TripInfoRoutes[F](service).routes

}
