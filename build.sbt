// Turn this project into a Scala.js project by importing these settings
import scala.scalajs.sbtplugin.ScalaJSPlugin._
import ScalaJSKeys._
import com.lihaoyi.workbench.Plugin._

scalaJSSettings

workbenchSettings

name := "Example"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  "org.scala-lang.modules.scalajs" %%% "scalajs-dom" % "0.6",
  "org.scala-lang.modules" %% "scala-async" % "0.9.1",
  "com.nativelibs4java" %% "scalaxy-loops" % "0.1.1" % "provided"
)

resolvers += Resolver.sonatypeRepo("snapshots")

bootSnippet := "ScalaJSExample().main();"

updateBrowsers <<= updateBrowsers.triggeredBy(ScalaJSKeys.packageJS in Compile)
