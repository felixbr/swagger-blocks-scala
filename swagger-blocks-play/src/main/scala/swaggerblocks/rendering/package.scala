package swaggerblocks

import internal.models._
import play.api.libs.json._
import rendering.formats._

package object rendering {
  object playJson {
    def render(root: ApiRoot, paths: Seq[ApiPathDefinition], schemata: Seq[ApiSchemaDefinition]): String = {
      Json.stringify(
        jsonAst(root, paths, schemata)
      )
    }

    def jsonAst(root: ApiRoot, paths: Seq[ApiPathDefinition], schemata: Seq[ApiSchemaDefinition]): JsValue = {
      Json.toJson(
        ApiSpec(
          root,
          paths.map(p => (p.path, p.metadata)).toMap,
          schemata.map(s => (s.name, s.schema)).toMap
        )
      )
    }
  }
}
