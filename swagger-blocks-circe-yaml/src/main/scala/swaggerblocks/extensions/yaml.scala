package swaggerblocks.extensions

import io.circe.yaml._
import io.circe.Encoder
import io.circe.syntax._
import swaggerblocks.internal.models.{ApiObjectSchema, ApiSchemaDefinition}

object yaml {
  implicit class ExampleExtension(schemaDef: ApiSchemaDefinition) {
    private val printer = Printer.spaces2.copy(
      preserveOrder = true,
      dropNullKeys = true
    )

    def withExample[T](obj: T)(implicit encoder: Encoder[T]): ApiSchemaDefinition =
      schemaDef.schema match {
        case s: ApiObjectSchema =>
          schemaDef.copy(
            schema = s.copy(
              example = Some(printer.pretty(obj.asJson))
            )
          )

        case _ =>
          schemaDef
      }
  }
}
