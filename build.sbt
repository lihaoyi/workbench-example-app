// Turn this project into a Scala.js project by importing these settings

import sbt.Keys._
import scala.scalajs.sbtplugin.ScalaJSPlugin._
import ScalaJSKeys._
import com.lihaoyi.workbench.Plugin._
import spray.revolver.AppProcess
import spray.revolver.RevolverPlugin.Revolver

val shared = project.in(file("shared"))
                    .settings(scalaJSSettings:_*)
                    .settings(
  scalaVersion := "2.11.2",
  version := "0.1-SNAPSHOT"
)

val client = project.in(file("client"))
                    .settings(scalaJSSettings ++workbenchSettings:_*)
                    .dependsOn(shared)
                    .settings(
  name := "Client",
  scalaVersion := "2.11.2",
  version := "0.1-SNAPSHOT",
  resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  libraryDependencies ++= Seq(
    "org.scala-lang.modules.scalajs" %%% "scalajs-dom" % "0.6",
    "com.lihaoyi" %%% "upickle" % "0.2.2",
    "com.lihaoyi" %%% "autowire" % "0.2.1",
    "com.scalatags" %%% "scalatags" % "0.4.0"
  ),
  bootSnippet := "ScalaJSExample().main();"
)

val server = project.in(file("server"))
                    .dependsOn(shared)
                    .settings(Revolver.settings:_*)
                    .settings(
  name := "Server",
  scalaVersion := "2.11.2",
  version := "0.1-SNAPSHOT",
  libraryDependencies ++= Seq(
    "io.spray" %% "spray-can" % "1.3.1",
    "io.spray" %% "spray-routing" % "1.3.1",
    "com.lihaoyi" %% "upickle" % "0.2.2",
    "com.lihaoyi" %% "autowire" % "0.2.1",
    "com.typesafe.akka" %% "akka-actor" % "2.3.2",
    "com.scalatags" %% "scalatags" % "0.4.0",
    "org.webjars" % "bootstrap" % "3.2.0"
  ),
  (resources in Compile) += {
    (fastOptJS in (client, Compile)).value
    (artifactPath in (client, Compile, fastOptJS)).value
  }
)
