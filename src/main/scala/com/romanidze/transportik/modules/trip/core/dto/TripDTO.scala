package com.romanidze.transportik.modules.trip.core.dto

import com.romanidze.transportik.components.db.PointEntity

object TripDTO {

  case class TripEntity(
    source: PointEntity,
    target: PointEntity
  )

  case class Results(
    result: List[TripEntity]
  )

}
