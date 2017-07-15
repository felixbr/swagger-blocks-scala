package swaggerblocks.internal

object propertyValues {

  sealed trait PropertyValue
  object PropertyValue {
    case class StringValue(value: String)                  extends PropertyValue
    case class IntValue(value: Int)                        extends PropertyValue
    case class BooleanValue(value: Boolean)                extends PropertyValue
    case class ListValue(value: List[PropertyValue])       extends PropertyValue
    case class MapValue(value: Map[String, PropertyValue]) extends PropertyValue
    case class NullValue()                                 extends PropertyValue
  }

  trait SwaggerType[T] {
    def valueOf(v: T): PropertyValue
  }

  object SwaggerType {

    implicit val unitType = new SwaggerType[PropertyValue.NullValue] {
      def valueOf(v: PropertyValue.NullValue) = PropertyValue.NullValue()
    }

    implicit val intType = new SwaggerType[Int] {
      def valueOf(v: Int) = PropertyValue.IntValue(v)
    }

    implicit val stringType = new SwaggerType[String] {
      def valueOf(v: String) = PropertyValue.StringValue(v)
    }

    implicit val booleanType = new SwaggerType[Boolean] {
      def valueOf(v: Boolean) = PropertyValue.BooleanValue(v)
    }

    implicit def listType[A: SwaggerType] = new SwaggerType[List[A]] {
      def valueOf(value: List[A]): PropertyValue = {
        PropertyValue.ListValue(
          value.map(implicitly[SwaggerType[A]].valueOf(_))
        )
      }
    }

    implicit def mapType[V: SwaggerType] = new SwaggerType[Map[String, V]] {
      def valueOf(value: Map[String, V]): PropertyValue = {
        PropertyValue.MapValue(
          value.map {
            case (k, v) =>
              (k, implicitly[SwaggerType[V]].valueOf(v))
          }
        )
      }
    }

  }

}
