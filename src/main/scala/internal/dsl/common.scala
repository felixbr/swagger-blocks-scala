package internal.dsl

import swaggerblocks._

object common {
  trait MetaDataMap {
    protected var metaData: MetaData = Map.empty

    def key(key: String, value: String) = {
      metaData = metaData + (key -> value)
    }

    def value = metaData
  }

  trait ConsumesList {
    protected var consumesList = List.empty[String]

    def consumes = consumesList
    def consumes_=(contentTypes: List[String]): Unit = {
      consumesList = consumesList ++ contentTypes
    }

    def collectedConsumes = consumesList
  }

  trait ProducesList {
    protected var producesList = List.empty[String]

    def produces = producesList
    def produces_=(contentTypes: List[String]): Unit = {
      producesList = producesList ++ contentTypes
    }

    def collectedProduces = producesList
  }

  def buildMetaData[InnerMetaData, Result](metaData: InnerMetaData, block: InnerMetaData => Unit)
    (afterBlock: InnerMetaData => Result): Result = {

    block(metaData)
    afterBlock(metaData)
  }
}
