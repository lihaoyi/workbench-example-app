// Turn this project into a Scala.js project by importing these settings
import com.lihaoyi.workbench.Plugin._

enablePlugins(ScalaJSPlugin)

workbenchSettings

name := "Example"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "org.scala-js" %%% "scalajs-dom" % "0.8.0",
  "org.scala-lang.modules" %% "scala-async" % "0.9.1",
  "com.nativelibs4java" %% "scalaxy-loops" % "0.1.1" % "provided"
)

resolvers += Resolver.sonatypeRepo("snapshots")

bootSnippet := "example.ScalaJSExample().main();"

updateBrowsers <<= updateBrowsers.triggeredBy(fullOptJS in Compile)
