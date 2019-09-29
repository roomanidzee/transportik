package com.romanidze.transportik.modules.profile.http

import cats.implicits._
import cats.effect.Sync
import com.romanidze.transportik.modules.profile.dto.ProfileDTO.ProfileInfo
import com.romanidze.transportik.modules.profile.dto.ProfilePreparator
import com.romanidze.transportik.modules.profile.services.ProfileService
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class ProfileRoutes[F[_]: Sync](service: ProfileService[F])
    extends Http4sDsl[F]
    with ProfilePreparator {

  val routes: HttpRoutes[F] = HttpRoutes.of[F] {

    case GET -> Root / "profiles" / "user" / IntVar(id) =>
      Ok(service.getProfile(id))

    case req @ POST -> Root / "profiles" / "create" =>
      for {
        profileDTO <- req.as[ProfileInfo]
        result     <- service.saveProfile(profileDTO)
        resp       <- Ok(result)
      } yield resp

  }

}
