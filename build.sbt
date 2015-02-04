// Turn this project into a Scala.js project by importing these settings
import com.lihaoyi.workbench.Plugin._

enablePlugins(ScalaJSPlugin)

workbenchSettings

scalaVersion := "2.11.4"

name := "Example"

version := "0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.8.0",
  "com.lihaoyi" %%% "scalatags" % "0.4.5",
  "com.lihaoyi" %%% "scalarx" % "0.2.7"
)

bootSnippet := "example.ScalaJSExample().main()"

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)
