package swaggerblocks

import internal.models.{ApiPathDefinition, ApiRoot, ApiSchemaDefinition}

trait RenderingPackage {
  def render(
    root: ApiRoot,
    paths: List[ApiPathDefinition],
    schemata: List[ApiSchemaDefinition]
  ): String

  def renderPretty(
    root: ApiRoot,
    paths: List[ApiPathDefinition],
    schemata: List[ApiSchemaDefinition]
  ): String
}
