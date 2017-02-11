package swaggerblocks.internal

object propertyFormats {

  sealed trait IntegerFormat
  case object Int32 extends IntegerFormat
  case object Int64 extends IntegerFormat

  sealed trait StringFormat
  case object Text     extends StringFormat
  case object Byte     extends StringFormat
  case object Binary   extends StringFormat
  case object Date     extends StringFormat
  case object DateTime extends StringFormat
  case object Password extends StringFormat

  sealed trait NumberFormat
  case object Float extends NumberFormat
  case object Double extends NumberFormat

}
