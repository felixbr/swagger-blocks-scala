package swaggerblocks.rendering

import internal.models.{ApiPathDefinition, ApiRoot, ApiSchemaDefinition, ApiSpec}
import swaggerblocks.RenderingPackage
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import internal.modelTransformations.transformSpec
import circe.formats._

package object circe extends RenderingPackage {
  def render(
    root: ApiRoot,
    paths: List[ApiPathDefinition],
    schemata: List[ApiSchemaDefinition]): String = {

    val printer = Printer.noSpaces.copy(
      preserveOrder = true,
      dropNullKeys = true
    )

    transformSpec(ApiSpec(root, paths, schemata)).asJson.pretty(printer)
  }

  def renderPretty(
    root: ApiRoot,
    paths: List[ApiPathDefinition],
    schemata: List[ApiSchemaDefinition]): String = {

    val printer = Printer.spaces2.copy(
      preserveOrder = true,
      dropNullKeys = true,
      colonLeft = ""
    )

    transformSpec(ApiSpec(root, paths, schemata)).asJson.pretty(printer)
  }

  def jsonAst(root: ApiRoot, paths: List[ApiPathDefinition], schemata: List[ApiSchemaDefinition]) = {
    transformSpec(ApiSpec(root, paths, schemata)).asJson
  }
}
