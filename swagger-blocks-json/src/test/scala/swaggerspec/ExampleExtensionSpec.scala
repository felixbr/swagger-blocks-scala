package swaggerspec

import fixtures.{PetstoreAPI => P}
import helpers.CustomMatchers
import org.scalatest._
import swaggerblocks.Implicits._
import swaggerblocks._
import swaggerblocks.rendering.json._
import swaggerblocks.extensions.json.ExampleExtension
import io.circe.generic.auto._

class ExampleExtensionSpec
    extends WordSpec
    with MustMatchers
    with CustomMatchers
    with ParallelTestExecution {

  val schemaWithExample = swaggerSchema("SchemaWithExample")(
    property("id")(
      schema = t.integer
    ),
    property("name")(
      schema = t.string
    )
  ).withExample(
    Map(
      "id"   -> "123",
      "name" -> "Bello"
    )
  )

  case class Dog(id: Int, name: String)

  val schemaWithCaseClassExample = swaggerSchema("SchemaWithCaseClassExample")(
    property("id")(
      schema = t.integer
    ),
    property("name")(
      schema = t.string
    )
  ).withExample(
    Dog(123, "Fiffi")
  )

  "The petstore example spec" when {
    "using 'renderPretty'" must {
      "produce valid swagger json" in {
        val json = renderPretty(P.petstoreRoot, List.empty, List(schemaWithExample))

        json must beValidSwaggerJson
      }
    }

    "adding an inline example" must {
      "include the example in the ast" in {
        val ast = jsonAst(P.petstoreRoot, List.empty, List(schemaWithExample))

        ast.hcursor
          .downField("definitions")
          .downField("SchemaWithExample")
          .get[Map[String, String]]("example") must be(a[Right[_, _]])
      }
    }

    "using a CaseClass example" must {
      "include the example in the ast" in {
        val ast = jsonAst(P.petstoreRoot, List.empty, List(schemaWithCaseClassExample))

        ast.hcursor
          .downField("definitions")
          .downField("SchemaWithCaseClassExample")
          .get[Dog]("example") must be(a[Right[_, _]])
      }
    }
  }
}
