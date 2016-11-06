package fixtures

import internal.models._
import org.scalacheck._
import org.scalacheck.{Arbitrary => A}
import org.scalacheck.Shapeless._

object generators {

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
    name <- Gen.alphaLowerStr.suchThat(_.nonEmpty)
    domain <- Gen.alphaLowerStr.suchThat(_.nonEmpty)
  } yield s"$name@$domain"

  lazy val genApiRoot = for {
    swagger <- Gen.const("2.0")
    host <- A.arbitrary[Option[String]]
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
    url <- A.arbitrary[Option[String]]
    email <- genEmail
  } yield ApiContact(name, url, Some(email))

}
