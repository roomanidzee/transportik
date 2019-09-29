package com.romanidze.transportik.modules.trip.profile.domain

object TripProfileDomain {

  case class TripProfile(
    id: Long,
    tripID: Long,
    profileID: Long
  )

}
