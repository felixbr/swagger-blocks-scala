package internal

import internal.models._
import internal.specModels._
import swaggerblocks.Method
import writeLogic._

// api* to spec* transformations
object modelTransformations {

  def transformSchemaRef(
    apiSchemaRef: ApiSchemaRef,
    description: Option[String] = None): SpecSchema = apiSchemaRef match {

    case SingleRef(schemaDef) =>
      SpecSchema(
        $ref = Some(referenceTo(schemaDef.name)),
        description = description
      )

    case MultipleRef(schemaDef) =>
      SpecSchema(
        `type` = Some("array"),
        items = Some(
          SpecSchema(
            $ref = Some(referenceTo(schemaDef.name))
          )),
        description = description // TODO check if this has to be in items object
      )

    case InlineSchema(schema) =>
      transformSchema(schema)

    case MultipleInlineSchema(schema) =>
      SpecSchema(
        `type` = Some("array"),
        items = Some(transformSchema(schema))
      )

  }

  def transformSchema(apiSchema: ApiSchema): SpecSchema = apiSchema match {
    case ApiObjectSchema(propdefs, example) =>
      val requiredProps = propdefs.collect {
        case p: ApiPropertyDefinition if p.required => p.name
      }.distinct // distinct is mostly needed for generator tests
      val propMap: Map[String, SpecSchema] = propdefs.map {
        case ApiPropertyDefinition(name, propSchema, _, description) =>
          // pass through the description for the referenced object
          name -> transformSchemaRef(propSchema, description)
      }.toMap

      SpecSchema(
        `type` = Some("object"),
        required = if (requiredProps.nonEmpty) Some(requiredProps) else None,
        properties = Some(propMap),
        example = example.map(SpecExample)
      )

    case ApiValueSchema(propType, description, enum) =>
      SpecSchema(
        `type` = Some(propertyTypeToString(propType)),
        description = description,
        enum = if (enum.isEmpty) None else Some(enum)
      )
  }

  def transformParameter(apiParameter: ApiParameter): SpecParameter = apiParameter match {
    case p: ApiBodyParameter =>
      SpecParameter(
        name = p.name,
        in = parameterInToString(p.in),
        description = p.description,
        required = Some(p.required),
        schema = Some(transformSchemaRef(p.schema))
      )

    case p: ApiOtherParameter =>
      val base = SpecParameter(
        name = p.name,
        in = parameterInToString(p.in),
        description = p.description,
        required = Some(p.required),
        enum = if (p.enum.isEmpty) None else Some(p.enum)
      )
      transformParameterSchema(base, p.schema)
  }

  def transformParameterSchema(base: SpecParameter, schema: ApiParameterSchema): SpecParameter = {
    schema match {
      // TODO fix this mess
      case ApiParameterArraySchema(itemsSchema) =>
        val innerSchema = itemsSchema.asInstanceOf[ApiParameterValueSchema]

        base.copy(
          `type` = Some("array"),
          items = Some(
            SpecSchema(
              `type` = Some(propertyTypeToString(innerSchema.typ))
            )
          )
        )

      case ApiParameterValueSchema(typ) =>
        base.copy(
          `type` = Some(propertyTypeToString(typ))
        )
    }
  }

  def transformResponseHeader(apiResponseHeader: ApiResponseHeader): SpecResponseHeader = {
    val base = SpecResponseHeader(
      description = apiResponseHeader.description,
      enum = if (apiResponseHeader.enum.isEmpty) None else Some(apiResponseHeader.enum)
    )
    transformResponseHeaderSchema(base, apiResponseHeader.schema)
  }

  def transformResponseHeaderSchema(
    base: SpecResponseHeader,
    schema: ApiParameterSchema): SpecResponseHeader = {

    schema match {
      // TODO fix this mess
      case ApiParameterArraySchema(itemsSchema) =>
        val innerSchema = itemsSchema.asInstanceOf[ApiParameterValueSchema]

        base.copy(
          `type` = Some("array"),
          items = Some(
            SpecSchema(
              `type` = Some(propertyTypeToString(innerSchema.typ))
            )
          )
        )

      case ApiParameterValueSchema(typ) =>
        base.copy(
          `type` = Some(propertyTypeToString(typ))
        )
    }
  }

  def transfromResponse(apiResponse: ApiResponse): SpecResponse = {
    SpecResponse(
      description = apiResponse.description,
      schema = apiResponse.schema.map(transformSchemaRef(_)),
      headers = apiResponse.headers.map(h => h.name -> transformResponseHeader(h)).toMap
    )
  }

  def transformOperation(apiOperation: ApiOperation): SpecOperation = {
    SpecOperation(
      description = apiOperation.description,
      summary = apiOperation.summary,
      tags = apiOperation.tags,
      parameters = apiOperation.parameters.map(transformParameter),
      responses = apiOperation.responses.map {
        case (status: String, apiResponse) =>
          status -> transfromResponse(apiResponse)
      }
    )
  }

  def transformSpec(apiSpec: ApiSpec): Spec = {
    val paths: Map[SpecPath, Map[SpecMethod, SpecOperation]] =
      apiSpec.paths.map { pathDef =>
        val opsMap = pathDef.metadata.operations.map {
          case (m: Method, op: ApiOperation) =>
            methodToString(m) -> transformOperation(op)
        }

        pathDef.path -> opsMap
      }.toMap

    val definitions: Map[SpecRef, SpecSchema] =
      apiSpec.schemata.map { apiSchemaDef =>
        apiSchemaDef.name -> transformSchema(apiSchemaDef.schema)
      }.toMap

    Spec(
      swagger = apiSpec.root.swagger,
      host = apiSpec.root.host,
      basePath = apiSpec.root.basePath,
      info = apiSpec.root.info,
      externalDocs = apiSpec.root.externalDocs,
      paths,
      definitions
    )
  }

}
