package com.romanidze.transportik.modules.user.services

import cats.effect.{ Async, ContextShift }
import cats.syntax.functor._
import com.romanidze.transportik.modules.user.dto.{ Results, UserDTO }
import com.romanidze.transportik.modules.user.mappers.UserMapper
import com.romanidze.transportik.modules.user.repositories.UserRepository

trait UserServiceTrait[F[_]] {

  def getUsers: F[Results]
  def saveUser(userDTO: UserDTO): F[UserDTO]

}

final class UserService[F[_]: Async: ContextShift](repository: UserRepository[F])
    extends UserServiceTrait[F] {

  override def getUsers: F[Results] = {

    val result = for {
      dbResult     <- repository.findAll()
      mappedResult = dbResult.map(user => UserMapper.domainToDTO(user))
    } yield Results(mappedResult)

    result

  }

  override def saveUser(userDTO: UserDTO): F[UserDTO] = {

    val result = for {
      _ <- repository.insert(UserMapper.dtoToDomain(userDTO))
    } yield userDTO

    result
  }

}
