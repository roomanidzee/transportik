package com.romanidze.transportik.modules.profile.repositories

import cats.effect.{ Async, ContextShift }
import com.romanidze.transportik.modules.profile.domain.ProfileDomain.Profile
import doobie._
import doobie.implicits._

trait ProfileRepositoryTrait[F[_]] {
  def findAll: F[List[Profile]]
  def find(id: Long): F[Option[Profile]]
  def insert(model: Profile): F[Long]
  def update(id: Long, model: Profile): F[Int]
  def delete(id: Long): F[Int]

  def findProfileByUserID(userID: Long): F[Option[Profile]]

}

final class ProfileRepository[F[_]: Async: ContextShift](xa: Transactor[F])
    extends ProfileRepositoryTrait[F] {

  import ProfileRepository.SQL

  override def findAll: F[List[Profile]] =
    SQL.findAll
      .to[List]
      .transact(xa)

  override def find(id: Long): F[Option[Profile]] =
    SQL
      .find(id)
      .option
      .transact(xa)

  override def insert(model: Profile): F[Long] =
    SQL
      .insert(model)
      .withUniqueGeneratedKeys[Long]("id")
      .transact(xa)

  override def update(id: Long, model: Profile): F[Int] =
    SQL
      .update(id, model)
      .run
      .transact(xa)

  override def delete(id: Long): F[Int] =
    SQL
      .delete(id)
      .run
      .transact(xa)

  override def findProfileByUserID(userID: Long): F[Option[Profile]] =
    SQL
      .findProfileByUserID(userID)
      .option
      .transact(xa)
}

object ProfileRepository {

  object SQL {

    def findAll: Query0[Profile] =
      sql"""SELECT * FROM profile""".query[Profile]

    def find(id: Long): Query0[Profile] =
      sql"""SELECT * FROM profile WHERE id = $id""".query[Profile]

    def insert(model: Profile): Update0 =
      sql"""INSERT INTO profile(user_id, surname, name, patronymic, phone)
           |VALUES(${model.user}, ${model.surname}, ${model.name}, ${model.patronymic}, ${model.phone})
           |""".stripMargin.update

    def update(id: Long, model: Profile): Update0 =
      sql"""UPDATE profile SET user_id = ${model.user},
           |                   surname = ${model.surname},
           |                   name = ${model.name},
           |                   patronymic = ${model.patronymic},
           |                   phone = ${model.phone}
           |WHERE id = $id
           |""".stripMargin.update

    def delete(id: Long): Update0 =
      sql"""DELETE FROM profile WHERE id = $id""".update

    def findProfileByUserID(userID: Long): Query0[Profile] =
      sql"""SELECT * FROM profile WHERE user_id = $userID""".query[Profile]

  }

}
