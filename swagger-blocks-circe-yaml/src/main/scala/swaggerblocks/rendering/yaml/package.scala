package swaggerblocks.rendering

import io.circe.yaml._
import io.circe.generic.auto._
import io.circe.syntax._
import swaggerblocks._
import swaggerblocks.internal.modelTransformations.transformSpec
import swaggerblocks.internal.models._

package object yaml extends RenderingPackage with SpecEncoders {
  def render(
    root: ApiRoot,
    paths: List[ApiPathDefinition],
    schemata: List[ApiSchemaDefinition]
  ): String = {

    val printer = Printer.spaces2.copy(
      preserveOrder = true,
      dropNullKeys = true
    )

    printer.pretty(transformSpec(ApiSpec(root, paths, schemata)).asJson)
  }

  def renderPretty(
    root: ApiRoot,
    paths: List[ApiPathDefinition],
    schemata: List[ApiSchemaDefinition]
  ): String = {

    val printer = Printer.spaces2.copy(
      preserveOrder = true,
      dropNullKeys = true
    )

    printer.pretty(transformSpec(ApiSpec(root, paths, schemata)).asJson)
  }

  def jsonAst(root: ApiRoot, paths: List[ApiPathDefinition], schemata: List[ApiSchemaDefinition]) = {
    transformSpec(ApiSpec(root, paths, schemata)).asJson
  }
}
