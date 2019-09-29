package com.romanidze.transportik.modules.transport.position.dto

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import io.circe.syntax._
import cats.Applicative
import cats.effect.{ IO, Sync }
import com.romanidze.transportik.components.db.PointEntity
import com.romanidze.transportik.components.http.PointEntityPreparator
import com.romanidze.transportik.modules.transport.position.dto.TransportPositionDTO.{ Results, TransportPositionEntity }
import io.circe.generic.semiauto.deriveEncoder
import io.circe.{ Decoder, Encoder, HCursor, Json }
import org.http4s.circe.{ jsonEncoderOf, jsonOf }
import org.http4s.{ EntityDecoder, EntityEncoder }

trait TransportPositionPreparator extends PointEntityPreparator {

  private val timeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

  implicit val positionEncoder: Encoder[TransportPositionEntity] =
    (a: TransportPositionEntity) =>
      Json.obj(
        ("transport_id", Json.fromLong(a.transportID)),
        ("coordinate", a.coordinate.asJson),
        ("record_date", Json.fromString(timeFormatter.format(a.recordDate)))
      )

  implicit def positionEntityEncoder[F[_]: Applicative]: EntityEncoder[F, TransportPositionEntity] =
    jsonEncoderOf
  implicit val positionIOEncoder: EntityEncoder[IO, TransportPositionEntity] =
    jsonEncoderOf[IO, TransportPositionEntity]

  implicit val userListEncoder: Encoder.AsObject[Results] = deriveEncoder[Results]
  implicit def userListEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Results] =
    jsonEncoderOf
  implicit val userListIOEncoder: EntityEncoder[IO, Results] =
    jsonEncoderOf[IO, Results]

  implicit val positionDecoder: Decoder[TransportPositionEntity] =
    (c: HCursor) =>
      for {
        transportID <- c.downField("transport_id").as[Long]
        coordinate  <- c.downField("coordinate").as[PointEntity]
        recordDate  <- c.downField("record_date").as[String]
      } yield {
        TransportPositionEntity(
          transportID,
          coordinate,
          ZonedDateTime.parse(recordDate, timeFormatter)
        )
      }

  implicit def positionEntityDecoder[F[_]: Sync]: EntityDecoder[F, TransportPositionEntity] = jsonOf
  implicit val positionIODecoder: EntityDecoder[IO, TransportPositionEntity] =
    jsonOf[IO, TransportPositionEntity]

}
