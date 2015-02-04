// Turn this project into a Scala.js project by importing these settings

import sbt.Keys._
import com.lihaoyi.workbench.Plugin._
import spray.revolver.AppProcess
import spray.revolver.RevolverPlugin.Revolver

val example = crossProject.settings(
  scalaVersion := "2.11.4",
  version := "0.1-SNAPSHOT",
  libraryDependencies ++= Seq(
    "com.lihaoyi" %%% "upickle" % "0.2.6",
    "com.lihaoyi" %%% "autowire" % "0.2.4",
    "com.lihaoyi" %%% "scalatags" % "0.4.4"
  )
).jsSettings(
  workbenchSettings:_*
).jsSettings(
  name := "Client",
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.0"
  ),
  bootSnippet := "example.ScalaJSExample().main();"
).jvmSettings(
  Revolver.settings:_*
).jvmSettings(
  name := "Server",
  libraryDependencies ++= Seq(
    "io.spray" %% "spray-can" % "1.3.1",
    "io.spray" %% "spray-routing" % "1.3.1",
    "com.typesafe.akka" %% "akka-actor" % "2.3.2",
    "org.webjars" % "bootstrap" % "3.2.0"
  )
)

val exampleJS = example.js
val exampleJVM = example.jvm.settings(
  (resources in Compile) += {
    (fastOptJS in (exampleJS, Compile)).value
    (artifactPath in (exampleJS, Compile, fastOptJS)).value
  }
)