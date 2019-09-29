package com.romanidze.transportik.components.http

import cats.Applicative
import cats.effect.{ IO, Sync }
import com.romanidze.transportik.components.db.PointEntity
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }
import org.http4s.circe.{ jsonEncoderOf, jsonOf }
import org.http4s.{ EntityDecoder, EntityEncoder }

trait PointEntityPreparator {

  implicit val pointEncoder: Encoder.AsObject[PointEntity]                          = deriveEncoder[PointEntity]
  implicit def pointEntityEncoder[F[_]: Applicative]: EntityEncoder[F, PointEntity] = jsonEncoderOf
  implicit val pointIOEncoder: EntityEncoder[IO, PointEntity]                       = jsonEncoderOf[IO, PointEntity]

  implicit val pointDecoder: Decoder[PointEntity]                            = deriveDecoder[PointEntity]
  implicit def pointEntityDecoder[F[_]: Sync]: EntityDecoder[F, PointEntity] = jsonOf
  implicit val pointIODecoder: EntityDecoder[IO, PointEntity]                = jsonOf[IO, PointEntity]

}

object PointEntityPreparator extends PointEntityPreparator
