// Turn this project into a Scala.js project by importing these settings

import sbt.Keys._
import spray.revolver.AppProcess
import spray.revolver.RevolverPlugin.Revolver

enablePlugins(WorkbenchPlugin)

scalaVersion := "2.11.8"

val example = crossProject.settings(
  scalaVersion := "2.11.8",
  version := "0.1-SNAPSHOT",
  libraryDependencies ++= Seq(
    "com.lihaoyi" %%% "upickle" % "0.4.3",
    "com.lihaoyi" %%% "autowire" % "0.2.6",
    "com.lihaoyi" %%% "scalatags" % "0.6.1"
  )
).jsSettings(
  name := "Client",
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.1"
  )
).jvmSettings(
  Revolver.settings:_*
).jvmSettings(
  name := "Server",
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http-experimental" % "2.4.11",
    "com.typesafe.akka" %% "akka-actor" % "2.4.12",
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
