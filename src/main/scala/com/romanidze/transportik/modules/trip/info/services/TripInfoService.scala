package com.romanidze.transportik.modules.trip.info.services

import cats.effect.{ Async, ContextShift }
import cats.syntax.functor._
import com.romanidze.transportik.modules.trip.info.dto.TripInfoDTO.TripInfoEntity
import com.romanidze.transportik.modules.trip.info.mappers.TripInfoMapper
import com.romanidze.transportik.modules.trip.info.repositories.TripInfoRepository

trait TripInfoServiceTrait[F[_]] {

  def getTripInfo(id: Long): F[TripInfoEntity]
  def saveTripInfo(trip: TripInfoEntity): F[TripInfoEntity]

}

final class TripInfoService[F[_]: Async: ContextShift](repository: TripInfoRepository[F])
    extends TripInfoServiceTrait[F] {

  override def getTripInfo(id: Long): F[TripInfoEntity] = {

    val result = for {
      dbResult     <- repository.find(id)
      mappedResult = dbResult.map(info => TripInfoMapper.domainToDTO(info))
    } yield mappedResult.get

    result

  }

  override def saveTripInfo(trip: TripInfoEntity): F[TripInfoEntity] = {

    val result = for {
      _ <- repository.insert(TripInfoMapper.dtoToDomain(trip))
    } yield trip

    result

  }
}
