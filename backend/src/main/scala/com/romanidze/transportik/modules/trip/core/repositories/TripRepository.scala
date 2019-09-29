package com.romanidze.transportik.modules.trip.core.repositories

import cats.effect.{Async, ContextShift}
import com.romanidze.transportik.modules.trip.core.domain.TripDomain.{TripDBInput, TripDBOutput}
import doobie._
import doobie.implicits._

trait TripRepositoryTrait[F[_]]{
  def findAll(): F[List[TripDBOutput]]
  def find(id: Long): F[Option[TripDBOutput]]
  def insert(model: TripDBInput): F[Long]
  def update(id: Long, model: TripDBInput): F[Int]
  def delete(id: Long): F[Int]
}

class TripRepository[F[_]: Async: ContextShift](xa: Transactor[F])
   extends TripRepositoryTrait[F]{

  import TripRepository.SQL

  override def findAll(): F[List[TripDBOutput]] =
    SQL.findAll
    .to[List]
    .transact(xa)

  override def find(id: Long): F[Option[TripDBOutput]] =
    SQL
      .find(id)
      .option
      .transact(xa)

  override def insert(model: TripDBInput): F[Long] =
    SQL
      .insert(model)
      .withUniqueGeneratedKeys[Long]("id")
      .transact(xa)

  override def update(id: Long, model: TripDBInput): F[Int] =
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

object TripRepository{

  object SQL {

    import doobie.postgres.pgisimplicits._

    def findAll: Query0[TripDBOutput] =
      sql"""SELECT id, ST_AsText(source) AS source, ST_AsText(target) AS target FROM trip""".query[TripDBOutput]

    def find(id: Long): Query0[TripDBOutput] =
      sql"""SELECT id, ST_AsText(source) AS source, ST_AsText(target) AS target FROM trip
           |WHERE id = $id
           |""".stripMargin.query[TripDBOutput]

    def insert(model: TripDBInput): Update0 =
      sql"""INSERT INTO trip(source, target)
           |VALUES(
           |  ST_SetSRID(ST_MakePoint(${model.source.longitude}, ${model.source.latitude}), 4326),
           |  ST_SetSRID(ST_MakePoint(${model.target.longitude}, ${model.target.latitude}), 4326)
           |)""".stripMargin.update

    def update(id: Long, model: TripDBInput): Update0 =
      sql"""UPDATE trip
           |SET source = ST_SetSRID(ST_MakePoint(${model.source.longitude}, ${model.source.latitude}), 4326),
           |    target = ST_SetSRID(ST_MakePoint(${model.target.longitude}, ${model.target.latitude}), 4326)
           |WHERE id = $id""".stripMargin.update

    def delete(id: Long): Update0 =
      sql"""DELETE FROM trip WHERE id = $id""".update

  }

}
