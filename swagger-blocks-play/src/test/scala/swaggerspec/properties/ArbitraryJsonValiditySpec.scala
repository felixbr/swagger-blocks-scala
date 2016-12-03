package swaggerspec.properties

import fixtures.generators._
import fixtures.{PetstoreAPI => P}
import helpers.CustomMatchers
import org.scalatest._
import org.scalatest.prop.{Checkers, GeneratorDrivenPropertyChecks}
import swaggerblocks.rendering.playJson._

class ArbitraryJsonValiditySpec
    extends WordSpec
    with Checkers
    with MustMatchers
    with CustomMatchers
    with GeneratorDrivenPropertyChecks
    with ParallelTestExecution {

  implicit override val generatorDrivenConfig = PropertyCheckConfiguration(
    sizeRange = 10,
    workers = 4
  )

  "A spec with generated ApiRoot" when {
    "using 'renderPretty'" must {
      "produce valid swagger json" in {
        forAll(genApiRoot) { apiRoot =>
          val json = renderPretty(apiRoot, List.empty, List.empty)

          json must beValidSwaggerJson
        }
      }
    }
  }

  "A spec with generated ApiPathDefinition" when {
    "using 'renderPretty'" must {
      "produce valid swagger json" in {
        forAll(genApiPathDefinition) { apiPathDefinition =>
          val json = renderPretty(P.petstoreRoot, List(apiPathDefinition), List.empty)

          json must beValidSwaggerJson
        }
      }
    }
  }

  "A spec with generated ApiSchemaDefinitions" when {
    "using 'renderPretty'" must {
      "preoduce valid swagger json" in {
        forAll(genSchemaDefs()) { apiSchemaDefinitions =>
          val json = renderPretty(P.petstoreRoot, List.empty, apiSchemaDefinitions)

          json must beValidSwaggerJson
        }
      }
    }
  }
}
