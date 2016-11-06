package swaggerblocks.rendering

import internal.models._
import net.jcazevedo.moultingyaml._
import swaggerblocks._
import swaggerblocks.rendering.yaml.formats._

package object yaml extends RenderingPackage {
  def render(root: ApiRoot, paths: Seq[ApiPathDefinition], schemata: Seq[ApiSchemaDefinition]): String = {
    yamlAst(root, paths, schemata).print(scalarStyle = SingleQuoted)
  }

  def renderPretty(root: ApiRoot, paths: Seq[ApiPathDefinition], schemata: Seq[ApiSchemaDefinition]): String = {
    yamlAst(root, paths, schemata).prettyPrint
  }

  def yamlAst(root: ApiRoot, paths: Seq[ApiPathDefinition], schemata: Seq[ApiSchemaDefinition]): YamlValue = {
    ApiSpec.fromSeqs(root, paths, schemata).toYaml
  }
}
