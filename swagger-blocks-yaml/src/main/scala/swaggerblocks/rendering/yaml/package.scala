package swaggerblocks.rendering

import internal.models._
import net.jcazevedo.moultingyaml._
import swaggerblocks._
import swaggerblocks.rendering.yaml.formats._
import internal.modelTransformations.transformSpec

package object yaml extends RenderingPackage {
  def render(
    root: ApiRoot,
    paths: List[ApiPathDefinition],
    schemata: List[ApiSchemaDefinition]): String = {

    yamlAst(root, paths, schemata).print(scalarStyle = SingleQuoted)
  }

  def renderPretty(
    root: ApiRoot,
    paths: List[ApiPathDefinition],
    schemata: List[ApiSchemaDefinition]): String = {

    yamlAst(root, paths, schemata).prettyPrint
  }

  def yamlAst(
    root: ApiRoot,
    paths: List[ApiPathDefinition],
    schemata: List[ApiSchemaDefinition]): YamlValue = {

    transformSpec(ApiSpec(root, paths, schemata)).toYaml
  }
}
