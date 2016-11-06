package internal

import swaggerblocks._
import swaggerblocks.s.PropertyType

object models {

  case class ApiRoot(
    swagger: String,
    host: Option[String],
    basePath: Option[String],
    info: ApiInfo,
    externalDocs: Option[ApiExternalDocs]
  )

  case class ApiInfo(
    version: String,
    title: Option[String],
    description: Option[String],
    termsOfService: Option[String],
    contact: Option[ApiContact],
    license: Option[ApiLicense]
  )

  case class ApiContact(
    name: Option[String],
    url: Option[String],
    email: Option[String]
  )

  case class ApiLicense(
    name: Option[String],
    url: Option[String]
  )

  case class ApiExternalDocs(
    url: String,
    description: Option[String]
  )

  case class ApiPath(
    operations: Map[Method, ApiOperation]
  )

  case class ApiPathDefinition(
    path: String,
    metadata: ApiPath
  )

  case class ApiOperation(
    description: Option[String],
    summary: Option[String],
    tags: List[String],
    parameters: List[ApiParameter],
    responses: Map[String, ApiResponse]
  )

  case class ApiOperationDefinition(
    method: Method,
    operation: ApiOperation
  )

  case class ApiParameter(
    name: String,
    in: ParameterIn,
    required: Boolean,
    typ: PropertyType,
    description: Option[String],
    schema: Option[ApiSchemaRef]
  )

  case class ApiResponse(
    description: Option[String],
    schema: Option[ApiSchemaRef]
  )

  case class ApiResponseDefinition(
    status: String,
    response: ApiResponse
  )

  case class ApiSchemaDefinition(name: String, schema: ApiSchema)

  sealed trait ApiSchema

  case class ApiObjectSchema(
    properties: List[ApiPropertyDefinition]
  ) extends ApiSchema

  case class ApiValueSchema(
    typ: PropertyType
  ) extends ApiSchema

  sealed trait ApiSchemaRef
  case class SingleRef(schema: ApiSchemaDefinition) extends ApiSchemaRef
  case class MultipleRef(schema: ApiSchemaDefinition) extends ApiSchemaRef
  case class InlineSchema(schema: ApiSchema) extends ApiSchemaRef
  case class MultipleInlineSchema(schema: ApiSchema) extends ApiSchemaRef

  case class ApiProperty(
    typ: PropertyType,
    description: Option[String]
  )

  case class ApiPropertyDefinition(
    name: String,
    prop: ApiProperty,
    required: Boolean
  )

  object ApiSpec {
    def fromSeqs(root: ApiRoot, paths: Seq[ApiPathDefinition], schemata: Seq[ApiSchemaDefinition]): ApiSpec = {
      ApiSpec(
        root,
        paths.map(p => (p.path, p.metadata)).toMap,
        schemata.map(s => (s.name, s.schema)).toMap
      )
    }
  }

  case class ApiSpec(
    root: ApiRoot,
    paths: Map[String, ApiPath],
    definitions: Map[String, ApiSchema]
  )
}
