// Turn this project into a Scala.js project by importing these settings
import com.lihaoyi.workbench.Plugin._

enablePlugins(ScalaJSPlugin)

workbenchSettings

scalaVersion := "2.11.8"

name := "Example"

version := "0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.1",
  "com.lihaoyi" %%% "scalatags" % "0.6.1",
  "com.lihaoyi" %%% "scalarx" % "0.2.8"
)

bootSnippet := "example.ScalaJSExample().main()"

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)
