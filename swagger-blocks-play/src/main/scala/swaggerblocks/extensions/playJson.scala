package swaggerblocks.extensions

import swaggerblocks.internal.models.{ApiObjectSchema, ApiSchemaDefinition}
import play.api.libs.json.{JsValue, Json, Writes}

object playJson {
  implicit class ExampleExtension(schemaDef: ApiSchemaDefinition) {
    def withExample(jsValue: JsValue): ApiSchemaDefinition = schemaDef.schema match {
      case s: ApiObjectSchema =>
        schemaDef.copy(
          schema = s.copy(
            example = Some(Json.stringify(jsValue))
          )
        )

      case _ =>
        schemaDef // nothing to do here
    }

    def withExample[T](obj: T)(implicit ev: Writes[T]): ApiSchemaDefinition = {
      withExample(Json.toJson(obj))
    }
  }
}
