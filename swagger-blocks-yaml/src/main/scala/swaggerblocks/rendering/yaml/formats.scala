package swaggerblocks.rendering.yaml

import internal.models._
import net.jcazevedo.moultingyaml._
import swaggerblocks._
import swaggerblocks.rendering.writeLogic
import swaggerblocks.s._

//noinspection NotImplementedCode
object formats extends DefaultYamlProtocol {
  implicit val apiContact = yamlFormat3(ApiContact)

  implicit val apiLicense = yamlFormat2(ApiLicense)

  implicit val apiExternalDocs = yamlFormat2(ApiExternalDocs)

  implicit val apiInfo = yamlFormat6(ApiInfo)

  implicit val apiRoot = yamlFormat5(ApiRoot)

  implicit val apiParameterIn = new YamlFormat[ParameterIn] {
    def write(obj: ParameterIn): YamlValue = YamlString(writeLogic.parameterInToString(obj))

    def read(yaml: YamlValue): ParameterIn = ???
  }

  // use format instead of writer to enable usage of 'yamlFormat2()' helper for ApiProperty
  implicit val apiPropertyType = new YamlFormat[PropertyType] {
    def write(obj: PropertyType): YamlValue = YamlString(writeLogic.propertyTypeToString(obj))

    def read(yaml: YamlValue): PropertyType = ???
  }

  implicit val apiProperty = new YamlFormat[ApiProperty] {
    def write(obj: ApiProperty): YamlValue = {
      val yaml = obj.toYaml(yamlFormat2(ApiProperty)).asYamlObject

      val fieldsWithReplacedType = (yaml.fields - YamlString("typ")) + (YamlString("type") -> obj.typ.toYaml)
      yaml.copy(fields = fieldsWithReplacedType)
    }

    def read(yaml: YamlValue): ApiProperty = ???
  }

  implicit val apiSchema = new YamlFormat[ApiSchema] {
    def write(obj: ApiSchema): YamlValue = obj match {
      case ApiObjectSchema(propdefs) =>
        val requiredAttributes: List[String] =
          propdefs.collect {
            case ApiPropertyDefinition(name, p, true) => name
          }

        val properties = propdefs.map { p =>
          (p.name, p.prop)
        }.toMap[String, ApiProperty]

        Map(
          "required" -> requiredAttributes.toYaml,
          "properties" -> properties.toYaml
        ).toYaml

      case ApiValueSchema(typ) =>
        Map(
          "type" -> typ
        ).toYaml
    }

    def read(yaml: YamlValue): ApiSchema = ???
  }

  implicit val apiSchemaDefinition = yamlFormat2(ApiSchemaDefinition)

  implicit val apiSchemaRef = new YamlFormat[ApiSchemaRef] {
    def write(obj: ApiSchemaRef): YamlValue = obj match {
      case SingleRef(ApiSchemaDefinition(name, schema)) =>
        Map(
          writeLogic.referenceTo(name)
        ).toYaml

      case MultipleRef(ApiSchemaDefinition(name, schema)) =>
        Map(
          "type" -> "array".toYaml,
          "items" -> Map(
            writeLogic.referenceTo(name)
          ).toYaml
        ).toYaml

      case InlineSchema(schema) =>
        schema.toYaml

      case MultipleInlineSchema(schema) =>
        Map(
          "type" -> "array".toYaml,
          "items" -> schema.toYaml
        ).toYaml
    }

    def read(yaml: YamlValue): ApiSchemaRef = ???
  }

  implicit val apiMethod = new YamlWriter[Method] {
    def write(obj: Method): YamlValue = writeLogic.methodToString(obj).toYaml
  }

  implicit val apiParameter = new YamlFormat[ApiParameter] {
    def write(obj: ApiParameter): YamlValue = {
      val yaml = obj.toYaml(yamlFormat6(ApiParameter)).asYamlObject

      val fieldsWithReplacedType = (yaml.fields - YamlString("typ")) + (YamlString("type") -> obj.typ.toYaml)
      yaml.copy(fields = fieldsWithReplacedType)
    }

    def read(yaml: YamlValue): ApiParameter = ???
  }

  implicit val apiResponse = yamlFormat2(ApiResponse)

  implicit val apiOperation = yamlFormat5(ApiOperation)

  implicit val apiOperationMap = new YamlWriter[Map[Method, ApiOperation]] {
    def write(m: Map[Method, ApiOperation]): YamlValue = {
      m.map { case (method, op) =>
        (writeLogic.methodToString(method), op)
      }.toYaml
    }
  }

  implicit val apiPath = new YamlFormat[ApiPath] {
    def write(obj: ApiPath): YamlValue = obj.operations.toYaml

    def read(yaml: YamlValue): ApiPath = ???
  }

  implicit val apiSpec = new YamlWriter[ApiSpec] {
    def write(spec: ApiSpec): YamlValue = {
      val rootFields = spec.root.toYaml.asYamlObject.fields
      val restFields = Map(
        "paths".toYaml -> spec.paths.toYaml,
        "definitions".toYaml -> spec.definitions.toYaml
      )

      (rootFields ++ restFields).toYaml
    }
  }
}
