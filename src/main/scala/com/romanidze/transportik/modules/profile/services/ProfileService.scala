package com.romanidze.transportik.modules.profile.services

import cats.effect.{ Async, ContextShift }
import cats.syntax.functor._
import com.romanidze.transportik.modules.profile.dto.ProfileDTO.{ ProfileInfo, ProfileOutput }
import com.romanidze.transportik.modules.profile.mappers.ProfileMapper
import com.romanidze.transportik.modules.profile.repositories.ProfileRepository

trait ProfileServiceTrait[F[_]] {

  def getProfile(userID: Long): F[ProfileOutput]
  def saveProfile(profile: ProfileInfo): F[ProfileOutput]

}

final class ProfileService[F[_]: Async: ContextShift](repository: ProfileRepository[F])
    extends ProfileServiceTrait[F] {
  override def getProfile(userID: Long): F[ProfileOutput] = {

    val result = for {
      dbResult     <- repository.findProfileByUserID(userID)
      mappedResult = dbResult.map(profile => ProfileMapper.domainToDTO(profile))
    } yield mappedResult.get

    result

  }

  override def saveProfile(profile: ProfileInfo): F[ProfileOutput] = {

    val result = for {
      _            <- repository.insert(ProfileMapper.dtoToDomain(profile))
      domain       = ProfileMapper.dtoToDomain(profile)
      mappedResult = ProfileMapper.domainToDTO(domain)
    } yield mappedResult

    result

  }
}
