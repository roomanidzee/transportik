package com.romanidze.transportik.modules.transport.core.http

import cats.implicits._
import cats.effect.Sync
import com.romanidze.transportik.modules.transport.core.dto.TransportDTO.TransportInfoEntity
import com.romanidze.transportik.modules.transport.core.dto.TransportPreparator
import com.romanidze.transportik.modules.transport.core.services.TransportService
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class TransportRoutes[F[_]: Sync](service: TransportService[F])
    extends Http4sDsl[F]
    with TransportPreparator {

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root / "transport" / IntVar(id) => Ok(service.getTransport(id))

    case req @ POST -> Root / "transport" / "create" =>
      for {
        transportDTO <- req.as[TransportInfoEntity]
        result       <- service.saveTransport(transportDTO)
        resp         <- Ok(result)
      } yield resp

  }

}
