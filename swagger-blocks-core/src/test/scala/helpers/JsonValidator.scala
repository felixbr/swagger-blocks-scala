package helpers

import com.github.fge.jsonschema.main.JsonSchemaFactory
import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.github.fge.jackson.JsonLoader
import com.github.fge.jsonschema.core.report.ProcessingReport

object JsonValidator {
  lazy val mapper = new ObjectMapper

  lazy val jsonSchemaFactory = JsonSchemaFactory.byDefault
  lazy val swaggerSchemaNode = JsonLoader.fromResource("/swagger_2.0_schema.json")
  lazy val swaggerSchema     = jsonSchemaFactory.getJsonSchema(swaggerSchemaNode)

  def validateWithReport(json: String): ProcessingReport = {
    val node: JsonNode = mapper.readTree(mapper.getFactory.createParser(json.getBytes("utf-8")))

    swaggerSchema.validate(node)
  }

  def validate(json: String): Boolean = {
    validateWithReport(json) match {
      case report if report.isSuccess => true
      case report                     => throw new RuntimeException(report.toString)
    }
  }
}
