package com.romanidze.transportik.modules.profile.mappers

import com.romanidze.transportik.modules.profile.domain.ProfileDomain.Profile
import com.romanidze.transportik.modules.profile.dto.ProfileDTO.{ ProfileInfo, ProfileOutput }

object ProfileMapper {

  def domainToDTO(profile: Profile) =
    ProfileOutput(profile.surname, profile.name, profile.patronymic, profile.phone)

  def dtoToDomain(profile: ProfileInfo) =
    Profile(0, profile.userID, profile.surname, profile.name, profile.patronymic, profile.phone)

}
