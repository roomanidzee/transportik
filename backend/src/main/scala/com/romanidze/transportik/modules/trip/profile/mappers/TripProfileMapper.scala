package com.romanidze.transportik.modules.trip.profile.mappers

import com.romanidze.transportik.modules.trip.profile.domain.TripProfileDomain.TripProfile
import com.romanidze.transportik.modules.trip.profile.dto.TripProfileDTO.TripProfileEntity

object TripProfileMapper {

  def domainToDTO(tripProfile: TripProfile) =
    TripProfileEntity(tripProfile.tripID, tripProfile.profileID)

  def dtoToDomain(tripProfile: TripProfileEntity) =
    TripProfile(0, tripProfile.tripID, tripProfile.profileID)

}
