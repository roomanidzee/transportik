package com.romanidze.transportik.modules.trip.info.http

import cats.implicits._
import cats.effect.Sync
import com.romanidze.transportik.modules.trip.info.dto.TripInfoDTO.TripInfoEntity
import com.romanidze.transportik.modules.trip.info.dto.TripInfoPreparator
import com.romanidze.transportik.modules.trip.info.services.TripInfoService
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class TripInfoRoutes[F[_]: Sync](service: TripInfoService[F])
    extends Http4sDsl[F]
    with TripInfoPreparator {

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root / "trip_info" / IntVar(id) => Ok(service.getTripInfo(id))

    case req @ POST -> Root / "trip_info" / "create" =>
      for {
        tripInfoDTO <- req.as[TripInfoEntity]
        result      <- service.saveTripInfo(tripInfoDTO)
        resp        <- Ok(result)
      } yield resp

  }

}
