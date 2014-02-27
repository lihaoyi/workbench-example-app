addSbtPlugin("org.scala-lang.modules.scalajs" % "scalajs-sbt-plugin" % "0.3")

lazy val root = project.in(file(".")).dependsOn(
  uri("https://github.com/lihaoyi/workbench.git")
)