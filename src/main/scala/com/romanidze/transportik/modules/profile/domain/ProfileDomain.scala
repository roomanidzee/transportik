package com.romanidze.transportik.modules.profile.domain

object ProfileDomain {

  case class Profile(
    id: Long,
    user: Long,
    surname: String,
    name: String,
    patronymic: String,
    phone: String
  )

}
