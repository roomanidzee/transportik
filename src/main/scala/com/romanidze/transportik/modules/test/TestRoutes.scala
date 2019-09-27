package com.romanidze.transportik.modules.test

import cats.Applicative
import cats.effect.{ IO, Sync }
import io.circe.{ Decoder, Encoder }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import org.http4s.{ EntityDecoder, EntityEncoder, HttpRoutes }
import org.http4s.dsl.Http4sDsl
import org.http4s.circe.{ jsonEncoderOf, jsonOf }

class TestRoutes[F[_]: Sync] extends Http4sDsl[F] {

  implicit val messageEncoder: Encoder.AsObject[MessageResponse] =
    deriveEncoder[MessageResponse]
  implicit def messageEntityEncoder[F[_]: Applicative]: EntityEncoder[F, MessageResponse] =
    jsonEncoderOf
  implicit val messageIOEncoder: EntityEncoder[IO, MessageResponse] =
    jsonEncoderOf[IO, MessageResponse]

  implicit val messageDecoder: Decoder[MessageResponse] =
    deriveDecoder[MessageResponse]
  implicit def messageEntityDecoder[F[_]: Sync]: EntityDecoder[F, MessageResponse] = jsonOf
  implicit val messageIODecoder: EntityDecoder[IO, MessageResponse] =
    jsonOf[IO, MessageResponse]

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {
    case req @ GET -> Root / "test" =>
      Ok(MessageResponse(message = "hello!"))
  }

}
