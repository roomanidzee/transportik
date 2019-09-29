package com.romanidze.transportik.modules.profile.dto

import cats.effect.{ IO, Sync }
import cats.{ Applicative, Id }
import com.romanidze.transportik.modules.profile.dto.ProfileDTO.{ ProfileInfo, ProfileOutput }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder, HCursor }
import org.http4s.{ EntityDecoder, EntityEncoder }
import org.http4s.circe.{ jsonEncoderOf, jsonOf }

trait ProfilePreparator {

  implicit val profileEncoder: Encoder.AsObject[ProfileOutput] = deriveEncoder[ProfileOutput]
  implicit def profileEntityEncoder[F[_]: Applicative]: EntityEncoder[F, ProfileOutput] =
    jsonEncoderOf
  implicit val profileIOEncoder: EntityEncoder[IO, ProfileOutput] = jsonEncoderOf[IO, ProfileOutput]

  implicit val profileInfoDecoder: Decoder[ProfileInfo]                            = deriveDecoder[ProfileInfo]
  implicit def profileInfoEntityDecoder[F[_]: Sync]: EntityDecoder[F, ProfileInfo] = jsonOf
  implicit val profileInfoIODecoder: EntityDecoder[IO, ProfileInfo]                = jsonOf[IO, ProfileInfo]
}

object ProfilePreparator extends ProfilePreparator
