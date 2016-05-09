import internal.dsl.common.buildMetaData
import internal.dsl.info._
import internal.dsl.path._
import internal.dsl.root._
import internal.dsl.schema._
import internal.models._

package object swaggerblocks {
  type MetaData = Map[String, String]

  def swaggerRoot(swagger: String = "2.0")(block: RootMetaData => Unit): ApiRoot = {
    buildMetaData(new RootMetaData, block) { metaData =>
      ApiRoot(swagger)
    }
  }

  def swaggerPath(path: String)(block: PathMetaData => Unit): ApiPath = {
    buildMetaData(new PathMetaData, block) { metaData =>
      ApiPath(
        path,
        metaData.collectedOperations.map(ApiOperation.fromMetaData),
        metaData.value
      )
    }
  }

  def swaggerSchema(name: String, reqired: Boolean = true)
    (block: SchemaMetaData => Unit): ApiSchema = {

    buildMetaData(new SchemaMetaData, block) { metaData =>
      ApiSchema(name, metaData.value)
    }
  }
}