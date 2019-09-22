package com.romanidze.transportik.modules.test

import cats.effect.IO
import org.http4s.{ Method, Request, Status, Uri }
import org.scalatest.{ Matchers, WordSpec }

class TestSimpleSpec extends WordSpec with Matchers {

  "Request to simple endpoint" should {
      "retrieve simple message" in {

        val request = Request[IO](Method.GET, Uri.uri("/test"))

        val responseIO = new TestRoutes[IO]().routes.run(request)

        val response = responseIO.value.unsafeRunSync().get

        response.status shouldBe Status.Ok

      }
    }

}
