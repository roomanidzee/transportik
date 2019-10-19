package com.romanidze.transportik.modules.transport.core.dto

import java.time.ZonedDateTime

object TransportDTO {

  case class TransportInfoEntity(
    name: String,
    length: Long,
    tonnage: Long,
    createdTime: ZonedDateTime,
    transportType: Int
  )

}
