package com.romanidze.transportik.modules.transport.position.http

import cats.implicits._
import cats.effect.Sync
import com.romanidze.transportik.modules.transport.position.dto.TransportPositionDTO.TransportPositionEntity
import com.romanidze.transportik.modules.transport.position.dto.TransportPositionPreparator
import com.romanidze.transportik.modules.transport.position.services.TransportPositionService
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class TransportPositionRoutes[F[_]: Sync](service: TransportPositionService[F])
    extends Http4sDsl[F]
    with TransportPositionPreparator {

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root / "positions" => Ok(service.getPositions)

    case req @ POST -> Root / "positions" / "create" =>
      for {
        positionDTO <- req.as[TransportPositionEntity]
        result      <- service.savePosition(positionDTO)
        resp        <- Ok(result)
      } yield resp

  }

}
