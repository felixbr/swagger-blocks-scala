package swaggerblocks.extensions

import internal.models.{ApiObjectSchema, ApiSchemaDefinition}
import net.jcazevedo.moultingyaml._

object yaml {
  implicit class ExampleExtension(schemaDef: ApiSchemaDefinition) {
    def withExample(yamlValue: YamlValue): ApiSchemaDefinition = schemaDef.schema match {
      case s: ApiObjectSchema =>
        schemaDef.copy(
          schema = s.copy(
            example = Some(yamlValue.prettyPrint)
          )
        )

      case _ =>
        schemaDef // nothing to do here
    }

    def withExample[T](obj: T)(implicit ev: YamlWriter[T]): ApiSchemaDefinition = {
      withExample(obj.toYaml)
    }
  }
}
