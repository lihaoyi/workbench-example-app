resolvers += Resolver.url("scala-js-releases",
  url("http://dl.bintray.com/content/scala-js/scala-js-releases"))(
    Resolver.ivyStylePatterns)

addSbtPlugin("org.scala-lang.modules.scalajs" % "scalajs-sbt-plugin" % "0.5.0-M3")

resolvers += "spray repo" at "http://repo.spray.io"

resolvers += "typesafe" at "http://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.lihaoyi" % "workbench" % "0.1.2")