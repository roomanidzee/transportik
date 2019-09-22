package com.romanidze.transportik.modules.test

import cats.effect.IO
import org.http4s.{ Method, Request, Status, Uri }
import org.scalatest.{ Matchers, WordSpec }
import org.http4s.syntax.kleisli._

class TestSimpleSpec extends WordSpec with Matchers {

  "Request to simple endpoint" should {
      "retrieve simple message" in {

        val request = Request[IO](Method.GET, Uri.uri("/test"))

        val responseIO = new TestRoutes[IO]().routes.orNotFound(request).unsafeRunSync()

        responseIO.status shouldBe Status.Ok

        responseIO.as[String].unsafeRunSync() shouldBe """{"message":"hello!"}"""

      }
    }

}
