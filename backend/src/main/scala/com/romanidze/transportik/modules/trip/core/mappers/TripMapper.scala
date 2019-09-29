package com.romanidze.transportik.modules.trip.core.mappers

import com.romanidze.transportik.components.db.PointEntity
import com.romanidze.transportik.modules.trip.core.domain.TripDomain.{TripDBInput, TripDBOutput}
import com.romanidze.transportik.modules.trip.core.dto.TripDTO.TripEntity

object TripMapper {

  def domainToDTO(trip: TripDBOutput): TripEntity =
    TripEntity(
      PointEntity(
        trip.source.x,
        trip.source.y
      ),
      PointEntity(
        trip.target.x,
        trip.target.y
      )
    )

  def dtoToDomain(trip: TripEntity): TripDBInput =
    TripDBInput(
      0,
      trip.source,
      trip.target
    )

}
