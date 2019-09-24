package com.romanidze.transportik.modules.user

import cats.effect.{ Async, ContextShift }

import doobie._
import doobie.implicits._

trait UserRepository[F[_]] {
  def findAll(): F[List[User]]
  def find(id: Long): F[Option[User]]
  def insert(model: User): F[Long]
  def update(id: Long, model: User): F[Int]
  def delete(id: Long): F[Int]
}

final class UserRepositoryImpl[F[_]: Async: ContextShift](xa: Transactor[F]) extends UserRepository[F] {

  import UserRepositoryImpl.SQL

  override def findAll(): F[List[User]] =
    SQL.findAll
      .to[List]
      .transact(xa)

  override def find(id: Long): F[Option[User]] =
    SQL
      .find(id)
      .option
      .transact(xa)

  override def insert(model: User): F[Long] =
    SQL
      .insert(model)
      .withUniqueGeneratedKeys[Long]("id")
      .transact(xa)

  override def update(id: Long, model: User): F[Int] =
    SQL
      .update(id, model)
      .run
      .transact(xa)

  override def delete(id: Long): F[Int] =
    SQL
      .delete(id)
      .run
      .transact(xa)
}

object UserRepositoryImpl {

  object SQL {

    def findAll: Query0[User] =
      sql"""SELECT * FROM users""".query[User]

    def find(id: Long): Query0[User] =
      sql"""SELECT * FROM users WHERE id = $id""".query[User]

    def insert(model: User): Update0 =
      sql"""INSERT INTO users(username, password)
           |VALUES(${model.username}, ${model.password})
           |""".stripMargin.update

    def update(id: Long, model: User): Update0 =
      sql"""UPDATE users SET username = ${model.username}, password = ${model.password}
           |WHERE id = $id
           |""".stripMargin.update

    def delete(id: Long): Update0 =
      sql"""DELETE FROM users WHERE id = $id""".update

  }

}
