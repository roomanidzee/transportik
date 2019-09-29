package com.romanidze.transportik.modules.trip.info.dto

import cats.Applicative
import cats.effect.{IO, Sync}
import com.romanidze.transportik.modules.trip.info.dto.TripInfoDTO.TripInfoEntity
import io.circe.{Decoder, Encoder, HCursor, Json}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

trait TripInfoPreparator {

  implicit val tripInfoEncoder: Encoder[TripInfoEntity] =
    (a: TripInfoEntity) =>
      Json.obj(
        ("trip_id", Json.fromLong(a.tripID)),
        ("transport_id", Json.fromLong(a.transportID)),
        ("cost", Json.fromLong(a.cost))
      )

  implicit def tripInfoEntityEncoder[F[_]: Applicative]: EntityEncoder[F, TripInfoEntity] = jsonEncoderOf
  implicit val tripInfoIOEncoder: EntityEncoder[IO, TripInfoEntity] = jsonEncoderOf[IO, TripInfoEntity]

  implicit val tripInfoDecoder: Decoder[TripInfoEntity] =
    (c: HCursor) => for {
    tripID <- c.downField("trip_id").as[Long]
    transportID <- c.downField("transport_id").as[Long]
    cost <- c.downField("cost").as[Long]
  } yield {
    TripInfoEntity(tripID, transportID, cost)
  }
  implicit def tripInfoEntityDecoder[F[_]: Sync]: EntityDecoder[F, TripInfoEntity] = jsonOf
  implicit val tripInfoIODecoder: EntityDecoder[IO, TripInfoEntity] = jsonOf[IO, TripInfoEntity]

}
