package swaggerblocks.rendering.circe

import swaggerblocks.internal.specModels.SpecExample
import io.circe._
import io.circe.parser.parse

object formats {

  implicit val encodeSpecExample = new Encoder[SpecExample] {
    def apply(se: SpecExample): Json = {
      // Since we control the serialization this should be always valid
      parse(se.value).right.get
    }
  }

}
