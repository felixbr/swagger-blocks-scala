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
    description: Option[String] = None,
    summary: Option[String] = None,
    operations: List[ApiOperation] = List.empty
  ): ApiPath = {
    ApiPath(path, description, summary, operations)
  }

  def operations(ops: ApiOperation*): List[ApiOperation] = ops.toList

  def operation(method: Method)(
    description: Option[String] = None,
    parameters: List[ApiParameter] = List.empty,
    responses: List[ApiResponse] = List.empty
  ): ApiOperation = {
    ApiOperation(method, description, parameters, responses)
  }

  def parameters(ps: ApiParameter*): List[ApiParameter] = ps.toList

  def parameter(
    name: String,
    in: String,
    required: Boolean,
    description: Option[String] = None,
    schema: Option[ApiSchemaRef] = None
  ): ApiParameter = {
    ApiParameter(name, in, required, description, schema)
  }

  def responses(rs: ApiResponse*): List[ApiResponse] = rs.toList

  def response(statusCode: Int)(
    description: Option[String] = None,
    schema: Option[ApiSchemaRef] = None
  ): ApiResponse = {
    ApiResponse(statusCode, description, schema)
  }

  def swaggerSchema(name: String)(
    properties: ApiProperty*
  ): ApiSchema = {
    ApiSchema(name, properties.toList)
  }

  def property(name: String)(
    typ: PropertyType,
    required: Boolean = true,
    description: Option[String] = None
  ): ApiProperty = {
    ApiProperty(name, typ.toString, required, description)
  }

  def oneOf(schemaRef: ApiSchema): Option[SingleRef] = {
    Some(SingleRef(schemaRef))
  }

  def manyOf(schemaRef: ApiSchema): Option[MultipleRef] = {
    Some(MultipleRef(schemaRef))
  }

  def valueOf(valueType: PropertyType): Option[InlineType] = {
    Some(InlineType(s.String))
  }

  private def allNone(optionals: Option[_]*): Boolean = {
    optionals.forall(_.isEmpty)
  }

  object Implicits {
    implicit def stringToSomeString(string: String): Option[String] = Some(string)
  }
}