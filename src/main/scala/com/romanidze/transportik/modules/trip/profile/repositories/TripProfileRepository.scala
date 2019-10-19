package com.romanidze.transportik.modules.trip.profile.repositories

import cats.effect.{ Async, ContextShift }
import com.romanidze.transportik.modules.trip.profile.domain.TripProfileDomain.TripProfile
import doobie._
import doobie.implicits._

trait TripProfileRepositoryTrait[F[_]] {

  def findAll(): F[List[TripProfile]]
  def find(id: Long): F[Option[TripProfile]]
  def insert(model: TripProfile): F[Long]
  def update(id: Long, model: TripProfile): F[Int]
  def delete(id: Long): F[Int]

}

final class TripProfileRepository[F[_]: Async: ContextShift](xa: Transactor[F])
    extends TripProfileRepositoryTrait[F] {

  import TripProfileRepository.SQL

  override def findAll(): F[List[TripProfile]] =
    SQL.findAll
      .to[List]
      .transact(xa)

  override def find(id: Long): F[Option[TripProfile]] =
    SQL
      .find(id)
      .option
      .transact(xa)

  override def insert(model: TripProfile): F[Long] =
    SQL
      .insert(model)
      .withUniqueGeneratedKeys[Long]("id")
      .transact(xa)

  override def update(id: Long, model: TripProfile): F[Int] =
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

object TripProfileRepository {

  object SQL {

    def findAll: Query0[TripProfile] =
      sql"""SELECT * FROM trip_to_profile""".query[TripProfile]

    def find(id: Long): Query0[TripProfile] =
      sql"""SELECT * FROM trip_to_profile WHERE id = $id""".query[TripProfile]

    def insert(model: TripProfile): Update0 =
      sql"""INSERT INTO trip_to_profile(trip_id, profile_id)
           |VALUES(${model.tripID}, ${model.profileID})
           |""".stripMargin.update

    def update(id: Long, model: TripProfile): Update0 =
      sql"""UPDATE trip_to_profile
           |SET trip_id = ${model.tripID},
           |    profile_id = ${model.profileID}
           |WHERE id = $id
           |""".stripMargin.update

    def delete(id: Long): Update0 =
      sql"""DELETE FROM trip_to_profile WHERE id = $id""".update

  }

}
