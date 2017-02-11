package swaggerblocks.rendering

import swaggerblocks.internal.models._
import play.api.libs.json._
import swaggerblocks.RenderingPackage
import swaggerblocks.rendering.playJson.formats._
import swaggerblocks.internal.modelTransformations.transformSpec

package object playJson extends RenderingPackage {
  def render(
    root: ApiRoot,
    paths: List[ApiPathDefinition],
    schemata: List[ApiSchemaDefinition]): String = {

    Json.stringify(
      jsonAst(root, paths, schemata)
    )
  }

  def renderPretty(
    root: ApiRoot,
    paths: List[ApiPathDefinition],
    schemata: List[ApiSchemaDefinition]): String = {

    Json.prettyPrint(
      jsonAst(root, paths, schemata)
    )
  }

  def jsonAst(
    root: ApiRoot,
    paths: List[ApiPathDefinition],
    schemata: List[ApiSchemaDefinition]): JsValue = {

    Json.toJson(
      transformSpec(ApiSpec(root, paths, schemata))
    )
  }
}
