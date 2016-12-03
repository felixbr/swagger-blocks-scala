package internal

import internal.models._

object specModels {

  type SpecSchemaName = String // e.g. id
  case class SpecSchema(
    $ref: Option[String] = None,
    `type`: Option[String] = None,
    format: Option[String] = None,
    title: Option[String] = None,
    description: Option[String] = None,
    required: Option[List[String]] = None,
    enum: Option[List[String]] = None,
    items: Option[SpecSchema] = None,
    properties: Option[Map[SpecSchemaName, SpecSchema]] = None
  )

  case class SpecParameter(
    name: String,
    in: String,
    description: Option[String] = None,
    required: Option[Boolean] = None,
    schema: Option[SpecSchema] = None,
    `type`: Option[String] = None,
    format: Option[String] = None,
    allowEmptyValue: Option[Boolean] = None,
    items: Option[SpecSchema] = None,
    collectionFormat: Option[String] = None,
    enum: Option[List[String]] = None
  )

  case class SpecResponse(
    description: String,
    schema: Option[SpecSchema] = None
  )

  type SpecResponseStatus = String
  case class SpecOperation(
    description: Option[String],
    summary: Option[String],
    tags: List[String],
    parameters: List[SpecParameter],
    responses: Map[SpecResponseStatus, SpecResponse]
  )

  type SpecPath   = String // e.g. /api/v1/pets
  type SpecMethod = String // e.g. get
  type SpecRef    = String // e.g. #/definitions/Pet
  case class Spec(
    swagger: String,
    host: Option[String],
    basePath: Option[String],
    info: ApiInfo,
    externalDocs: Option[ApiExternalDocs],
    paths: Map[SpecPath, Map[SpecMethod, SpecOperation]],
    definitions: Map[SpecRef, SpecSchema]
  )

}
