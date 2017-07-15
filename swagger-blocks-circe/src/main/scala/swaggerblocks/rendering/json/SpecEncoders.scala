package swaggerblocks.rendering.json

import swaggerblocks.internal.specModels.{SpecExample, SpecValue}
import io.circe._
import io.circe.parser.parse
import cats.syntax.either._

trait SpecEncoders {

  implicit val encodeSpecExample = new Encoder[SpecExample] {
    def apply(se: SpecExample): Json = {
      // Since we control the serialization this should be always valid
      parse(se.value).valueOr(throw _)
    }
  }

  implicit val encodeSpecValue = new Encoder[SpecValue] {
    def apply(sv: SpecValue): Json = sv match {
      case SpecValue.StringValue(v)    => Json.fromString(v)
      case SpecValue.IntValue(v)       => Json.fromInt(v)
      case SpecValue.BooleanValue(v)   => Json.fromBoolean(v)
      case SpecValue.ListValue(list)   => Json.fromValues(list.map(apply))
      case SpecValue.MapValue(mapping) => Json.fromFields(mapping.mapValues(apply))
    }
  }
}
