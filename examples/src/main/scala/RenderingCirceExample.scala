import ApiExample._
import swaggerblocks.rendering.circe._

object RenderingCirceExample extends App {

  println(s"\n${renderPretty(petstoreRoot, List(petsPath), List(petSchema))}\n")

}