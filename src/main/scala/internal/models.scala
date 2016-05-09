package internal

import internal.dsl.path.OperationMetaData
import swaggerblocks._

object models {

  case class ApiRoot(swagger: String)

  case class ApiInfo(metaData: MetaData)

  case class ApiPath(path: String, operations: List[ApiOperation], metaData: MetaData)

  case class ApiOperation(method: Method, metaData: MetaData)

  object ApiOperation {
    def fromMetaData(m: OperationMetaData): ApiOperation = {
      ApiOperation(m.method, m.value)
    }
  }

  case class ApiSchema(name: String, metaData: MetaData)

}
