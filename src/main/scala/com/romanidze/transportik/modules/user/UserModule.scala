package com.romanidze.transportik.modules.user

import cats.effect.{ Async, ContextShift }
import com.romanidze.transportik.modules.user.http.UserRoutes
import com.romanidze.transportik.modules.user.repositories.UserRepository
import com.romanidze.transportik.modules.user.services.UserService
import doobie.Transactor
import org.http4s.HttpRoutes

class UserModule[F[_]: Async: ContextShift](transactor: Transactor[F]) {

  private val repository = new UserRepository[F](transactor)
  private val service    = new UserService[F](repository)

  val routes: HttpRoutes[F] = new UserRoutes[F](service).routes

}
