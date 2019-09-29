package com.romanidze.transportik.modules.transport.core.dto

import java.time.ZonedDateTime

import cats.Applicative
import cats.effect.{IO, Sync}
import java.time.format.DateTimeFormatter

import com.romanidze.transportik.modules.transport.core.dto.TransportDTO.TransportInfoEntity
import io.circe.{Decoder, Encoder, HCursor, Json}
import org.http4s.circe.{jsonEncoderOf, jsonOf}
import org.http4s.{EntityDecoder, EntityEncoder}

trait TransportPreparator {

  private val timeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss V z")

  implicit val transportEncoder: Encoder[TransportInfoEntity] =
    (a: TransportInfoEntity) =>
      Json.obj(
        ("name", Json.fromString(a.name)),
        ("length", Json.fromLong(a.length)),
        ("tonnage", Json.fromLong(a.tonnage)),
        ("created_time", Json.fromString(timeFormatter.format(a.createdTime))),
        ("type", Json.fromInt(a.transportType))
      )

  implicit def transportEntityEncoder[F[_]: Applicative]: EntityEncoder[F, TransportInfoEntity] = jsonEncoderOf
  implicit val transportIOEncoder: EntityEncoder[IO, TransportInfoEntity] = jsonEncoderOf[IO, TransportInfoEntity]

  implicit val transportDecoder: Decoder[TransportInfoEntity] =
    (c: HCursor) =>
      for {
        name <- c.downField("name").as[String]
        length <- c.downField("length").as[Long]
        tonnage <- c.downField("tonnage").as[Long]
        createdTime <- c.downField("created_time").as[String]
        transportType <- c.downField("type").as[Int]
      } yield {
        TransportInfoEntity(
          name,
          length,
          tonnage,
          ZonedDateTime.parse(createdTime, timeFormatter),
          transportType
        )
      }

  implicit def transportEntityDecoder[F[_]: Sync]: EntityDecoder[F, TransportInfoEntity] = jsonOf
  implicit val transportIODecoder: EntityDecoder[IO, TransportInfoEntity] = jsonOf[IO, TransportInfoEntity]

}

object TransportPreparator extends TransportPreparator
