package com.romanidze.transportik.modules.trip.profile.dto

import cats.Applicative
import cats.effect.{ IO, Sync }
import com.romanidze.transportik.modules.trip.profile.dto.TripProfileDTO.TripProfileEntity
import io.circe.{ Decoder, Encoder, HCursor, Json }
import org.http4s.circe.{ jsonEncoderOf, jsonOf }
import org.http4s.{ EntityDecoder, EntityEncoder }

trait TripProfilePreparator {

  implicit val tripProfileEncoder: Encoder[TripProfileEntity] =
    (a: TripProfileEntity) =>
      Json.obj(
        ("trip_id", Json.fromLong(a.tripID)),
        ("profile_id", Json.fromLong(a.profileID))
      )

  implicit def tripProfileEntityEncoder[F[_]: Applicative]: EntityEncoder[F, TripProfileEntity] =
    jsonEncoderOf
  implicit val tripProfileIOEncoder: EntityEncoder[IO, TripProfileEntity] =
    jsonEncoderOf[IO, TripProfileEntity]

  implicit val tripProfileDecoder: Decoder[TripProfileEntity] =
    (c: HCursor) =>
      for {

        tripID    <- c.downField("trip_id").as[Long]
        profileID <- c.downField("profile_id").as[Long]

      } yield {
        TripProfileEntity(tripID, profileID)
      }

  implicit def tripProfileEntityDecoder[F[_]: Sync]: EntityDecoder[F, TripProfileEntity] = jsonOf
  implicit val tripProfileIODecoder: EntityDecoder[IO, TripProfileEntity] =
    jsonOf[IO, TripProfileEntity]

}

object TripProfilePreparator extends TripProfilePreparator
