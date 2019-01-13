// Turn this project into a Scala.js project by importing these settings
enablePlugins(ScalaJSPlugin, WorkbenchPlugin)

scalaVersion := "2.12.7"

name := "Example"

version := "0.1-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.9.6",
  "com.lihaoyi" %%% "scalatags" % "0.6.7",
  "com.lihaoyi" %%% "scalarx" % "0.4.0"
)
