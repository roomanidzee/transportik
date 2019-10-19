package com.romanidze.transportik.modules.profile.dto

object ProfileDTO {

  case class ProfileInfo(
    userID: Long,
    surname: String,
    name: String,
    patronymic: String,
    phone: String
  )

  case class ProfileOutput(
    surname: String,
    name: String,
    patronymic: String,
    phone: String
  )

}
