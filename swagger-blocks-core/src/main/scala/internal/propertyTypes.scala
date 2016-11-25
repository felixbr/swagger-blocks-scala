package internal

import internal.propertyFormats.{IntegerFormat, NumberFormat, StringFormat}

object propertyTypes {

  sealed trait PropertyType
  case class String(format: StringFormat) extends PropertyType
  case class Int(format: IntegerFormat)   extends PropertyType
  case class Number(format: NumberFormat) extends PropertyType
  case object Boolean                     extends PropertyType
  case object File                        extends PropertyType

}
