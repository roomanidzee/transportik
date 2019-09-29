package com.romanidze.transportik.modules.trip.core.dto

import cats.Applicative
import cats.effect.{ IO, Sync }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }
import org.http4s.circe.{ jsonEncoderOf, jsonOf }
import org.http4s.{ EntityDecoder, EntityEncoder }
import com.romanidze.transportik.components.http.PointEntityPreparator
import com.romanidze.transportik.modules.trip.core.dto.TripDTO.{ Results, TripEntity }

trait TripPreparator extends PointEntityPreparator {

  implicit val tripEncoder: Encoder.AsObject[TripEntity]                          = deriveEncoder[TripEntity]
  implicit def tripEntityEncoder[F[_]: Applicative]: EntityEncoder[F, TripEntity] = jsonEncoderOf
  implicit val tripIOEncoder: EntityEncoder[IO, TripEntity]                       = jsonEncoderOf[IO, TripEntity]

  implicit val userListEncoder: Encoder.AsObject[Results] = deriveEncoder[Results]
  implicit def userListEntityEncoder[F[_]: Applicative]: EntityEncoder[F, Results] =
    jsonEncoderOf
  implicit val userListIOEncoder: EntityEncoder[IO, Results] =
    jsonEncoderOf[IO, Results]

  implicit val tripDecoder: Decoder[TripEntity]                            = deriveDecoder[TripEntity]
  implicit def tripEntityDecoder[F[_]: Sync]: EntityDecoder[F, TripEntity] = jsonOf
  implicit val tripIODecoder: EntityDecoder[IO, TripEntity]                = jsonOf[IO, TripEntity]

}

object TripPreparator extends TripPreparator
