package swaggerblocks.rendering.playJson

import swaggerblocks.internal.models._
import swaggerblocks.internal.propertyTypes.PropertyType
import swaggerblocks.internal.specModels._
import swaggerblocks.internal.writeLogic
import play.api.libs.functional.syntax._
import play.api.libs.json._
import swaggerblocks._

object formats {
  implicit val apiContactWrites = Json.writes[ApiContact]

  implicit val apiLicenseWrites = Json.writes[ApiLicense]

  implicit val apiExternalDocsWrites = Json.writes[ApiExternalDocs]

  implicit val apiInfoWrites = Json.writes[ApiInfo]

  implicit val apiRootWrites = Json.writes[ApiRoot]

  implicit val apiParameterInWrites = new Writes[ParameterIn] {
    def writes(obj: ParameterIn): JsValue =
      JsString(writeLogic.parameterInToString(obj))
  }

  implicit val apiPropertyTypeWrites = new Writes[PropertyType] {
    def writes(obj: PropertyType): JsValue =
      JsString(writeLogic.propertyTypeToString(obj))
  }

  implicit lazy val optionSpecSchemaWrites = Writes.OptionWrites[SpecSchema]

  implicit lazy val specSchemaMapWrites = Writes.map[SpecSchema]

  implicit lazy val specSchemaWrites: Writes[SpecSchema] = (
    (__ \ "$ref").writeNullable[String] and
      (__ \ "type").writeNullable[String] and
      (__ \ "format").writeNullable[String] and
      (__ \ "title").writeNullable[String] and
      (__ \ "description").writeNullable[String] and
      (__ \ "example").writeNullable[SpecExample](exampleWrites) and
      (__ \ "required").writeNullable[List[String]] and
      (__ \ "enum").writeNullable[List[String]] and
      (__ \ "items").lazyWriteNullable[SpecSchema](specSchemaWrites) and
      (__ \ "properties").lazyWriteNullable[Map[SpecSchemaName, SpecSchema]](specSchemaMapWrites)
  )(unlift(SpecSchema.unapply))

  implicit val specResponseHeaderWrites = Json.writes[SpecResponseHeader]

  implicit val specResponseWrites = Json.writes[SpecResponse]

  implicit val specParameterWrites = Json.writes[SpecParameter]

  implicit val specOperationWrites = Json.writes[SpecOperation]

  implicit val specPathMapWrites: Writes[Map[SpecPath, Map[SpecMethod, SpecOperation]]] =
    Writes.map(Writes.map[SpecOperation])

  implicit val specWrites = Json.writes[Spec]

  lazy val exampleWrites = new Writes[SpecExample] {
    def writes(exampleJson: SpecExample): JsValue =
      Json.parse(exampleJson.value)
  }

}
