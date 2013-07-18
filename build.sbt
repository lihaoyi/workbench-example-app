// Turn this project into a Scala.js project by importing these settings
scalaJSSettings

name := "Example"

version := "0.1-SNAPSHOT"

// The following is for optimize-js (this should be simpler in the future)
unmanagedSources in (Compile, ScalaJSKeys.optimizeJS) <++= (
    baseDirectory
) map { base =>
  Seq(base / "js" / "scalajs-runtime.js", base / "js" / "startup.js")
}
