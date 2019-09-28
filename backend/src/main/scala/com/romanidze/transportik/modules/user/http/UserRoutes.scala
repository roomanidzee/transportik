package com.romanidze.transportik.modules.user.http

import cats.implicits._
import cats.effect.Sync
import com.romanidze.transportik.modules.user.dto.{ UserDTO, UserEncoder }
import com.romanidze.transportik.modules.user.services.UserService
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class UserRoutes[F[_]: Sync](service: UserService[F]) extends Http4sDsl[F] with UserEncoder {

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root / "users" => Ok(service.getUsers)

    case req @ POST -> Root / "users" / "create" =>
      for {
        userDTO <- req.as[UserDTO]
        result  <- service.saveUser(userDTO)
        resp    <- Ok(result)
      } yield resp

  }

}
