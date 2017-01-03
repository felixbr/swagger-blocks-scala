val commonSettings = Seq(
  version := "0.3.0",
  scalaVersion := Version.scala,
  homepage := Some(url("https://github.com/felixbr/swagger-blocks-scala")),
  organization := "io.github.felixbr",
  licenses := List(
    "MIT License" -> url("http://www.opensource.org/licenses/mit-license.php")
  ),
  scalafmtConfig in ThisBuild := Some(file(".scalafmt.conf"))
)

val commonDeps = Seq(
  Lib.scalaTest           % "test",
  Lib.jsonSchemaValidator % "test",
  Lib.scalacheck          % "test",
  Lib.scalacheckShapeless % "test"
)

lazy val root = project
  .in(file("."))
  .aggregate(core, play, yaml)
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
    libraryDependencies ++= commonDeps ++ List(
      Lib.playJson
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
  .dependsOn(play, yaml, core)
  .settings(commonSettings: _*)
  .settings(
    name := "swagger-blocks-examples",
    publishArtifact := false,
    publish := {},
    publishLocal := {},
    libraryDependencies ++= List(
      Lib.play
    )
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
