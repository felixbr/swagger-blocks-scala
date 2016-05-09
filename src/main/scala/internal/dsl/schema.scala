package internal.dsl

import common._

object schema {
  class SchemaMetaData extends MetaDataMap {
    private var properties = List.empty[PropertyMetaData]

    def property(name: String, required: Boolean = true)(block: PropertyMetaData => Unit): Unit = {
      buildMetaData(new PropertyMetaData(name, required), block) { metaData =>
        properties = metaData :: properties
      }
    }
  }

  class PropertyMetaData(val name: String, val required: Boolean) extends MetaDataMap {
    def typ = metaData.get("type")
    def typ_=(typ: String): Unit = {
      key("type", typ)
    }

    def description = metaData.get("description")
    def description_=(description: String): Unit = {
      key("description", description)
    }
  }
}
