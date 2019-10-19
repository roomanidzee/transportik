package com.romanidze.transportik.modules.transport.core.domain

import java.sql.Timestamp

object TransportDomain {

  case class TransportInfo(
    id: Long,
    name: String,
    length: Long,
    tonnage: Long,
    createdTime: Timestamp,
    transportType: Int
  )

}
