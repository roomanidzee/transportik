package com.romanidze.transportik.modules.transport.analysis.domain

import java.time.ZonedDateTime

case class TransportAnalysisDomain(
  recordTime: ZonedDateTime,
  transportID: Long,
  isBusy: Boolean,
  isRepairing: Boolean,
  tripID: Long,
  distance: Long,
  cost: Long
)
