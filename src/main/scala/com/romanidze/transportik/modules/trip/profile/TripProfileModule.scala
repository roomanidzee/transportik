package com.romanidze.transportik.modules.trip.profile

import cats.effect.{ Async, ContextShift }
import com.romanidze.transportik.modules.trip.profile.http.TripProfileRoutes
import com.romanidze.transportik.modules.trip.profile.repositories.TripProfileRepository
import com.romanidze.transportik.modules.trip.profile.services.TripProfileService
import doobie.util.transactor.Transactor
import org.http4s.HttpRoutes

class TripProfileModule[F[_]: Async: ContextShift](transactor: Transactor[F]) {

  private val repository = new TripProfileRepository[F](transactor)
  private val service    = new TripProfileService[F](repository)

  val routes: HttpRoutes[F] = new TripProfileRoutes[F](service).routes

}
