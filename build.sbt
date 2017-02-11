val commonSettings = Seq(
  Quiet.silenceScalaBinaryVersionWarning,
  scalaVersion := Version.scala_2_11,
  crossScalaVersions := Version.crossVersions,
  version := "0.4.0",
  homepage := Some(url("https://github.com/felixbr/swagger-blocks-scala")),
  organization := "io.github.felixbr",
  licenses := List(
    "MIT License" -> url("http://www.opensource.org/licenses/mit-license.php")
  ),
  scalafmtConfig := Some(file(".scalafmt.conf"))
)

val commonDeps = Seq(
  Lib.scalaTest           % "test",
  Lib.jsonSchemaValidator % "test",
  Lib.scalacheck          % "test",
  Lib.scalacheckShapeless % "test"
)

lazy val root = project
  .in(file("."))
  .aggregate(core, play, yaml, circe)
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
    name := "swagger-blocks-scala",
    libraryDependencies ++= commonDeps
  )

lazy val play = project
  .in(file("swagger-blocks-play"))
  .dependsOn(core % "compile;test->test")
  .settings(commonSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    name := "swagger-blocks-play",
    libraryDependencies <++= scalaVersion { (sv) =>
      commonDeps ++ List(
        if (sv == Version.scala_2_11) Lib.playJson_2_11 else Lib.playJson
      )
    },
    crossScalaVersions := List(Version.scala_2_11)
  )

lazy val circe = project
  .in(file("swagger-blocks-circe"))
  .dependsOn(core % "compile;test->test")
  .settings(commonSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    name := "swagger-blocks-circe",
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
      Lib.moultingYaml
    )
  )

lazy val examples = project
  .in(file("examples"))
  .dependsOn(core, play, yaml, circe)
  .settings(commonSettings: _*)
  .settings(
    name := "swagger-blocks-examples",
    publishArtifact := false,
    publish := {},
    publishLocal := {},
    libraryDependencies ++= List(
      Lib.play
    ),
    crossScalaVersions := List(Version.scala_2_11)
  )

cancelable in Global := true

initialCommands in console in core in Test :=
  """
    |import fixtures.generators._
    |import org.scalacheck._
  """.stripMargin

scalacOptions in Global ++= Seq(
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

lazy val publishSettings = Seq(
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
