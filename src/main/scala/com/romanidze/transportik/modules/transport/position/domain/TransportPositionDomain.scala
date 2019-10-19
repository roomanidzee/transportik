package com.romanidze.transportik.modules.transport.position.domain

import java.sql.Timestamp

import com.romanidze.transportik.components.db.PointEntity
import org.postgis.Point

object TransportPositionDomain {

  case class TransportPositionInput(
    id: Long,
    transportID: Long,
    coordinate: PointEntity,
    recordDate: Timestamp
  )

  case class TransportPositionOutput(
    id: Long,
    transportID: Long,
    coordinate: Point,
    recordDate: Timestamp
  )

}
