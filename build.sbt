// Turn this project into a Scala.js project by importing these settings

import sbt.Keys._
import scala.scalajs.sbtplugin.ScalaJSPlugin._
import ScalaJSKeys._
import com.lihaoyi.workbench.Plugin._
import spray.revolver.AppProcess
import spray.revolver.RevolverPlugin.Revolver

val cross = new utest.jsrunner.JsCrossBuild(
  scalaVersion := "2.11.2",
  version := "0.1-SNAPSHOT",
  libraryDependencies ++= Seq(
    "com.lihaoyi" %%% "upickle" % "0.2.5",
    "com.lihaoyi" %%% "autowire" % "0.2.3",
    "com.scalatags" %%% "scalatags" % "0.4.2"
  )
)
val client = cross.js.in(file("client"))
                    .copy(id="client")
                    .settings(scalaJSSettings ++workbenchSettings:_*)
                    .settings(
  name := "Client",
  libraryDependencies ++= Seq(
    "org.scala-lang.modules.scalajs" %%% "scalajs-dom" % "0.6"
  ),
  bootSnippet := "ScalaJSExample().main();"
)

val server = cross.jvm.in(file("server"))
                    .copy(id="server")
                    .settings(Revolver.settings:_*)
                    .settings(
  name := "Server",
  libraryDependencies ++= Seq(
    "io.spray" %% "spray-can" % "1.3.1",
    "io.spray" %% "spray-routing" % "1.3.1",
    "com.typesafe.akka" %% "akka-actor" % "2.3.2",
    "org.webjars" % "bootstrap" % "3.2.0"
  ),
  (resources in Compile) += {
    (fastOptJS in (client, Compile)).value
    (artifactPath in (client, Compile, fastOptJS)).value
  }
)
