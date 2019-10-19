package com.romanidze.transportik.modules.transport.analysis

import cats.effect.{ Async, ContextShift, Resource }
import com.evolutiongaming.scassandra.CassandraSession
import com.romanidze.transportik.modules.transport.analysis.http.TransportAnalysisRoutes
import com.romanidze.transportik.modules.transport.analysis.repositories.TransportCassandraRepository
import org.http4s.HttpRoutes

class TransportAnalysisModule[F[_]: Async: ContextShift](
  session: Resource[F, CassandraSession[F]]
) {

  val repository = new TransportCassandraRepository[F](session)

  val routes: HttpRoutes[F] = new TransportAnalysisRoutes[F]().routes

}
