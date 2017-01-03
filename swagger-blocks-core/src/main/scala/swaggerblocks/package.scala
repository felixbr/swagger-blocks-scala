import internal.models._
import internal.{propertyFormats => f}
import internal.propertyTypes.PropertyType
import internal.{propertyTypes => s}

import scala.language.implicitConversions

package object swaggerblocks {
  object t {
    def string   = s.String(f.Text)
    def byte     = s.String(f.Byte)
    def binary   = s.String(f.Binary)
    def date     = s.String(f.Date)
    def dateTime = s.String(f.DateTime)
    def password = s.String(f.Password)

    def integer = s.Int(f.Int32)
    def long    = s.Int(f.Int64)

    def number = s.Number(f.Float)
    def float  = s.Number(f.Float)
    def double = s.Number(f.Double)

    def boolean = s.Boolean

    def file = s.File

    object parameter {
      object path {
        def string   = s.String(f.Text)
        def byte     = s.String(f.Byte)
        def binary   = s.String(f.Binary)
        def date     = s.String(f.Date)
        def dateTime = s.String(f.DateTime)
        def password = s.String(f.Password)

        def integer = s.Int(f.Int32)
        def long    = s.Int(f.Int64)

        def number = s.Number(f.Float)
        def float  = s.Number(f.Float)
        def double = s.Number(f.Double)

        def boolean = s.Boolean
      }
      object query {
        def string   = s.String(f.Text)
        def byte     = s.String(f.Byte)
        def binary   = s.String(f.Binary)
        def date     = s.String(f.Date)
        def dateTime = s.String(f.DateTime)
        def password = s.String(f.Password)

        def integer = s.Int(f.Int32)
        def long    = s.Int(f.Int64)

        def number = s.Number(f.Float)
        def float  = s.Number(f.Float)
        def double = s.Number(f.Double)

        def boolean = s.Boolean
      }
      object header {
        def string   = s.String(f.Text)
        def byte     = s.String(f.Byte)
        def binary   = s.String(f.Binary)
        def date     = s.String(f.Date)
        def dateTime = s.String(f.DateTime)
        def password = s.String(f.Password)

        def integer = s.Int(f.Int32)
        def long    = s.Int(f.Int64)

        def number = s.Number(f.Float)
        def float  = s.Number(f.Float)
        def double = s.Number(f.Double)

        def boolean = s.Boolean
      }
      object formData {
        def string   = s.String(f.Text)
        def byte     = s.String(f.Byte)
        def binary   = s.String(f.Binary)
        def date     = s.String(f.Date)
        def dateTime = s.String(f.DateTime)
        def password = s.String(f.Password)

        def integer = s.Int(f.Int32)
        def long    = s.Int(f.Int64)

        def number = s.Number(f.Float)
        def float  = s.Number(f.Float)
        def double = s.Number(f.Double)

        def boolean = s.Boolean

        def file = s.File
      }
    }

    object schema {
      def string   = s.String(f.Text)
      def byte     = s.String(f.Byte)
      def binary   = s.String(f.Binary)
      def date     = s.String(f.Date)
      def dateTime = s.String(f.DateTime)
      def password = s.String(f.Password)

      def integer = s.Int(f.Int32)
      def long    = s.Int(f.Int64)

      def number = s.Number(f.Float)
      def float  = s.Number(f.Float)
      def double = s.Number(f.Double)

      def boolean = s.Boolean
    }
  }

  def swaggerRoot(swagger: String = "2.0")(
    host: Option[String] = None,
    basePath: Option[String] = None,
    info: ApiInfo,
    externalDocs: Option[ApiExternalDocs] = None
  ): ApiRoot = {
    ApiRoot(swagger, host, basePath, info, externalDocs)
  }

  def info(
    version: String,
    title: String,
    description: Option[String] = None,
    termsOfService: Option[String] = None,
    contact: Option[ApiContact] = None,
    license: Option[ApiLicense] = None
  ): ApiInfo = {
    ApiInfo(version, title, description, termsOfService, contact, license)
  }

  def contact(
    name: Option[String] = None,
    url: Option[String] = None,
    email: Option[String] = None
  ): Option[ApiContact] = {
    if (allNone(name, url, email))
      None
    else
      Some(ApiContact(name, url, email))
  }

  def license(
    name: String,
    url: Option[String] = None
  ): Option[ApiLicense] = {
    Some(ApiLicense(name, url))
  }

  def externalDocs(
    url: String,
    description: Option[String] = None
  ): Option[ApiExternalDocs] = {
    Some(ApiExternalDocs(url, description))
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

  def operations(ops: ApiOperationDefinition*): List[ApiOperationDefinition] =
    ops.toList

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
        tags.distinct,
        parameters,
        responses.map(r => (r.status, r.response)).toMap
      )
    )
  }

  def parameters(ps: ApiParameter*): List[ApiParameter] = ps.toList

  def pathParameter(
    name: String,
    schema: ApiParameterSchema,
    description: Option[String] = None,
    enum: List[String] = List.empty
  ): ApiParameter = {
    ApiOtherParameter(name, Path, required = true, schema, description, enum)
  }

  def queryParameter(
    name: String,
    required: Boolean,
    schema: ApiParameterSchema,
    description: Option[String] = None,
    allowEmptyValue: Boolean = false,
    enum: List[String] = List.empty
  ): ApiParameter = {
    ApiOtherParameter(name, Query, required, schema, description, enum)
  }

  def headerParameter(
    name: String,
    required: Boolean,
    schema: ApiParameterSchema,
    description: Option[String] = None,
    enum: List[String] = List.empty
  ): ApiParameter = {
    ApiOtherParameter(name, Header, required, schema, description, enum)
  }

  def formDataParameter(
    name: String,
    required: Boolean,
    schema: ApiParameterSchema,
    description: Option[String] = None,
    allowEmptyValue: Boolean = false,
    enum: List[String] = List.empty
  ): ApiParameter = {
    ApiOtherParameter(name, FormData, required, schema, description, enum)
  }

  def bodyParameter(
    name: String,
    required: Boolean,
    schema: ApiSchemaRef,
    description: Option[String] = None,
    enum: List[String] = List.empty
  ): ApiParameter = {
    ApiBodyParameter(name, required, schema, description, enum)
  }

  object parameter {
    def oneOf(propertyType: PropertyType): ApiParameterSchema = {
      ApiParameterValueSchema(propertyType)
    }

    def manyOf(propertyType: PropertyType): ApiParameterSchema = {
      ApiParameterArraySchema(ApiParameterValueSchema(propertyType))
    }

    def manyOf(parameterSchema: ApiParameterSchema): ApiParameterSchema = {
      ApiParameterArraySchema(parameterSchema)
    }
  }

  def responses(rs: ApiResponse*): List[ApiResponse] = rs.toList

  def response(statusCode: Int)(
    description: String,
    schema: Option[ApiSchemaRef] = None,
    headers: List[ApiResponseHeader] = List.empty
  ): ApiResponseDefinition = {
    ApiResponseDefinition(
      statusCode.toString,
      ApiResponse(description, schema, headers)
    )
  }

  def responseHeader(
    name: String,
    schema: ApiParameterSchema,
    description: Option[String] = None,
    enum: List[String] = List.empty
  ): ApiResponseHeader = {
    ApiResponseHeader(name, schema, description, enum)
  }

  def swaggerSchema(name: String)(
    properties: ApiPropertyDefinition*
  ): ApiSchemaDefinition = {
    ApiSchemaDefinition(name, ApiObjectSchema(properties.toList))
  }

  def property(name: String)(
    schema: ApiSchemaRef,
    required: Boolean = true,
    description: Option[String] = None,
    enum: List[String] = List.empty
  ): ApiPropertyDefinition = schema match {
    case s @ SingleRef(schemaDef) =>
      ApiPropertyDefinition(name, s, required, description)
    case s @ MultipleRef(schemaDef) =>
      ApiPropertyDefinition(name, s, required, description)
    case InlineSchema(a: ApiValueSchema) =>
      val s = InlineSchema(a.copy(description = description, enum = enum))
      ApiPropertyDefinition(name, s, required, None)
    case MultipleInlineSchema(a: ApiValueSchema) =>
      val s = MultipleInlineSchema(a.copy(description = description, enum = enum))
      ApiPropertyDefinition(name, s, required, None)
    case s =>
      ApiPropertyDefinition(name, s, required, description)
  }

  def oneOf(schemaRef: ApiSchemaDefinition): ApiSchemaRef = {
    SingleRef(schemaRef)
  }

  def manyOf(schemaRef: ApiSchemaDefinition): ApiSchemaRef = {
    MultipleRef(schemaRef)
  }

  def oneOf(propertyType: PropertyType): ApiSchemaRef = {
    InlineSchema(ApiValueSchema(propertyType))
  }

  def manyOf(propertyType: PropertyType): ApiSchemaRef = {
    MultipleInlineSchema(ApiValueSchema(propertyType))
  }

  private def allNone(optionals: Option[_]*): Boolean = {
    optionals.forall(_.isEmpty)
  }

  object Implicits {
    implicit def stringToSomeString(string: String): Option[String] =
      Some(string)

    implicit def schemaRefToSomeSchemaRef(schemaRef: ApiSchemaRef): Option[ApiSchemaRef] =
      Some(schemaRef)

    implicit def schemaDefToSchemaRef(schemaDef: ApiSchemaDefinition): ApiSchemaRef =
      SingleRef(schemaDef)

    implicit def schemaDefToSomeSchemaRef(schemaDef: ApiSchemaDefinition): Option[ApiSchemaRef] =
      Some(SingleRef(schemaDef))

    implicit def oneOfPropertyToSchemaRef(typ: PropertyType): ApiSchemaRef =
      InlineSchema(ApiValueSchema(typ))

    implicit def oneOfPropertyToSomeSchemaRef(typ: PropertyType): Option[ApiSchemaRef] =
      Some(oneOfPropertyToSchemaRef(typ))

    implicit def oneOfPropertyToParameterSchema(typ: PropertyType): ApiParameterSchema =
      ApiParameterValueSchema(typ)
  }
}
