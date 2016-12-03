package swaggerspec

import fixtures.{PetstoreAPI => P}
import helpers.CustomMatchers
import org.scalatest._
import swaggerblocks.rendering.playJson._

class JsonValiditySpec
    extends WordSpec
    with MustMatchers
    with CustomMatchers
    with ParallelTestExecution {

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
