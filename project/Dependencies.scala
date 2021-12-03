import sbt._

object Version {
  final val scala               = "2.12.6"
  final val scala_2_11          = "2.11.11"
  final val scala_2_13          = "2.13.6"
  final val crossVersions       = List(scala_2_11, scala, scala_2_13)
  final val scalaTest           = "3.0.9"
  final val jsonSchemaValidator = "2.2.6"
  final val scalacheck          = "1.14.0"
  final val scalacheckShapeless = "1.2.3"
  final val circe               = "0.9.3"
  final val circeYaml           = "0.8.0"
}

object Lib {
  val scalaTest           = "org.scalatest"              %% "scalatest"                 % Version.scalaTest
  val jsonSchemaValidator = "com.github.fge"             % "json-schema-validator"      % Version.jsonSchemaValidator
  val scalacheck          = "org.scalacheck"             %% "scalacheck"                % Version.scalacheck
  val scalacheckShapeless = "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % Version.scalacheckShapeless
  val circeCore           = "io.circe"                   %% "circe-core"                % Version.circe
  val circeGeneric        = "io.circe"                   %% "circe-generic"             % Version.circe
  val circeParser         = "io.circe"                   %% "circe-parser"              % Version.circe
  val circeYaml           = "io.circe"                   %% "circe-yaml"                % Version.circeYaml
}
