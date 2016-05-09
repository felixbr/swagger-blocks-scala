package internal.dsl

import internal.dsl.common._

object info {

  class InfoMetaData extends MetaDataMap {
    private val contact = new ContactMetaData
    private val license = new LicenseMetaData

    def version = metaData.get("version")
    def version_=(version: String): Unit = {
      key("version", version)
    }

    def title = metaData.get("title")
    def title_=(title: String): Unit = {
      key("title", title)
    }

    def description = metaData.get("description")
    def description_=(description: String): Unit = {
      key("description", description)
    }

    def termsOfService = metaData.get("termsOfService")
    def termsOfService_=(termsOfService: String): Unit = {
      key("termsOfService", termsOfService)
    }

    def contact(block: ContactMetaData => Unit): Unit = {
      block(contact)
    }

    def license(block: LicenseMetaData => Unit): Unit = {
      block(license)
    }
  }

  class ContactMetaData extends MetaDataMap {
    def name = metaData.get("name")
    def name_=(name: String): Unit = {
      key("name", name)
    }
  }

  class LicenseMetaData extends MetaDataMap {
    def name = metaData.get("name")
    def name_=(name: String): Unit = {
      key("name", name)
    }
  }

}
