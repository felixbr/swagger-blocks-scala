val commonSettings = List(
  Quiet.silenceScalaBinaryVersionWarning,
  scalaVersion := Version.scala,
  crossScalaVersions := Version.crossVersions,
  version := "0.6.0",
  homepage := Some(url("https://github.com/felixbr/swagger-blocks-scala")),
  organization := "io.github.felixbr",
  licenses := List(
    "MIT License" -> url("http://www.opensource.org/licenses/mit-license.php")
  )
)

val commonDeps = List(
  Lib.scalaTest           % "test",
  Lib.jsonSchemaValidator % "test",
  Lib.scalacheck          % "test",
  Lib.scalacheckShapeless % "test"
)

lazy val root = project
  .in(file("."))
  .aggregate(core, json, yaml, examples)
  .settings(commonSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    publishArtifact := false,
    publish := {},
    publishLocal := {},
    packagedArtifacts := Map.empty,
    aggregate in update := false
  )

lazy val core = project
  .in(file("swagger-blocks-core"))
  .settings(commonSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    name := "swagger-blocks-core",
    libraryDependencies ++= commonDeps
  )

lazy val json = project
  .in(file("swagger-blocks-json"))
  .dependsOn(core % "compile;test->test")
  .settings(commonSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    name := "swagger-blocks-json",
    libraryDependencies ++= commonDeps ++ List(
      Lib.circeCore,
      Lib.circeGeneric,
      Lib.circeParser
    )
  )

lazy val yaml = project
  .in(file("swagger-blocks-yaml"))
  .dependsOn(core % "compile;test->test")
  .settings(commonSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    name := "swagger-blocks-yaml",
    libraryDependencies ++= commonDeps ++ List(
      Lib.circeCore,
      Lib.circeGeneric,
      Lib.circeParser,
      Lib.circeYaml
    )
  )

lazy val examples = project
  .in(file("examples"))
  .dependsOn(core, json, yaml)
  .settings(commonSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    name := "swagger-blocks-examples",
    publishArtifact := false,
    publish := {},
    publishLocal := {},
    packagedArtifacts := Map.empty
  )

initialCommands in console in core in Test :=
  """
    |import fixtures.generators._
    |import org.scalacheck._
  """.stripMargin

scalacOptions in Global ++= List(
  "-deprecation",
  "-encoding",
  "UTF-8", // yes, this is 2 args
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint:-missing-interpolator",
  "-Yno-adapted-args",
  "-Ywarn-numeric-widen",
  //  "-Ywarn-value-discard",
  "-Xfuture"
)

lazy val publishSettings = List(
  publishMavenStyle := true,
  publishArtifact in Test := false,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra :=
    <scm>
      <url>git@github.com:felixbr/swagger-blocks-scala.git</url>
      <connection>scm:git:git@github.com:felixbr/swagger-blocks-scala.git</connection>
    </scm>
    <developers>
      <developer>
        <id>felixbr</id>
        <name>Felix Bruckmeier</name>
        <url>https://github.com/felixbr</url>
      </developer>
    </developers>
)
