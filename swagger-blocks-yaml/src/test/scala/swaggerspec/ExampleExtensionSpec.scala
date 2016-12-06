package swaggerspec

import fixtures.{PetstoreAPI => P}
import helpers.CustomMatchers
import net.jcazevedo.moultingyaml.DefaultYamlProtocol._
import net.jcazevedo.moultingyaml._
import org.scalatest._
import swaggerblocks.Implicits._
import swaggerblocks._
import swaggerblocks.extensions.yaml._
import swaggerblocks.rendering.yaml._

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
      "id"   -> 123.toYaml,
      "name" -> "Bello".toYaml
    ).toYaml
  )

  case class Dog(id: Int, name: String)
  implicit val dogFormat = yamlFormat2(Dog)

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
    "Using a Map" must {
      "contain the example" in {
        val yaml = renderPretty(P.petstoreRoot, List.empty, List(schemaWithExample))

        yaml.contains("example:") must be(true)
      }
    }

    "Using a CaseClass" must {
      "contain the example" in {
        val yaml = renderPretty(P.petstoreRoot, List.empty, List(schemaWithCaseClassExample))

        yaml.contains("example:") must be(true)
      }
    }
  }
}
