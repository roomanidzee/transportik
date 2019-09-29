package com.romanidze.transportik.modules.transport.core.services

import cats.effect.{Async, ContextShift}
import cats.syntax.functor._
import com.romanidze.transportik.modules.transport.core.dto.TransportDTO.TransportInfoEntity
import com.romanidze.transportik.modules.transport.core.mappers.TransportMapper
import com.romanidze.transportik.modules.transport.core.repositories.TransportRepository

trait TransportServiceTrait[F[_]]{

  def getTransport(id: Long): F[TransportInfoEntity]
  def saveTransport(transport: TransportInfoEntity): F[TransportInfoEntity]

}

final class TransportService[F[_]: Async: ContextShift](repository: TransportRepository[F])
   extends TransportServiceTrait[F]{

  override def getTransport(id: Long): F[TransportInfoEntity] = {

    val result = for {
       dbResult <- repository.find(id)
       mappedResult = dbResult.map(transport => TransportMapper.domainToDTO(transport))
    } yield mappedResult.get

    result

  }

  override def saveTransport(transport: TransportInfoEntity): F[TransportInfoEntity] = {

    val result = for {
       _ <- repository.insert(TransportMapper.dtoToDomain(transport))
    } yield transport

    result

  }
}
