package com.romanidze.transportik.modules.transport.analysis.repositories

import java.time.{ Instant, ZoneId }

import cats.effect.{ Async, ContextShift, Resource }
import cats.syntax.functor._
import com.evolutiongaming.scassandra.CassandraSession
import com.evolutiongaming.scassandra.syntax._
import com.romanidze.transportik.modules.transport.analysis.domain.TransportAnalysisDomain

import scala.collection.mutable.ListBuffer

class TransportCassandraRepository[F[_]: Async: ContextShift](
  session: Resource[F, CassandraSession[F]]
) extends TransportAnalysisRepository[F] {

  override def findAll(): F[List[TransportAnalysisDomain]] = {

    for {

      resultSet <- session.use { session =>
                    session.execute("SELECT * FROM transport_analysis")
                  }

    } yield {

      val result = new ListBuffer[TransportAnalysisDomain]()

      val rows = resultSet.all()

      rows.forEach(row => {
        result += TransportAnalysisDomain(
              recordTime = row.decode[Instant]("record_time").atZone(ZoneId.systemDefault()),
              transportID = row.decode[Long]("transport_id"),
              isBusy = row.decode[Boolean]("is_busy"),
              isRepairing = row.decode[Boolean]("is_repairing"),
              tripID = row.decode[Long]("trip_id"),
              distance = row.decode[Long]("distance"),
              cost = row.decode[Long]("cost")
            )

      })

      result.toList

    }

  }

  override def insert(model: TransportAnalysisDomain): F[Long] = {

    val query =
      """
        |INSERT INTO
        |default.transport_analysis (record_time, transport_id, is_busy, is_repairing, trip_id, distance, cost)
        | VALUES (?, ?, ?, ?, ?, ?, ?)
        |""".stripMargin

    for {

      _ <- session.use { session =>
            for {

              prepared <- session.prepare(query)

              bound = prepared
                .bind()
                .encode("record_time", model.recordTime.toInstant)
                .encode("transport_id", model.transportID)
                .encode("is_busy", model.isBusy)
                .encode("is_repairing", model.isRepairing)
                .encode("trip_id", model.tripID)
                .encode("distance", model.distance)
                .encode("cost", model.cost)

            } yield session.execute(bound)

          }

    } yield 1

  }

}
