package swaggerblocks.rendering

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import swaggerblocks.RenderingPackage
import swaggerblocks.internal.modelTransformations.transformSpec
import swaggerblocks.internal.models._

package object json extends RenderingPackage with SpecEncoders {
  def render(
    root: ApiRoot,
    paths: List[ApiPathDefinition],
    schemata: List[ApiSchemaDefinition]
  ): String = {

    val printer = Printer.noSpaces.copy(
      preserveOrder = true,
      dropNullKeys = true
    )

    transformSpec(ApiSpec(root, paths, schemata)).asJson.pretty(printer)
  }

  def renderPretty(
    root: ApiRoot,
    paths: List[ApiPathDefinition],
    schemata: List[ApiSchemaDefinition]
  ): String = {

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
