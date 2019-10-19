package com.romanidze.transportik.modules.trip.core.domain

import com.romanidze.transportik.components.db.PointEntity
import org.postgis.Point

object TripDomain {

  case class TripDBInput(
    id: Long,
    source: PointEntity,
    target: PointEntity
  )

  case class TripDBOutput(
    id: Long,
    source: Point,
    target: Point
  )

}
