package swaggerblocks.rendering.yaml

import io.circe.yaml.parser.parse
import io.circe.{Encoder, Json}
import swaggerblocks.internal.specModels._
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
