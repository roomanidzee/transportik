package com.romanidze.transportik.modules.transport.analysis.http

import java.time.ZonedDateTime

import cats.implicits._
import cats.effect.Sync
import com.romanidze.transportik.modules.transport.analysis.domain.TransportAnalysisDomain
import com.romanidze.transportik.modules.transport.analysis.dto.TransportAnalysisPreparator
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class TransportAnalysisRoutes[F[_]: Sync] extends Http4sDsl[F] with TransportAnalysisPreparator {

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root / "trips" / "analyse" =>
      for {
        resp <- Ok(
          TransportAnalysisDomain(
            ZonedDateTime.now(),
            1,
            isBusy = true,
            isRepairing = true,
            1,
            1,
            1
          )
        )
      } yield resp

  }

}
