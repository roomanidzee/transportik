package com.romanidze.transportik.modules.trip.profile.services

import cats.effect.{ Async, ContextShift }
import cats.syntax.functor._
import com.romanidze.transportik.modules.trip.profile.dto.TripProfileDTO.TripProfileEntity
import com.romanidze.transportik.modules.trip.profile.mappers.TripProfileMapper
import com.romanidze.transportik.modules.trip.profile.repositories.TripProfileRepository

trait TripProfileServiceTrait[F[_]] {

  def getTripProfile(id: Int): F[TripProfileEntity]
  def saveTripProfile(tripProfile: TripProfileEntity): F[TripProfileEntity]

}

class TripProfileService[F[_]: Async: ContextShift](repository: TripProfileRepository[F])
    extends TripProfileServiceTrait[F] {

  override def getTripProfile(id: Int): F[TripProfileEntity] = {

    val result = for {

      dbResult     <- repository.find(id)
      mappedResult = dbResult.map(tripProfile => TripProfileMapper.domainToDTO(tripProfile))

    } yield mappedResult.get

    result

  }

  override def saveTripProfile(tripProfile: TripProfileEntity): F[TripProfileEntity] = {

    val result = for {
      _ <- repository.insert(TripProfileMapper.dtoToDomain(tripProfile))
    } yield tripProfile

    result

  }
}
