package com.romanidze.transportik.modules.trip.info.mappers

import com.romanidze.transportik.modules.trip.info.domain.TripInfoDomain.TripInfo
import com.romanidze.transportik.modules.trip.info.dto.TripInfoDTO.TripInfoEntity

object TripInfoMapper {

  def domainToDTO(trip: TripInfo) =
    TripInfoEntity(trip.tripID, trip.transportID, trip.cost)

  def dtoToDomain(trip: TripInfoEntity) =
    TripInfo(0, trip.tripID, trip.transportID, trip.cost)

}
