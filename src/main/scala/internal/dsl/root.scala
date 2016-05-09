package internal.dsl

import internal.dsl.common._
import internal.dsl.info.InfoMetaData

object root {

  class RootMetaData extends MetaDataMap with ConsumesList with ProducesList {
    private val info = new InfoMetaData

    def host = metaData.get("host")
    def host_=(host: String): Unit = {
      key("host", host)
    }

    def basePath = metaData.get("basePath")
    def basePath_=(basePath: String): Unit = {
      key("basePath", basePath)
    }

    def info(block: InfoMetaData => Unit): Unit = {
      block(info)
    }
  }

}
