package com.romanidze.transportik.modules.transport.core.mappers

import java.sql.Timestamp
import java.time.ZoneId

import com.romanidze.transportik.modules.transport.core.domain.TransportDomain.TransportInfo
import com.romanidze.transportik.modules.transport.core.dto.TransportDTO.TransportInfoEntity

object TransportMapper {

  def domainToDTO(transport: TransportInfo) =
    TransportInfoEntity(
      transport.name,
      transport.length,
      transport.tonnage,
      transport.createdTime.toLocalDateTime.atZone(ZoneId.systemDefault()),
      transport.transportType
    )

  def dtoToDomain(transport: TransportInfoEntity) =
    TransportInfo(
      0,
      transport.name,
      transport.length,
      transport.tonnage,
      Timestamp.valueOf(transport.createdTime.toLocalDateTime),
      transport.transportType
    )

}
