package com.romanidze.transportik.modules.trip.info.repositories

import cats.effect.{ Async, ContextShift }
import com.romanidze.transportik.modules.trip.info.domain.TripInfoDomain.TripInfo
import doobie._
import doobie.implicits._

trait TripInfoRepositoryTrait[F[_]] {

  def findAll(): F[List[TripInfo]]
  def find(id: Long): F[Option[TripInfo]]
  def insert(model: TripInfo): F[Long]
  def update(id: Long, model: TripInfo): F[Int]
  def delete(id: Long): F[Int]

}

final class TripInfoRepository[F[_]: Async: ContextShift](xa: Transactor[F])
    extends TripInfoRepositoryTrait[F] {

  import TripInfoRepository.SQL

  override def findAll(): F[List[TripInfo]] =
    SQL.findAll
      .to[List]
      .transact(xa)

  override def find(id: Long): F[Option[TripInfo]] =
    SQL
      .find(id)
      .option
      .transact(xa)

  override def insert(model: TripInfo): F[Long] =
    SQL
      .insert(model)
      .withUniqueGeneratedKeys[Long]("id")
      .transact(xa)

  override def update(id: Long, model: TripInfo): F[Int] =
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

object TripInfoRepository {

  object SQL {

    def findAll: Query0[TripInfo] =
      sql"""SELECT * FROM trip_info""".query[TripInfo]

    def find(id: Long): Query0[TripInfo] =
      sql"""SELECT * FROM trip_info WHERE id = $id""".query[TripInfo]

    def insert(model: TripInfo): Update0 =
      sql"""INSERT INTO trip_info(trip_id, transport_id, cost)
           |VALUES(${model.tripID}, ${model.transportID}, ${model.cost})
           |""".stripMargin.update

    def update(id: Long, model: TripInfo): Update0 =
      sql"""UPDATE trip_info
           |SET trip_id = ${model.tripID},
           |    transport_id = ${model.transportID},
           |    cost = ${model.cost}
           |WHERE id = $id
           |""".stripMargin.update

    def delete(id: Long): Update0 =
      sql"""DELETE FROM trip_info WHERE id = $id""".update

  }

}
