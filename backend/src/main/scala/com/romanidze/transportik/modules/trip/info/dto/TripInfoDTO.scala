package com.romanidze.transportik.modules.trip.info.dto

object TripInfoDTO {

  case class TripInfoEntity(
    tripID: Long,
    transportID: Long,
    cost: Long
  )

}
