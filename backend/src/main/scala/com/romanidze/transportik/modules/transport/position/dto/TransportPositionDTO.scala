package com.romanidze.transportik.modules.transport.position.dto

import java.time.ZonedDateTime

import com.romanidze.transportik.components.db.PointEntity

object TransportPositionDTO {

  case class TransportPositionEntity(
    transportID: Long,
    coordinate: PointEntity,
    recordDate: ZonedDateTime
  )

  case class Results(
    result: List[TransportPositionEntity]
  )

}
