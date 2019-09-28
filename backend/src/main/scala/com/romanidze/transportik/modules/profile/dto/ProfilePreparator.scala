package com.romanidze.transportik.modules.profile.dto

import cats.effect.{ IO, Sync }
import cats.{ Applicative, Id }
import com.romanidze.transportik.modules.profile.dto.ProfileDTO.{ ProfileInfo, ProfileOutput }
import io.circe.generic.semiauto.deriveEncoder
import io.circe.{ Decoder, Encoder, HCursor }
import org.http4s.{ EntityDecoder, EntityEncoder }
import org.http4s.circe.{ jsonEncoderOf, jsonOf }

trait ProfilePreparator {

  implicit val profileEncoder: Encoder.AsObject[ProfileOutput] = deriveEncoder[ProfileOutput]
  implicit def profileEntityEncoder[F[_]: Applicative]: EntityEncoder[Id, ProfileOutput] =
    jsonEncoderOf
  implicit val profileIOEncoder: EntityEncoder[IO, ProfileOutput] = jsonEncoderOf[IO, ProfileOutput]

  implicit val profileDecoder: Decoder[ProfileInfo] = (c: HCursor) =>
    for {
      userID     <- c.downField("user_id").as[Long]
      surname    <- c.downField("surname").as[String]
      name       <- c.downField("name").as[String]
      patronymic <- c.downField("patronymic").as[String]
      phone      <- c.downField("phone").as[String]
    } yield ProfileInfo(
      userID,
      surname,
      name,
      patronymic,
      phone
    )

  implicit def profileEntityDecoder[F[_]: Sync]: EntityDecoder[F, ProfileInfo] = jsonOf
  implicit val profileIODecoder: EntityDecoder[IO, ProfileInfo]                = jsonOf[IO, ProfileInfo]

}

object ProfilePreparator extends ProfilePreparator
