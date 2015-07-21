// Turn this project into a Scala.js project by importing these settings

import sbt.Keys._
import com.lihaoyi.workbench.Plugin._
import spray.revolver.AppProcess
import spray.revolver.RevolverPlugin.Revolver

val example = crossProject.settings(
  scalaVersion := "2.11.7",
  version := "0.1-SNAPSHOT",
  libraryDependencies ++= Seq(
    "com.lihaoyi" %%% "upickle"   % "0.2.6",
    "com.lihaoyi" %%% "autowire"  % "0.2.4",
    "com.lihaoyi" %%% "scalatags" % "0.4.5"
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
    "io.spray"          %% "spray-can"     % "1.3.3",
    "io.spray"          %% "spray-routing" % "1.3.3",
    "com.typesafe.akka" %% "akka-actor"    % "2.3.11",
    "org.webjars.bower"  % "bootstrap"     % "3.3.5"
  )
)

val exampleJS = example.js
val exampleJVM = example.jvm.settings(
  (resources in Compile) ++= {
    def andSourceMap(aFile: java.io.File) = Seq(
      aFile,
      file(aFile.getAbsolutePath + ".map")
    )
    andSourceMap((fastOptJS in (exampleJS, Compile)).value.data)
  }
)