import sbt._

object Version {
  final val Akka      = "2.4.8"
  final val Play      = "2.5.4"
  final val Scala     = "2.11.8"
  final val ScalaTest = "2.2.6"
  final val Upickle   = "0.4.1"
}

object Lib {
  val akkaHttp      = "com.typesafe.akka" %% "akka-http-experimental" % Version.Akka
  val playJson      = "com.typesafe.play" %% "play-json"              % Version.Play
  val scalaTest     = "org.scalatest"     %% "scalatest"              % Version.ScalaTest
  val upickle       = "com.lihaoyi"       %% "upickle"                % Version.Upickle
}