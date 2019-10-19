package com.romanidze.transportik.modules.trip

import cats.effect.{ Async, ContextShift }
import cats.implicits._
import com.romanidze.transportik.modules.trip.core.TripCoreModule
import com.romanidze.transportik.modules.trip.info.TripInfoModule
import com.romanidze.transportik.modules.trip.profile.TripProfileModule
import doobie.util.transactor.Transactor
import org.http4s.HttpRoutes

class TripModule[F[_]: Async: ContextShift](transactor: Transactor[F]) {

  private val coreModule    = new TripCoreModule[F](transactor)
  private val profileModule = new TripProfileModule[F](transactor)
  private val infoModule    = new TripInfoModule[F](transactor)

  val routes: HttpRoutes[F] = coreModule.routes <+> profileModule.routes <+> infoModule.routes

}
