package com.romanidze.transportik.modules.transport.analysis.repositories

import com.romanidze.transportik.modules.transport.analysis.domain.TransportAnalysisDomain

trait TransportAnalysisRepository[F[_]] {
  def findAll(): F[List[TransportAnalysisDomain]]
  def insert(model: TransportAnalysisDomain): F[Long]
}
