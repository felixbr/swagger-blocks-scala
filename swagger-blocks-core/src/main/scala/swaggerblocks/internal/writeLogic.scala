package swaggerblocks.internal

import swaggerblocks.internal.{propertyTypes => s}
import swaggerblocks._

object writeLogic {

  def parameterInToString(obj: ParameterIn): String = obj match {
    case Query    => "query"
    case Header   => "header"
    case Path     => "path"
    case FormData => "formData"
    case Body     => "body"
  }

  def propertyTypeToString(obj: s.PropertyType): String = obj match {
    case s.String(_) => "string"
    case s.Number(_) => "number"
    case s.Int(_)    => "integer"
    case s.Boolean   => "boolean"
    case s.File      => "file"
  }

  def methodToString(obj: Method): String = obj match {
    case GET    => "get"
    case POST   => "post"
    case PUT    => "put"
    case PATCH  => "patch"
    case DELETE => "delete"
  }

  def referenceTo(name: String): String =
    s"#/definitions/$name"

  def classNameToString(obj: Any): String = {
    obj.getClass.getName.split('$').last
  }

}
