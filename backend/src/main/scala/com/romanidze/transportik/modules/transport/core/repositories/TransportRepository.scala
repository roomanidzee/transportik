package com.romanidze.transportik.modules.transport.core.repositories

import cats.effect.{ Async, ContextShift }
import com.romanidze.transportik.modules.transport.core.domain.TransportDomain.TransportInfo
import doobie._
import doobie.implicits._

trait TransportRepositoryTrait[F[_]] {

  def findAll(): F[List[TransportInfo]]
  def find(id: Long): F[Option[TransportInfo]]
  def insert(model: TransportInfo): F[Long]
  def update(id: Long, model: TransportInfo): F[Int]
  def delete(id: Long): F[Int]

}

class TransportRepository[F[_]: Async: ContextShift](xa: Transactor[F])
    extends TransportRepositoryTrait[F] {

  import TransportRepository.SQL

  override def findAll(): F[List[TransportInfo]] =
    SQL.findAll
      .to[List]
      .transact(xa)

  override def find(id: Long): F[Option[TransportInfo]] =
    SQL
      .find(id)
      .option
      .transact(xa)

  override def insert(model: TransportInfo): F[Long] =
    SQL
      .insert(model)
      .withUniqueGeneratedKeys[Long]("id")
      .transact(xa)

  override def update(id: Long, model: TransportInfo): F[Int] =
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

object TransportRepository {

  object SQL {

    def findAll: Query0[TransportInfo] =
      sql"""SELECT * FROM transport_info""".query[TransportInfo]

    def find(id: Long): Query0[TransportInfo] =
      sql"""SELECT * FROM transport_info WHERE id = $id""".query[TransportInfo]

    def insert(model: TransportInfo): Update0 =
      sql"""INSERT INTO transport_info(name, length, tonnage, created_time, type)
           |VALUES(${model.name}, ${model.length}, ${model.tonnage}, ${model.createdTime}, ${model.transportType})
           |""".stripMargin.update

    def update(id: Long, model: TransportInfo): Update0 =
      sql"""UPDATE transport_info
           |SET name = ${model.name},
           |    length = ${model.length},
           |    tonnage = ${model.tonnage},
           |    created_time = ${model.createdTime},
           |    type = ${model.transportType}
           |WHERE id = $id
           |""".stripMargin.update

    def delete(id: Long): Update0 =
      sql"""DELETE FROM transport_info WHERE id = $id""".update

  }

}
