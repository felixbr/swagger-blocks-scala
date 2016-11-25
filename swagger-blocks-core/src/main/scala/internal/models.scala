package internal

import swaggerblocks._
import propertyTypes.PropertyType

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
    title: String,
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
    name: String,
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

  sealed trait ApiParameter {
    def name: String
    def in: ParameterIn
    def required: Boolean
    def description: Option[String]
  }
  case class ApiBodyParameter(
    name: String,
    required: Boolean,
    schema: ApiSchemaRef,
    description: Option[String],
    enum: List[String]
  ) extends ApiParameter {
    val in = Body
  }
  case class ApiOtherParameter(
    name: String,
    in: ParameterIn,
    required: Boolean,
    schema: ApiParameterSchema,
    description: Option[String],
    enum: List[String]
  ) extends ApiParameter

  sealed trait ApiParameterSchema
  case class ApiParameterArraySchema(
    itemsSchema: ApiParameterSchema
  ) extends ApiParameterSchema
  case class ApiParameterValueSchema(
    typ: PropertyType
  ) extends ApiParameterSchema

//  sealed trait ApiParameterSchemaRef
//  case class SingleParameterSchemaRef(parameterSchema: ApiParameterSchema)   extends ApiParameterSchemaRef
//  case class MultipleParamSchemaRef(parameterSchema: ApiParameterSchema) extends ApiParameterSchemaRef

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
    typ: PropertyType,
    description: Option[String] = None,
    enum: List[String] = List.empty
  ) extends ApiSchema

  sealed trait ApiSchemaRef
  case class SingleRef(schema: ApiSchemaDefinition)   extends ApiSchemaRef
  case class MultipleRef(schema: ApiSchemaDefinition) extends ApiSchemaRef
  case class InlineSchema(schema: ApiSchema)          extends ApiSchemaRef
  case class MultipleInlineSchema(schema: ApiSchema)  extends ApiSchemaRef

  case class ApiPropertyDefinition(
    name: String,
    prop: ApiSchemaRef,
    required: Boolean,
    description: Option[String]
  )

  case class ApiSpec(
    root: ApiRoot,
    paths: List[ApiPathDefinition],
    schemata: List[ApiSchemaDefinition]
  )
}
