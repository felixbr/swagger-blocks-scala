package swaggerblocks.rendering

import swaggerblocks._
import swaggerblocks.s.PropertyType

object writeLogic {

  def parameterInToString(obj: ParameterIn): String = obj match {
    case Query    => "query"
    case Header   => "header"
    case Path     => "path"
    case FormData => "formData"
    case Body     => "body"
  }

  def propertyTypeToString(obj: PropertyType): String = obj match {
    case s.String => "string"
    case s.Number => "number"
    case s.Int    => "integer"
  }

  def methodToString(obj: Method): String = obj match {
    case GET    => "get"
    case POST   => "post"
    case PUT    => "put"
    case PATCH  => "patch"
    case DELETE => "delete"
  }

  def referenceTo(name: String): (String, String) =
    "$ref" -> s"#/definitions/$name"

}
