package swaggerspec

import org.scalatest._
import helpers.JsonValidator.validate
import swaggerblocks.rendering.playJson._
import fixtures.{PetstoreAPI => P}
import helpers.CustomMatchers

class JsonValiditySpec extends WordSpec with MustMatchers with CustomMatchers {
  "The petstore example spec" when {
    "using 'render'" should {
      "produce valid swagger json" in {
        val json = render(P.petstoreRoot, P.paths, P.schemas)

        json must beValidSwaggerJson
      }
    }

    "using 'renderPretty'" should {
      "produce valid swagger json" in {
        val json = renderPretty(P.petstoreRoot, P.paths, P.schemas)

        json must beValidSwaggerJson
      }
    }
  }
}
