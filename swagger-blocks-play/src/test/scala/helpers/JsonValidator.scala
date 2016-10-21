package helpers

import com.github.fge.jsonschema.main.JsonSchemaFactory
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.core.report.ProcessingReport

object JsonValidator {
  lazy val mapper = new ObjectMapper

  lazy val jsonSchemaFactory = JsonSchemaFactory.byDefault
  lazy val swaggerSchemaNode = JsonLoader.fromResource("/swagger_2.0_schema.json")
  lazy val swaggerSchema = jsonSchemaFactory.getJsonSchema(swaggerSchemaNode)

  def validate(json: String): Boolean = {
    val node: JsonNode = mapper.readTree(mapper.getFactory.createParser(json.iterator.toArray))

    val report: ProcessingReport = swaggerSchema.validate(node)
    report match {
      case _ if report.isSuccess => true
      case _ => throw new RuntimeException(report.toString)
    }
  }
}
