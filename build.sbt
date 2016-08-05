val commonSettings = Seq(
  version := "0.1",
  scalaVersion := Version.scala
)

lazy val core = project.in(file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "swagger-blocks-scala",
    libraryDependencies ++= List(
      Lib.scalaTest % "test"
    )
  )

lazy val play = project.in(file("swagger-blocks-play"))
  .dependsOn(core)
  .settings(commonSettings: _*)
  .settings(
    name := "swagger-blocks-play",
    libraryDependencies ++= List(
      Lib.playJson
    )
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