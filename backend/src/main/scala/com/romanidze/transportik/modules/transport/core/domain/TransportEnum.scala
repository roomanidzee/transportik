package com.romanidze.transportik.modules.transport.core.domain

import enumeratum.values.{IntEnum, IntEnumEntry}

import scala.collection.immutable

sealed abstract class TransportType(val value: Int, val name: String) extends IntEnumEntry

case object TransportItem extends IntEnum[TransportType]{

   case object Awning extends TransportType(value = 1, name = "AWNING")
   case object Refrigerator extends TransportType(value = 2, name = "REFRIGERATOR")
   case object Isotherm extends TransportType(value = 3, name = "ISOTHERM")

   val values: immutable.IndexedSeq[TransportType] = findValues

}


