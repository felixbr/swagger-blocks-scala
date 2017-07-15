import ApiExample._
import swaggerblocks.rendering.json._

object RenderingJsonExample extends App {

  println(s"\n${renderPretty(petstoreRoot, List(petsPath), List(petSchema))}\n")

}