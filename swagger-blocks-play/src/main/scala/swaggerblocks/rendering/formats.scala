package swaggerblocks.rendering

import play.api.libs.json._
import internal.models._
import swaggerblocks._
import swaggerblocks.s.PropertyType

object formats {
  implicit val apiContactWrites = Json.writes[ApiContact]

  implicit val apiLicenseWrites = Json.writes[ApiLicense]

  implicit val apiExternalDocs = Json.writes[ApiExternalDocs]

  implicit val apiInfoWrites = Json.writes[ApiInfo]

  implicit val apiRootWrites = Json.writes[ApiRoot]

  implicit val apiParameterInWrites = new Writes[ParameterIn] {
    def writes(obj: ParameterIn): JsValue = JsString(obj match {
      case Query    => "query"
      case Header   => "header"
      case Path     => "path"
      case FormData => "formData"
      case Body     => "body"
    })
  }

  implicit val apiPropertyTypeWrites = new Writes[PropertyType] {
    def writes(obj: PropertyType): JsValue = JsString(obj match {
      case s.String => "string"
      case s.Number => "number"
      case s.Int    => "integer"
    })
  }

  implicit val apiPropertyWrites = new Writes[ApiProperty] {
    def writes(obj: ApiProperty): JsValue = {
      val json = Json.toJson(obj)(Json.writes[ApiProperty]).as[JsObject]

      (json - "typ") + ("type" -> Json.toJson(obj.typ))
    }
  }

  implicit val apiSchemaWrites = new Writes[ApiSchema] {
    def writes(obj: ApiSchema): JsValue = obj match {
      case ApiObjectSchema(propdefs) =>
        Json.obj(
          "required" -> propdefs.collect {
            case ApiPropertyDefinition(name, p, true) => JsString(name)
          },
          "properties" -> JsObject(
            propdefs.map { p =>
              (p.name, Json.toJson(p.prop))
            }
          )
        )

      case ApiValueSchema(typ) =>
        Json.obj(
          "type" -> Json.toJson(typ)
        )
    }
  }

  implicit val apiSchemaDefinitionWrites = Json.writes[ApiSchemaDefinition]

  implicit val apiSchemaRefWrites = new Writes[ApiSchemaRef] {
    def writes(obj: ApiSchemaRef): JsValue = obj match {
      case SingleRef(ApiSchemaDefinition(name, schema)) =>
        Json.obj(
          "$ref" -> JsString(s"#/definitions/$name")
        )

      case MultipleRef(ApiSchemaDefinition(name, schema)) =>
        Json.obj(
          "type" -> JsString("array"),
          "items" -> Json.obj(
            "$ref" -> JsString(s"#/definitions/$name")
          )
        )

      case InlineSchema(schema) =>
        Json.toJson(schema)

      case MultipleInlineSchema(schema) =>
        Json.obj(
          "type" -> JsString("array"),
          "items" -> Json.toJson(schema)
        )
    }
  }

  implicit val apiMethodWrites = new Writes[Method] {
    def writes(obj: Method): JsValue = JsString(asString(obj))

    def asString(obj: Method): String = obj match {
      case GET    => "get"
      case POST   => "post"
      case PUT    => "put"
      case PATCH  => "patch"
      case DELETE => "delete"
    }
  }

  implicit val apiParameterWrites = new Writes[ApiParameter] {
    def writes(p: ApiParameter): JsValue = {
      val json = Json.toJson(p)(Json.writes[ApiParameter]).as[JsObject]

      (json - "typ") + ("type" -> Json.toJson(p.typ))
    }
  }

  implicit val apiResponseWrites = Json.writes[ApiResponse]

  implicit val apiOperationWrites = Json.writes[ApiOperation]

  implicit val apiOperationMapWrites = new Writes[Map[Method, ApiOperation]] {
    def writes(m: Map[Method, ApiOperation]): JsValue = {
      JsObject(
        m.map { case (method, op) =>
          (apiMethodWrites.asString(method), Json.toJson(op))
        }
      )
    }
  }

  implicit val apiPathWrites = new Writes[ApiPath] {
    def writes(obj: ApiPath): JsValue = Json.toJson(obj.operations)
  }

  implicit val apiSpecWrites = new Writes[ApiSpec] {
    def writes(spec: ApiSpec): JsValue = {
      Json.toJson(spec.root).as[JsObject] ++ Json.obj(
        "paths" -> spec.paths,
        "definitions" -> spec.definitions
      )
    }
  }
}
