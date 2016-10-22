val commonSettings = Seq(
  version := "0.1.1",
  scalaVersion := Version.scala,
  homepage := Some(url("https://github.com/felixbr/swagger-blocks-scala")),
  organization := "io.github.felixbr",
  licenses := List("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php"))
)

val commonDeps = Seq(
  Lib.scalaTest % "test",
  Lib.jsonSchemaValidator % "test"
)

lazy val core = project.in(file("."))
  .settings(commonSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    name := "swagger-blocks-scala",
    libraryDependencies ++= commonDeps
  )

lazy val play = project.in(file("swagger-blocks-play"))
  .dependsOn(core)
  .settings(commonSettings: _*)
  .settings(publishSettings: _*)
  .settings(
    name := "swagger-blocks-play",
    libraryDependencies ++= commonDeps ++ List(
      Lib.playJson
    )
  )

lazy val examples = project.in(file("examples"))
    .dependsOn(play, core)
    .settings(commonSettings: _*)
    .settings(
      name := "swagger-blocks-examples"
    )

cancelable in Global := true

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8", // yes, this is 2 args
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
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
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  }
)

