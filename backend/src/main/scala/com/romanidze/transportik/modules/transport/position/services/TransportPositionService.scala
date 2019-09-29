package com.romanidze.transportik.modules.transport.position.services

import cats.effect.{ Async, ContextShift }
import cats.syntax.functor._
import com.romanidze.transportik.modules.transport.position.dto.TransportPositionDTO.{ Results, TransportPositionEntity }
import com.romanidze.transportik.modules.transport.position.mappers.TransportPositionMapper
import com.romanidze.transportik.modules.transport.position.repositories.TransportPositionRepository

trait TransportPositionServiceTrait[F[_]] {

  def getPositions: F[Results]
  def savePosition(position: TransportPositionEntity): F[TransportPositionEntity]

}

class TransportPositionService[F[_]: Async: ContextShift](
  repository: TransportPositionRepository[F]
) extends TransportPositionServiceTrait[F] {

  override def getPositions: F[Results] = {

    val result = for {

      dbResult     <- repository.findAll()
      mappedResult = dbResult.map(position => TransportPositionMapper.domainToDTO(position))

    } yield Results(mappedResult)

    result

  }

  override def savePosition(position: TransportPositionEntity): F[TransportPositionEntity] =
    for {
      _ <- repository.insert(TransportPositionMapper.dtoToDomain(position))
    } yield position

}
