package internal

import swaggerblocks._
import swaggerblocks.s.PropertyType

object models {

  case class ApiRoot(
    swagger: String,
    host: Option[String],
    basePath: Option[String],
    info: ApiInfo
  )

  case class ApiInfo(
    version: String,
    title: Option[String],
    description: Option[String],
    termsOfService: Option[String],
    contact: Option[ApiContact],
    license: Option[ApiLicense]
  )

  case class ApiContact(name: Option[String])

  case class ApiLicense(name: Option[String])

  case class ApiPath(
    path: String,
    description: Option[String],
    summary: Option[String],
    operations: List[ApiOperation]
  )

  case class ApiOperation(
    method: Method,
    description: Option[String],
    parameters: List[ApiParameter],
    responses: List[ApiResponse]
  )

  case class ApiParameter(
    name: String,
    in: String,
    required: Boolean,
    description: Option[String],
    schema: Option[ApiSchemaRef]
  )

  case class ApiResponse(
    status: Int,
    description: Option[String],
    schema: Option[ApiSchemaRef]
  )

  case class ApiSchema(
    name: String,
    properties: List[ApiProperty]
  )

  sealed trait ApiSchemaRef
  case class SingleRef(schema: ApiSchema) extends ApiSchemaRef
  case class MultipleRef(schema: ApiSchema) extends ApiSchemaRef
  case class InlineType(t: PropertyType) extends ApiSchemaRef

  case class ApiProperty(
    name: String,
    `type`: String,
    required: Boolean,
    description: Option[String]
  )
}
