package swaggerspec

import fixtures.{PetstoreAPI => P}
import helpers.CustomMatchers
import org.scalatest._
import play.api.libs.json._
import swaggerblocks.Implicits._
import swaggerblocks._
import swaggerblocks.extensions.playJson._
import swaggerblocks.rendering.playJson._

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
    Json.obj(
      "id"   -> 123,
      "name" -> "Bello"
    )
  )

  case class Dog(id: Int, name: String)
  implicit val dogFormat = Json.format[Dog]

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

        ast \ "definitions" \ "SchemaWithExample" \ "example" mustNot be(a[JsUndefined])
      }
    }

    "using a CaseClass example" must {
      "include the example in the ast" in {
        val ast = jsonAst(P.petstoreRoot, List.empty, List(schemaWithCaseClassExample))

        ast \ "definitions" \ "SchemaWithCaseClassExample" \ "example" mustNot be(a[JsUndefined])
      }
    }
  }
}
