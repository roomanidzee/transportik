package com.romanidze.transportik.modules.profile

import cats.effect.{ Async, ContextShift }
import com.romanidze.transportik.modules.profile.http.ProfileRoutes
import com.romanidze.transportik.modules.profile.repositories.ProfileRepository
import com.romanidze.transportik.modules.profile.services.ProfileService
import doobie.Transactor
import org.http4s.HttpRoutes

class ProfileModule[F[_]: Async: ContextShift](transactor: Transactor[F]) {

  private val repository = new ProfileRepository[F](transactor)
  private val service    = new ProfileService[F](repository)

  val routes: HttpRoutes[F] = new ProfileRoutes[F](service).routes

}
