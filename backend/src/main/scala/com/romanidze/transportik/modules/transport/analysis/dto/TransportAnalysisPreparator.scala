package com.romanidze.transportik.modules.transport.analysis.dto

import java.time.format.DateTimeFormatter

import cats.Applicative
import cats.effect.IO
import com.romanidze.transportik.modules.transport.analysis.domain.TransportAnalysisDomain
import io.circe.{ Encoder, Json }
import org.http4s.circe.jsonEncoderOf
import org.http4s.EntityEncoder

trait TransportAnalysisPreparator {

  private val timeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss V z")

  implicit val analysisEncoder: Encoder[TransportAnalysisDomain] =
    (a: TransportAnalysisDomain) =>
      Json.obj(
        ("record_time", Json.fromString(timeFormatter.format(a.recordTime))),
        ("transport_id", Json.fromLong(a.transportID)),
        ("is_busy", Json.fromBoolean(a.isBusy)),
        ("is_repairing", Json.fromBoolean(a.isRepairing)),
        ("trip_id", Json.fromLong(a.tripID)),
        ("distance", Json.fromLong(a.distance)),
        ("cost", Json.fromLong(a.cost))
      )

  implicit def analysisEntityEncoder[F[_]: Applicative]: EntityEncoder[F, TransportAnalysisDomain] =
    jsonEncoderOf
  implicit val analysisIOEncoder: EntityEncoder[IO, TransportAnalysisDomain] =
    jsonEncoderOf[IO, TransportAnalysisDomain]

}
