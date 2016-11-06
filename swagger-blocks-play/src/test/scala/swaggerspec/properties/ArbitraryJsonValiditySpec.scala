package swaggerspec.properties

import fixtures.{ PetstoreAPI => P }
import internal.models._
import org.scalatest._
import helpers.JsonValidator.validate
import org.scalacheck.Prop._
import fixtures.generators._
import org.scalatest.prop.Checkers
import swaggerblocks.rendering.playJson._

class ArbitraryJsonValiditySpec extends WordSpec with Checkers {

  "A spec with generated ApiRoot" when {
    "using 'render'" should {
      "produce valid swagger json" in {
        val validSwaggerJsonProp = forAll(genApiRoot) { apiRoot =>
          val json = render(apiRoot, List.empty, List.empty)

          validate(json)
        }

        check(validSwaggerJsonProp)
      }
    }
  }

  "A spec with generated ApiPathDefinition" when {
    "using 'render'" should {
      "produce valid swagger json" in {
        val validSwaggerJsonProp = forAll(genApiPathDefinition) { apiPathDefinition =>
          val json = render(P.petstoreRoot, List(apiPathDefinition), List.empty)

          validate(json)
        }

        check(validSwaggerJsonProp)
      }
    }
  }
}
