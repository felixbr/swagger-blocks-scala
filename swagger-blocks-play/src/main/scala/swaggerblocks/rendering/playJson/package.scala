package swaggerblocks.rendering

import internal.models._
import play.api.libs.json._
import swaggerblocks.RenderingPackage
import swaggerblocks.rendering.playJson.formats._

package object playJson extends RenderingPackage {
  def render(root: ApiRoot, paths: Seq[ApiPathDefinition], schemata: Seq[ApiSchemaDefinition]): String = {
    Json.stringify(
      jsonAst(root, paths, schemata)
    )
  }

  def renderPretty(root: ApiRoot, paths: Seq[ApiPathDefinition], schemata: Seq[ApiSchemaDefinition]): String = {
    Json.prettyPrint(
      jsonAst(root, paths, schemata)
    )
  }

  def jsonAst(root: ApiRoot, paths: Seq[ApiPathDefinition], schemata: Seq[ApiSchemaDefinition]): JsValue = {
    Json.toJson(
      ApiSpec.fromSeqs(root, paths, schemata)
    )
  }
}
