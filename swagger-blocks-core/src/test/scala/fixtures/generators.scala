package fixtures

import internal.models._
import internal.propertyTypes
import internal.propertyFormats
import internal.propertyTypes.PropertyType
import org.scalacheck._
import org.scalacheck.{Arbitrary => A}
import org.scalacheck.Shapeless._
import org.scalacheck.rng.Seed
import swaggerblocks._
import org.scalacheck.Prop._

import scala.util.Random

object generators {

  implicit class RichGen(gen: Gen[_]) {
    def failures(): Int = {
      val params = Gen.Parameters.default
      (1 to 1000).map { _ =>
        val seed = Seed(Random.nextLong())
        gen.apply(params, seed)
      }.count(s => s.isEmpty)
    }

    def test() = {
      forAll(gen) { _ =>
        true
      }.check
    }
  }

  // GENERAL

  lazy val genDescription = Gen.option(Gen.alphaNumStr)

  lazy val genEnum = Gen.listOf(Gen.identifier)

  def genPropertyType(includeFile: Boolean = false): Gen[PropertyType] =
    for {
      stringFormat  <- A.arbitrary[propertyFormats.StringFormat]
      numberFormat  <- A.arbitrary[propertyFormats.NumberFormat]
      integerFormat <- A.arbitrary[propertyFormats.IntegerFormat]
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
      genSchemaMultipleRef(depth))

  def genSchemaSingleRef(depth: Int = 0): Gen[ApiSchemaRef] =
    for {
      schemaDef <- genSchemaDef(depth)
    } yield SingleRef(schemaDef)

  def genSchemaMultipleRef(depth: Int = 0): Gen[ApiSchemaRef] =
    for {
      schemaDef <- genSchemaDef(depth)
    } yield MultipleRef(schemaDef)

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
      required    <- A.arbitrary[Boolean]
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
    host         <- A.arbitrary[Option[String]].retryUntil(!_.contains(""))
    basePath     <- genBasePath
    info         <- genApiInfo
    externalDocs <- A.arbitrary[Option[ApiExternalDocs]]
  } yield ApiRoot(swagger, host, Some(basePath), info, externalDocs)

  lazy val genApiExternalDocs = for {
    description <- genDescription
    url = "http://foo.bar/?baz=qux#quux" // not checked right now
  } yield ApiExternalDocs(url, description)

  lazy val genApiInfo: Gen[ApiInfo] = for {
    version        <- A.arbitrary[String]
    title          <- A.arbitrary[String]
    description    <- A.arbitrary[Option[String]]
    termsOfService <- A.arbitrary[Option[String]]
    contact        <- genApiContact
    license        <- A.arbitrary(implicitly[Arbitrary[Option[ApiLicense]]])
  } yield ApiInfo(version, title, description, termsOfService, Some(contact), license)

  lazy val genApiContact = for {
    name <- A.arbitrary[Option[String]]
    url = "http://foo.bar/?baz=qux#quux" // not checked right now
    email <- genEmail
  } yield ApiContact(name, Some(url), Some(email))

  // PATHS

  lazy val genApiPathDefinition = for {
    path           <- Gen.identifier.map(s => s"/$s")
    pathDefinition <- genApiPath
  } yield ApiPathDefinition(path, pathDefinition)

  lazy val genApiPath = for {
    operationsList <- Gen.listOfN(3, genOperation)
  } yield ApiPath(operationsList.toMap)

  lazy val genOperation = for {
    method       <- A.arbitrary[Method]
    apiOperation <- genApiOperation
  } yield (method, apiOperation)

  lazy val genApiOperation = for {
    description <- Gen.option(Gen.alphaNumStr)
    summary     <- Gen.option(Gen.alphaNumStr)
    tags        <- Gen.listOf(Gen.identifier).map(_.distinct.take(2))
    parameters  <- Gen.listOf(genApiParameter).map(_.take(2))
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
    description <- Gen.alphaNumStr
  } yield ApiResponse(description, schema = None) // TODO schemaRef

  val genApiParameter: Gen[ApiParameter] = Gen.oneOf(
    genApiBodyParameter,
    genApiPathParameter,
    genApiQueryParameter,
    genApiHeaderParameter,
    genApiFormDataParameter
  )

  lazy val genApiBodyParameter: Gen[ApiParameter] = for {
    name        <- Gen.alphaNumStr
    required    <- A.arbitrary[Boolean]
    schema      <- genSchemaRef()
    description <- Gen.option(Gen.alphaNumStr)
    enum        <- genEnum
  } yield ApiBodyParameter(name, required, schema, description, enum)

  lazy val genApiPathParameter: Gen[ApiParameter] = for {
    name        <- Gen.alphaNumStr
    schema      <- genApiParameterSchema()
    description <- genDescription
  } yield
    ApiOtherParameter(name, in = Path, required = true, schema, description, enum = List.empty)

  lazy val genApiQueryParameter: Gen[ApiParameter] = for {
    name        <- Gen.alphaNumStr
    required    <- A.arbitrary[Boolean]
    schema      <- genApiParameterSchema()
    description <- genDescription
    enum        <- genEnum
  } yield ApiOtherParameter(name, in = Query, required, schema, description, enum)

  lazy val genApiHeaderParameter: Gen[ApiParameter] = for {
    name        <- Gen.identifier
    required    <- A.arbitrary[Boolean]
    schema      <- genApiParameterSchema()
    description <- genDescription
    enum        <- genEnum
  } yield ApiOtherParameter(name, in = Header, required, schema, description, enum)

  lazy val genApiFormDataParameter: Gen[ApiParameter] = for {
    name        <- Gen.alphaNumStr
    required    <- A.arbitrary[Boolean]
    schema      <- genApiParameterSchema()
    description <- genDescription
    enum        <- genEnum
  } yield ApiOtherParameter(name, in = FormData, required, schema, description, enum)

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
