package com.romanidze.transportik.modules.user.mappers

import com.romanidze.transportik.modules.user.domain.User
import com.romanidze.transportik.modules.user.dto.UserDTO

object UserMapper {

  def domainToDTO(user: User): UserDTO =
    UserDTO(username = user.username, password = user.password)

  def dtoToDomain(userDTO: UserDTO): User =
    User(id = 0, username = userDTO.username, password = userDTO.password)

}
