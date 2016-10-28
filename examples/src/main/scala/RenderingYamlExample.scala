import ApiExample._
import swaggerblocks.rendering.yaml._

object RenderingYamlExample extends App {

  println(s"\n${renderPretty(petstoreRoot, List(petsPath), List(petSchema))}\n")

}
