package fixtures

import swaggerblocks.internal.models._
import swaggerblocks.internal.propertyTypes
import swaggerblocks.internal.propertyFormats
import swaggerblocks.internal.propertyTypes.PropertyType
import org.scalacheck._
import org.scalacheck.Arbitrary._
import org.scalacheck.ScalacheckShapeless._
import org.scalacheck.rng.Seed
import swaggerblocks._
import org.scalacheck.Prop._
import swaggerblocks.internal.propertyValues.PropertyValue

import scala.util.Random

object generators {

  implicit class RichGen(gen: Gen[_]) {
    def failures(): Int = {
      val params = Gen.Parameters.default
      (1 to 1000)
        .map { _ =>
          val seed = Seed(Random.nextLong())
          gen.apply(params, seed)
        }
        .count(s => s.isEmpty)
    }

    def test() = {
      forAll(gen) { _ =>
        true
      }.check
    }
  }

  // GENERAL

  lazy val genDescription = Gen.option(Gen.alphaNumStr)

  lazy val genEnum = Gen.listOfN(3, Gen.identifier).map(_.distinct)

  lazy val genUrl = Gen.const("http://foo.bar/?baz=qux#quux")

  def genPropertyType(includeFile: Boolean = false): Gen[PropertyType] =
    for {
      stringFormat  <- arbitrary[propertyFormats.StringFormat]
      numberFormat  <- arbitrary[propertyFormats.NumberFormat]
      integerFormat <- arbitrary[propertyFormats.IntegerFormat]
      propertyType <- Gen.oneOf[PropertyType](
        propertyTypes.String(stringFormat),
        propertyTypes.Number(numberFormat),
        propertyTypes.Int(integerFormat),
        propertyTypes.Boolean
      )
    } yield propertyType

  // SCHEMA

  def genSchemaRef(depth: Int = 0) =
    Gen.oneOf( // TODO inline schema
      genSchemaSingleRef(depth),
      genSchemaMultipleRef(depth)
    )

  def genSchemaSingleRef(depth: Int = 0): Gen[ApiSchemaRef] =
    for {
      schemaDef <- genSchemaDef(depth)
    } yield SingleRef(schemaDef)

  def genSchemaMultipleRef(depth: Int = 0): Gen[ApiSchemaRef] =
    for {
      schemaDef <- genSchemaDef(depth)
    } yield MultipleRef(schemaDef)

  def genSchemaDefs(depth: Int = 0): Gen[List[ApiSchemaDefinition]] =
    Gen.listOfN(3, genSchemaDef(depth))

  def genSchemaDef(depth: Int = 0) =
    for {
      name   <- Gen.identifier
      schema <- genSchema(depth)
    } yield ApiSchemaDefinition(name, schema)

  def genSchema(depth: Int = 0): Gen[ApiSchema] =
    if (depth == 0) // only nest one level
      Gen.oneOf(
        genSchemaObject(depth),
        genSchemaValue(depth)
      )
    else
      genSchemaValue(depth)

  def genSchemaObject(depth: Int = 0): Gen[ApiSchema] =
    for {
      properties <- Gen.nonEmptyListOf(genPropertyDef(depth))
      example    <- Gen.const("""{"id": 123, "name": "Fiffi"}""")
    } yield ApiObjectSchema(properties)

  def genSchemaValue(depth: Int = 0): Gen[ApiSchema] =
    for {
      typ         <- genPropertyType()
      description <- genDescription
      enum        <- genEnum
    } yield ApiValueSchema(typ, description, enum)

  def genPropertyDef(depth: Int = 0): Gen[ApiPropertyDefinition] =
    for {
      name        <- Gen.identifier
      prop        <- genSchemaRef(depth + 1) // recursive!
      required    <- arbitrary[Boolean]
      description <- genDescription
    } yield ApiPropertyDefinition(name, prop, required, description)

  // ROOT

  lazy val genBasePath: Gen[String] = {
    def genSlash   = Gen.const("/")
    def genSegment = Gen.listOfN(5, Gen.alphaLowerChar).map(_.mkString)
    def genPathPart = Gen.frequency(
      (1, genSlash),
      (5, genSegment)
    )

    for {
      slash     <- genSlash
      segment   <- genSegment
      pathPart1 <- genPathPart
      pathPart2 <- genPathPart
      pathPart3 <- genPathPart
    } yield List(slash, pathPart1, pathPart2, pathPart3).mkString
  }

  lazy val genEmail: Gen[String] = for {
    name   <- Gen.identifier
    domain <- Gen.identifier
  } yield s"$name@$domain"

  lazy val genApiRoot = for {
    swagger      <- Gen.const("2.0")
    host         <- arbitrary[Option[String]].retryUntil(!_.contains(""))
    basePath     <- genBasePath
    info         <- genApiInfo
    externalDocs <- genApiExternalDocs
  } yield ApiRoot(swagger, host, Some(basePath), info, Some(externalDocs))

  lazy val genApiExternalDocs = for {
    description <- genDescription
    url         <- genUrl
  } yield ApiExternalDocs(url, description)

  lazy val genApiInfo: Gen[ApiInfo] = for {
    version        <- arbitrary[String]
    title          <- arbitrary[String]
    description    <- arbitrary[Option[String]]
    termsOfService <- arbitrary[Option[String]]
    contact        <- genApiContact
    license        <- arbitrary(implicitly[Arbitrary[Option[ApiLicense]]])
  } yield ApiInfo(version, title, description, termsOfService, Some(contact), license)

  lazy val genApiContact = for {
    name  <- arbitrary[Option[String]]
    url   <- genUrl
    email <- genEmail
  } yield ApiContact(name, Some(url), Some(email))

  lazy val genApiLicense = for {
    name <- Gen.identifier
    url  <- genUrl
  } yield ApiLicense(name, Some(url))

  // PATHS

  lazy val genApiPathDefinition = for {
    path           <- Gen.identifier.map(s => s"/$s")
    pathDefinition <- genApiPath
  } yield ApiPathDefinition(path, pathDefinition)

  lazy val genApiPath = for {
    operationsList <- Gen.listOfN(3, genOperation)
  } yield ApiPath(operationsList.toMap)

  lazy val genOperation = for {
    method       <- arbitrary[Method]
    apiOperation <- genApiOperation
  } yield (method, apiOperation)

  lazy val genApiOperation = for {
    description <- Gen.option(Gen.alphaNumStr)
    summary     <- Gen.option(Gen.alphaNumStr)
    tags        <- Gen.listOf(Gen.identifier).map(_.distinct.take(2))
    parameters  <- genApiParameters
    responses   <- genResponses
  } yield ApiOperation(description, summary, tags, parameters, responses)

  lazy val genResponses = for {
    rs <- Gen.nonEmptyListOf(genApiStatusResponse).map(_.take(2))
  } yield rs.toMap[String, ApiResponse]

  lazy val genApiStatusResponse = for {
    status   <- Gen.oneOf("default", "200", "201", "302", "400", "404", "500")
    response <- genApiResponse
  } yield (status, response)

  lazy val genApiResponse = for {
    description     <- Gen.alphaNumStr
    responseHeaders <- Gen.listOfN(1, genApiResponseHeader)
  } yield ApiResponse(description, schema = None, headers = responseHeaders) // TODO schemaRef

  lazy val genApiResponseHeader = for {
    name        <- Gen.identifier
    description <- genDescription
    schema      <- genApiParameterSchema()
    enum        <- genEnum
  } yield ApiResponseHeader(name, schema, description, enum)

  lazy val genApiParameters: Gen[List[ApiParameter]] =
    for {
      header <- genApiHeaderParameter
      path   <- genApiPathParameter
      query  <- genApiQueryParameter
      body   <- genApiBodyParameter
    } yield List(header, path, query, body)

  lazy val genApiParameter: Gen[ApiParameter] = Gen.oneOf(
    genApiBodyParameter,
    genApiPathParameter,
    genApiQueryParameter,
    genApiHeaderParameter,
    genApiFormDataParameter
  )

  lazy val genApiBodyParameter: Gen[ApiParameter] = for {
    name        <- Gen.alphaNumStr
    required    <- arbitrary[Boolean]
    schema      <- genSchemaRef()
    description <- Gen.option(Gen.alphaNumStr)
    enum        <- genEnum
  } yield ApiBodyParameter(name, required, schema, description, enum)

  lazy val genApiPathParameter: Gen[ApiParameter] = for {
    name        <- Gen.alphaNumStr
    schema      <- genApiParameterSchema()
    description <- genDescription
    default     <- genDefaultParam
  } yield
    ApiOtherParameter(
      name,
      in = Path,
      required = true,
      schema,
      description,
      default,
      enum = List.empty
    )

  lazy val genApiQueryParameter: Gen[ApiParameter] = for {
    name        <- Gen.alphaNumStr
    required    <- arbitrary[Boolean]
    schema      <- genApiParameterSchema()
    description <- genDescription
    default     <- genDefaultParam
    enum        <- genEnum
  } yield ApiOtherParameter(name, in = Query, required, schema, description, default, enum)

  lazy val genApiHeaderParameter: Gen[ApiParameter] = for {
    name        <- Gen.identifier
    required    <- arbitrary[Boolean]
    schema      <- genApiParameterSchema()
    description <- genDescription
    default     <- genDefaultParam
    enum        <- genEnum
  } yield ApiOtherParameter(name, in = Header, required, schema, description, default, enum)

  lazy val genApiFormDataParameter: Gen[ApiParameter] = for {
    name        <- Gen.alphaNumStr
    required    <- arbitrary[Boolean]
    schema      <- genApiParameterSchema()
    description <- genDescription
    default     <- genDefaultParam
    enum        <- genEnum
  } yield ApiOtherParameter(name, in = FormData, required, schema, description, default, enum)

  lazy val genDefaultParam: Gen[PropertyValue] = Gen.oneOf(
    Gen.alphaStr.map(PropertyValue.StringValue),
    Gen.oneOf(1 to 100).map(PropertyValue.IntValue),
    arbitrary[Boolean].map(PropertyValue.BooleanValue),
    Gen.const(PropertyValue.NullValue())
  )

  def genApiParameterSchema(depth: Int = 0): Gen[ApiParameterSchema] =
    if (depth == 0) // only nest one level
      Gen.oneOf(genApiParameterValue(), genApiParameterArray())
    else
      genApiParameterValue()

  def genApiParameterValue(depth: Int = 0, includeFileType: Boolean = false) =
    for {
      typ <- genPropertyType()
    } yield ApiParameterValueSchema(typ)

  def genApiParameterArray(depth: Int = 0) =
    for {
      parameterItems <- genApiParameterSchema(depth + 1) // recursive!
    } yield ApiParameterArraySchema(parameterItems)
}
