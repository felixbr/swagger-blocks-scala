package swaggerblocks

import internal.models.{ApiPathDefinition, ApiRoot, ApiSchemaDefinition}

trait RenderingPackage {
  def render(root: ApiRoot, paths: Seq[ApiPathDefinition], schemata: Seq[ApiSchemaDefinition]): String

  def renderPretty(root: ApiRoot, paths: Seq[ApiPathDefinition], schemata: Seq[ApiSchemaDefinition]): String
}
