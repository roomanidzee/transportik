package com.romanidze.transportik.modules.trip.core.services

import cats.effect.{ Async, ContextShift }
import cats.syntax.functor._
import com.romanidze.transportik.modules.trip.core.dto.TripDTO.{ Results, TripEntity }
import com.romanidze.transportik.modules.trip.core.mappers.TripMapper
import com.romanidze.transportik.modules.trip.core.repositories.TripRepository

trait TripServiceTrait[F[_]] {

  def getTrips: F[Results]
  def saveTrip(trip: TripEntity): F[TripEntity]

}

final class TripService[F[_]: Async: ContextShift](repository: TripRepository[F])
    extends TripServiceTrait[F] {

  override def getTrips: F[Results] = {

    val result = for {

      dbResult     <- repository.findAll()
      mappedResult = dbResult.map(trip => TripMapper.domainToDTO(trip))

    } yield Results(mappedResult)

    result

  }

  override def saveTrip(trip: TripEntity): F[TripEntity] = {

    val result = for {
      _ <- repository.insert(TripMapper.dtoToDomain(trip))
    } yield trip

    result

  }
}
