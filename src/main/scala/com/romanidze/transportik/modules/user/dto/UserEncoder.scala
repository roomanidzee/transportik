package com.romanidze.transportik.modules.user.dto

import cats.Applicative
import cats.effect.{ IO, Sync }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }
import org.http4s.circe.{ jsonEncoderOf, jsonOf }
import org.http4s.{ EntityDecoder, EntityEncoder }

trait UserEncoder {

  implicit val userEncoder: Encoder.AsObject[UserDTO]                          = deriveEncoder[UserDTO]
  implicit def userEntityEncoder[F[_]: Applicative]: EntityEncoder[F, UserDTO] = jsonEncoderOf
  implicit val userIOEncoder: EntityEncoder[IO, UserDTO]                       = jsonEncoderOf[IO, UserDTO]

  implicit val userListEncoder: Encoder.AsObject[List[UserDTO]] = deriveEncoder[List[UserDTO]]
  implicit def userListEntityEncoder[F[_]: Applicative]: EntityEncoder[F, List[UserDTO]] =
    jsonEncoderOf
  implicit val userListIOEncoder: EntityEncoder[IO, List[UserDTO]] =
    jsonEncoderOf[IO, List[UserDTO]]

  implicit val userDecoder: Decoder[UserDTO]                            = deriveDecoder[UserDTO]
  implicit def userEntityDecoder[F[_]: Sync]: EntityDecoder[F, UserDTO] = jsonOf
  implicit val userIODecoder: EntityDecoder[IO, UserDTO]                = jsonOf[IO, UserDTO]

}

object UserEncoder extends UserEncoder
