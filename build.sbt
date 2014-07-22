// Turn this project into a Scala.js project by importing these settings
import scala.scalajs.sbtplugin.ScalaJSPlugin._
import scala.scalajs.sbtplugin._
import ScalaJSKeys._
import com.lihaoyi.workbench.Plugin._

scalaJSSettings

workbenchSettings

scalaVersion := "2.11.1"

name := "Example"

version := "0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.scala-lang.modules.scalajs" %%% "scalajs-dom" % "0.6",
  "com.scalatags" %%% "scalatags" % "0.3.8",
  "com.scalarx" %%% "scalarx" % "0.2.5"
)

bootSnippet := "ScalaJSExample().main()"

updateBrowsers <<= updateBrowsers.triggeredBy(ScalaJSKeys.fastOptJS in Compile)
