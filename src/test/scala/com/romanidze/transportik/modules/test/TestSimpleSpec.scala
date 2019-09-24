package com.romanidze.transportik.modules.test

import cats.effect.IO
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import org.http4s.{ EntityDecoder, Method, Request, Status, Uri }
import org.scalatest.{ Matchers, WordSpec }
import org.http4s.syntax.kleisli._
import org.http4s.circe.jsonOf

class TestSimpleSpec extends WordSpec with Matchers {

  "Request to simple endpoint" should {
      "retrieve simple message" in {

        val request = Request[IO](Method.GET, Uri.uri("/test"))

        val responseIO                                                    = new TestRoutes[IO]().routes.orNotFound(request).unsafeRunSync()
        implicit val messageDecoder: Decoder[MessageResponse]             = deriveDecoder[MessageResponse]
        implicit val messageIODecoder: EntityDecoder[IO, MessageResponse] = jsonOf[IO, MessageResponse]

        responseIO.status shouldBe Status.Ok

        responseIO.as[MessageResponse].unsafeRunSync() shouldBe MessageResponse(message = "hello!")

      }
    }

}
