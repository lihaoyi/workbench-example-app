resolvers += Resolver.url("scala-js-snapshots",
    url("http://repo.scala-js.org/repo/snapshots/"))(Resolver.ivyStylePatterns)

addSbtPlugin("org.scala-lang.modules.scalajs" % "scalajs-sbt-plugin" % "0.1-SNAPSHOT")
