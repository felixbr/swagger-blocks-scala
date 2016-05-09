package internal.dsl

import internal.dsl.common._

object tag {

  class TagMetaData extends MetaDataMap {
    private val externalDocs = new ExternalDocsMetaData

    def name = metaData.get("name")
    def name_=(name: String): Unit = {
      key("name", name)
    }

    def description = metaData.get("description")
    def description_=(description: String): Unit = {
      key("description", description)
    }

    def externalDocs(block: ExternalDocsMetaData => Unit): Unit = {
      block(externalDocs)
    }
  }

  class ExternalDocsMetaData extends MetaDataMap {
    def description = metaData.get("description")
    def description_=(description: String): Unit = {
      key("description", description)
    }

    def url = metaData.get("url")
    def url_=(url: String): Unit = {
      key("url", url)
    }
  }

}
