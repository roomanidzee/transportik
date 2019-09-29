package com.romanidze.transportik.modules.trip.info.domain

object TripInfoDomain {

  case class TripInfo(
    id: Long,
    tripID: Long,
    transportID: Long,
    cost: Long
  )

}
