import sbt._

object Version {
  final val akka      = "2.4.8"
  final val play      = "2.5.4"
  final val scala     = "2.11.8"
  final val scalaTest = "2.2.6"
  final val uPickle   = "0.4.1"
}

object Lib {
  val akkaHttp  = "com.typesafe.akka" %% "akka-http-experimental" % Version.akka
  val playJson  = "com.typesafe.play" %% "play-json"              % Version.play
  val scalaTest = "org.scalatest"     %% "scalatest"              % Version.scalaTest
  val upickle   = "com.lihaoyi"       %% "upickle"                % Version.uPickle
}