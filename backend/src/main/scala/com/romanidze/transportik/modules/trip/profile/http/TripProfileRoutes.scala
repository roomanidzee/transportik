package com.romanidze.transportik.modules.trip.profile.http

import cats.implicits._
import cats.effect.Sync
import com.romanidze.transportik.modules.trip.profile.dto.TripProfileDTO.TripProfileEntity
import com.romanidze.transportik.modules.trip.profile.dto.TripProfilePreparator
import com.romanidze.transportik.modules.trip.profile.services.TripProfileService
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class TripProfileRoutes[F[_]: Sync](service: TripProfileService[F])
    extends Http4sDsl[F]
    with TripProfilePreparator {

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root / "trip_profile" / IntVar(id) => Ok(service.getTripProfile(id))

    case req @ POST -> Root / "trip_profile" / "create" =>
      for {
        tripProfileDTO <- req.as[TripProfileEntity]
        result         <- service.saveTripProfile(tripProfileDTO)
        resp           <- Ok(result)
      } yield resp

  }

}
