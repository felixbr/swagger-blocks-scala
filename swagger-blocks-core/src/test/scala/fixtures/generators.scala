package fixtures

import internal.models._
import org.scalacheck._
import org.scalacheck.{Arbitrary => A}
import org.scalacheck.Shapeless._
import org.scalacheck.rng.Seed
import swaggerblocks.Method
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
      forAll(gen) { _ => true }.check
    }
  }

  // ROOT

  lazy val genBasePath: Gen[String] = {
    def genSlash = Gen.const("/")
    def genSegment = Gen.listOfN(5, Gen.alphaLowerChar).map(_.mkString)
    def genPathPart = Gen.frequency(
      (1, genSlash),
      (5, genSegment)
    )

    for {
      slash <- genSlash
      segment <- genSegment
      pathPart1 <- genPathPart
      pathPart2 <- genPathPart
      pathPart3 <- genPathPart
    } yield List(slash, pathPart1, pathPart2, pathPart3).mkString
  }

  lazy val genEmail: Gen[String] = for {
    name <- Gen.identifier
    domain <- Gen.identifier
  } yield s"$name@$domain"

  lazy val genApiRoot = for {
    swagger <- Gen.const("2.0")
    host <- A.arbitrary[Option[String]].retryUntil(!_.contains(""))
    basePath <- genBasePath
    info <- genApiInfo
    externalDocs <- A.arbitrary[Option[ApiExternalDocs]]
  } yield ApiRoot(swagger, host, Some(basePath), info, externalDocs)

  lazy val genApiInfo: Gen[ApiInfo] = for {
    version <- A.arbitrary[String]
    title <- A.arbitrary[String]
    description <- A.arbitrary[Option[String]]
    termsOfService <- A.arbitrary[Option[String]]
    contact <- genApiContact
    license <- A.arbitrary(implicitly[Arbitrary[Option[ApiLicense]]])
  } yield ApiInfo(version, title, description, termsOfService, Some(contact), license)

  lazy val genApiContact = for {
    name <- A.arbitrary[Option[String]]
    url = "http://foo.bar/?baz=qux#quux" // not checked right now
    email <- genEmail
  } yield ApiContact(name, Some(url), Some(email))

  // PATHS

  lazy val genApiPathDefinition = for {
    path <- Gen.identifier.map(s => s"/$s")
    pathDefinition <- genApiPath
  } yield ApiPathDefinition(path, pathDefinition)

  lazy val genApiPath = for {
    operationsList <- Gen.listOfN(3, genOperation)
  } yield ApiPath(operationsList.toMap)

  lazy val genOperation = for {
    method <- A.arbitrary[Method]
    apiOperation <- genApiOperation
  } yield (method, apiOperation)

  lazy val genApiOperation = for {
    description <- Gen.option(Gen.alphaNumStr)
    summary <- Gen.option(Gen.alphaNumStr)
    tags <- Gen.listOf(Gen.identifier).map(_.distinct.take(2))
    parameters <- Gen.listOf(genApiParameter).map(_.take(2))
    responses <- genResponses
  } yield ApiOperation(description, summary, tags, parameters, responses)

  lazy val genResponses = for {
    rs <- Gen.nonEmptyListOf(genApiReponse).map(_.take(2))
  } yield rs.toMap[String, ApiResponse]

  lazy val genApiReponse = for {
    status <- Gen.oneOf("default", "200", "201", "302", "400", "404", "500")
    response <- A.arbitrary[ApiResponse]
  } yield (status, response)

  lazy val genApiParameter = A.arbitrary[ApiParameter]
}
