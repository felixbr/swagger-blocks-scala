package swaggerspec

import org.scalatest._
import helpers.JsonValidator.validate

import swaggerblocks.rendering.playJson._

import fixtures.{ PetstoreAPI => P }

class JsonValiditySpec extends WordSpec {
  "The petstore example spec" when {
    "using 'render'" should {
      "produce valid swagger json" in {
        val json = render(P.petstoreRoot, P.paths, P.schemas)

        assert(validate(json))
      }
    }

    "using 'renderPretty'" should {
      "produce valid swagger json" in {
        val json = renderPretty(P.petstoreRoot, P.paths, P.schemas)

        assert(validate(json))
      }
    }
  }
}
