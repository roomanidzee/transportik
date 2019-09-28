package com.romanidze.transportik.modules.user.dto

case class UserDTO(
  username: String,
  password: String
)

case class Results(
  result: List[UserDTO]
)
