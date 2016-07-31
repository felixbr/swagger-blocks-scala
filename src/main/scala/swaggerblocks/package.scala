import internal.models._
import swaggerblocks.s.PropertyType

import scala.language.implicitConversions

package object swaggerblocks {
  object s {
    sealed trait PropertyType
    case object String extends PropertyType
    case object Int extends PropertyType
    case object Number extends PropertyType
  }

  def swaggerRoot(swagger: String = "2.0")(
    host: Option[String] = None,
    basePath: Option[String] = None,
    info: ApiInfo
  ): ApiRoot = {
    ApiRoot(swagger, host, basePath, info)
  }

  def info(
    version: String,
    title: Option[String] = None,
    description: Option[String] = None,
    termsOfService: Option[String] = None,
    contact: Option[ApiContact] = None,
    license: Option[ApiLicense] = None
  ): ApiInfo = {
    ApiInfo(version, title, description, termsOfService, contact, license)
  }

  def contact(
    name: Option[String] = None
  ): Option[ApiContact] = {
    if (allNone(name))
      None
    else
      Some(ApiContact(name))
  }

  def license(
    name: Option[String] = None
  ): Option[ApiLicense] = {
    if (allNone(name))
      None
    else
      Some(ApiLicense(name))
  }

  def swaggerPath(path: String)(
    operations: List[ApiOperationDefinition] = List.empty
  ): ApiPathDefinition = {
    ApiPathDefinition(
      path,
      ApiPath(
        operations.map(o => (o.method, o.operation)).toMap
      )
    )
  }

  def operations(ops: ApiOperationDefinition*): List[ApiOperationDefinition] = ops.toList

  def operation(method: Method)(
    description: Option[String] = None,
    summary: Option[String] = None,
    tags: List[String] = List.empty,
    parameters: List[ApiParameter] = List.empty,
    responses: List[ApiResponseDefinition] = List.empty
  ): ApiOperationDefinition = {
    ApiOperationDefinition(
      method,
      ApiOperation(
        description,
        summary,
        tags,
        parameters,
        responses.map(r => (r.status, r.response)).toMap
      )
    )
  }

  def parameters(ps: ApiParameter*): List[ApiParameter] = ps.toList

  def parameter(
    name: String,
    in: ParameterIn,
    required: Boolean,
    typ: PropertyType,
    description: Option[String] = None,
    schema: Option[ApiSchemaRef] = None
  ): ApiParameter = {
    ApiParameter(name, in, required, typ, description, schema)
  }

  def responses(rs: ApiResponse*): List[ApiResponse] = rs.toList

  def response(statusCode: Int)(
    description: Option[String] = None,
    schema: Option[ApiSchemaRef] = None
  ): ApiResponseDefinition = {
    ApiResponseDefinition(
      statusCode.toString,
      ApiResponse(description, schema)
    )
  }

  def swaggerSchema(name: String)(
    properties: ApiPropertyDefinition*
  ): ApiSchemaDefinition = {
    ApiSchemaDefinition(name, ApiObjectSchema(properties.toList))
  }

  def property(name: String)(
    typ: PropertyType,
    required: Boolean = true,
    description: Option[String] = None
  ): ApiPropertyDefinition = {
    ApiPropertyDefinition(
      name,
      ApiProperty(typ, description),
      required
    )
  }

  def oneOf(schemaRef: ApiSchemaDefinition): Option[SingleRef] = {
    Some(SingleRef(schemaRef))
  }

  def manyOf(schemaRef: ApiSchemaDefinition): Option[MultipleRef] = {
    Some(MultipleRef(schemaRef))
  }

  def oneOf(propertyType: PropertyType): Option[InlineSchema] = {
    Some(InlineSchema(ApiValueSchema(propertyType)))
  }

  def manyOf(propertyType: PropertyType): Option[MultipleInlineSchema] = {
    Some(MultipleInlineSchema(ApiValueSchema(propertyType)))
  }

  private def allNone(optionals: Option[_]*): Boolean = {
    optionals.forall(_.isEmpty)
  }

  object Implicits {
    implicit def stringToSomeString(string: String): Option[String] = Some(string)
  }
}