// Turn this project into a Scala.js project by importing these settings

import sbt.Keys._
import scala.scalajs.sbtplugin.ScalaJSPlugin._
import ScalaJSKeys._
import com.lihaoyi.workbench.Plugin._

val client = project.in(file("client")).settings(scalaJSSettings ++workbenchSettings:_*).settings(
  name := "Example",
  scalaVersion := "2.11.2",
  version := "0.1-SNAPSHOT",
  resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  libraryDependencies ++= Seq(
    "org.scala-lang.modules.scalajs" %%% "scalajs-dom" % "0.6"
  ),
  bootSnippet := "ScalaJSExample().main();",
  updateBrowsers <<= updateBrowsers.triggeredBy(ScalaJSKeys.fastOptJS in Compile)
)

