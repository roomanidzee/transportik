package com.romanidze.transportik.modules.trip.core.http

import cats.implicits._
import cats.effect.Sync
import com.romanidze.transportik.modules.trip.core.dto.TripDTO.TripEntity
import com.romanidze.transportik.modules.trip.core.dto.TripPreparator
import com.romanidze.transportik.modules.trip.core.services.TripService
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class TripRoutes[F[_]: Sync](service: TripService[F]) extends Http4sDsl[F] with TripPreparator {

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root / "trips" => Ok(service.getTrips)

    case req @ POST -> Root / "trips" / "create" =>
      for {
        tripDTO <- req.as[TripEntity]
        result  <- service.saveTrip(tripDTO)
        resp    <- Ok(result)
      } yield resp

  }

}
