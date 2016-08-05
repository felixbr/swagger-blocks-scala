import ApiExample._
import swaggerblocks.rendering.playJson._

object RenderingExample extends App {

  println(s"\n${render(petstoreRoot, List(petsPath), List(petSchema))}\n")

}
