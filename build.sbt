// Turn this project into a Scala.js project by importing these settings
scalaJSSettings

name := "Example"

version := "0.1-SNAPSHOT"

// Specify additional .js file to be passed to optimize-js
unmanagedSources in (Compile, ScalaJSKeys.optimizeJS) <++= (
    baseDirectory
) map { base =>
  Seq(base / "js" / "startup.js")
}
