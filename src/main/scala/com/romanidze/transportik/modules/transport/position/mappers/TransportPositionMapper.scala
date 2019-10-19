package com.romanidze.transportik.modules.transport.position.mappers

import java.sql.Timestamp
import java.time.ZoneId

import com.romanidze.transportik.components.db.PointEntity
import com.romanidze.transportik.modules.transport.position.domain.TransportPositionDomain.{ TransportPositionInput, TransportPositionOutput }
import com.romanidze.transportik.modules.transport.position.dto.TransportPositionDTO.TransportPositionEntity

object TransportPositionMapper {

  def domainToDTO(position: TransportPositionOutput) =
    TransportPositionEntity(
      position.transportID,
      PointEntity(
        position.coordinate.x,
        position.coordinate.y
      ),
      position.recordDate.toLocalDateTime.atZone(ZoneId.systemDefault())
    )

  def dtoToDomain(position: TransportPositionEntity) =
    TransportPositionInput(
      0,
      position.transportID,
      PointEntity(
        position.coordinate.latitude,
        position.coordinate.longitude
      ),
      Timestamp.valueOf(position.recordDate.toLocalDateTime)
    )

}
