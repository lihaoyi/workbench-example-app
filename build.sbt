import com.lihaoyi.workbench.Plugin._

workbenchSettings

lazy val example = (project in file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    name := "Example",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.11.7",
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.8.2",
      "com.lihaoyi" %%% "scalatags" % "0.5.4"
    )
  )

bootSnippet := "example.ScalaJSExample().main(document.getElementById('canvas'));"

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)

