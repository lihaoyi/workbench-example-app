resolvers += Resolver.url("scala-js-snapshots",
  url("http://repo.scala-js.org/repo/snapshots/"))(
    Resolver.ivyStylePatterns)

addSbtPlugin("org.scala-lang.modules.scalajs" % "scalajs-sbt-plugin" % "0.3")

lazy val root = project.in(file(".")).dependsOn(
  uri("https://github.com/lihaoyi/workbench.git")
)