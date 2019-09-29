package com.romanidze.transportik.modules.transport.position.repositories

import cats.effect.{ Async, ContextShift }
import com.romanidze.transportik.modules.transport.position.domain.TransportPositionDomain.{ TransportPositionInput, TransportPositionOutput }
import doobie._
import doobie.implicits._

trait TransportPositionRepositoryTrait[F[_]] {

  def findAll(): F[List[TransportPositionOutput]]
  def find(id: Long): F[Option[TransportPositionOutput]]
  def insert(model: TransportPositionInput): F[Long]
  def update(id: Long, model: TransportPositionInput): F[Int]
  def delete(id: Long): F[Int]

}

class TransportPositionRepository[F[_]: Async: ContextShift](xa: Transactor[F])
    extends TransportPositionRepositoryTrait[F] {

  import TransportPositionRepository.SQL

  override def findAll(): F[List[TransportPositionOutput]] =
    SQL.findAll
      .to[List]
      .transact(xa)

  override def find(id: Long): F[Option[TransportPositionOutput]] =
    SQL
      .find(id)
      .option
      .transact(xa)

  override def insert(model: TransportPositionInput): F[Long] =
    SQL
      .insert(model)
      .withUniqueGeneratedKeys[Long]("id")
      .transact(xa)

  override def update(id: Long, model: TransportPositionInput): F[Int] =
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

object TransportPositionRepository {

  object SQL {

    import doobie.postgres.pgisimplicits._

    def findAll: Query0[TransportPositionOutput] =
      sql"""SELECT id, transport_id,
           | ST_AsText(coordinate) AS coordinate,
           | record_date FROM transport_position""".stripMargin.query[TransportPositionOutput]

    def find(id: Long): Query0[TransportPositionOutput] =
      sql"""SELECT id, transport_id,
           | ST_AsText(coordinate) AS coordinate,
           | record_date FROM transport_position WHERE id = $id""".stripMargin
        .query[TransportPositionOutput]

    def insert(model: TransportPositionInput): Update0 =
      sql"""INSERT INTO transport_position(transport_id, coordinate, record_date)
           |VALUES(${model.transportID},
           |       ST_SetSRID(ST_MakePoint(${model.coordinate.longitude}, ${model.coordinate.latitude}), 4326),
           |       ${model.recordDate})
           |""".stripMargin.update

    def update(id: Long, model: TransportPositionInput): Update0 =
      sql"""UPDATE transport_position
           |SET transport_id = ${model.transportID},
           |    coordinate = ST_SetSRID(ST_MakePoint(${model.coordinate.longitude}, ${model.coordinate.latitude}), 4326),
           |    record_date = ${model.recordDate}
           |WHERE id = $id
           |""".stripMargin.update

    def delete(id: Long): Update0 =
      sql"""DELETE FROM transport_position WHERE id = $id""".update

  }

}
