package internal.dsl

import common._
import internal.dsl.schema.SchemaMetaData
import swaggerblocks._

object path {

  class PathMetaData extends MetaDataMap {
    private var operations = List.empty[OperationMetaData]

    def operation(method: Method)(block: OperationMetaData => Unit): Unit = {
      buildMetaData(new OperationMetaData(method), block) { metaData =>
        operations = metaData :: operations
      }
    }

    def description = metaData.get("description")
    def description_=(description: String): Unit = {
      key("description", description)
    }

    def collectedOperations: List[OperationMetaData] = operations
  }

  class OperationMetaData(val method: Method) extends MetaDataMap {
    private var parameterList = List.empty[ParameterMetaData]
    private var responseList = List.empty[ResponseMetaData]

    def description = metaData.get("description")
    def description_=(description: String): Unit = {
      key("description", description)
    }

    def parameter(block: ParameterMetaData => Unit): Unit = {
      buildMetaData(new ParameterMetaData, block) { metaData =>
        parameterList = metaData :: parameterList
      }
    }

    def response(status: Int)(block: ResponseMetaData => Unit): Unit = {
      buildMetaData(new ResponseMetaData(status), block) { metaData =>
        responseList = metaData :: responseList
      }
    }
  }

  class ParameterMetaData extends MetaDataMap with ConsumesList with ProducesList {
    private val operationSchema = new OperationSchemaMetaData
    private val parameterItems = new ParameterItemsMetaData

    def name = metaData.get("name")
    def name_=(name: String): Unit = {
      key("name", name)
    }

    def in = metaData.get("in")
    def in_=(in: String): Unit = {
      key("in", in)
    }

    def description = metaData.get("description")
    def description_=(description: String): Unit = {
      key("description", description)
    }

    def required = metaData.get("required")
    def required_=(required: String): Unit = {
      key("required", required)
    }

    def items(block: ParameterItemsMetaData => Unit): Unit = {
      block(parameterItems)
    }

    def schema(block: OperationSchemaMetaData => Unit): Unit = {
      block(operationSchema)
    }
  }

  class ParameterItemsMetaData extends MetaDataMap {
    private var itemsSchema: Option[SchemaMetaData] = None
  }

  class ResponseMetaData(val status: Int) extends MetaDataMap {

  }

  class OperationSchemaMetaData extends MetaDataMap {
    private var operationSchema: Option[ParameterSchemaRef] = None

    def arrayOf(schema: SchemaMetaData): Unit = {
      key("type", "array")

      operationSchema = Some(ListRef(schema))
    }

    def objectOf(schema: SchemaMetaData): Unit = {
      metaData = metaData - "type"

      operationSchema = Some(ObjectRef(schema))
    }
  }



  class ParameterSchemaRef {

  }
  case class ObjectRef(schema: SchemaMetaData) extends ParameterSchemaRef
  case class ListRef(schema: SchemaMetaData) extends ParameterSchemaRef

}
