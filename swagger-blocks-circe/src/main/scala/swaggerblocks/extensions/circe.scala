package swaggerblocks.extensions

import swaggerblocks.internal.models.{ApiObjectSchema, ApiSchemaDefinition}
import io.circe._
import io.circe.syntax._

object circe {
  implicit class ExampleExtension(schemaDef: ApiSchemaDefinition) {
    private val printer = Printer.spaces2.copy(
      preserveOrder = true,
      dropNullKeys = true,
      colonLeft = ""
    )

    def withExample[T](obj: T)(implicit encoder: Encoder[T]): ApiSchemaDefinition = schemaDef.schema match {
      case s: ApiObjectSchema =>
        schemaDef.copy(
          schema = s.copy(
            example = Some(obj.asJson.pretty(printer))
          )
        )

      case _ =>
        schemaDef
    }
  }
}
