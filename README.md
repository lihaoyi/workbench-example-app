# Example application written in Scala.js

This is a barebone example of an application written in
[Scala.js](https://github.com/lampepfl/scala-js).

## Get started

To get started, first make the Scala.js compiler, library and sbt plugin
available locally by publishing them locally. This is very easy, just follow
the instructions in the [Scala.js readme](https://github.com/lampepfl/scala-js).

Then, copy the Scala.js runtime into the `js/` subdirectory of this project.
You will find it in the subdirectory `target/` of Scala.js, it is named
`scalajs-runtime.js`. Optionally, also copy the source map
`scalajs-runtime.js.map`.

Now, you are good to go. Open `sbt` in this example project, and issue the
task `package-js`. This creates the file `target/example.js`. You can now
open `index.html` in your favorite Web browser!

During development, it is useful to use `~package-js` in sbt, so that each
time you save a source file, a compilation of the project is triggered.
Hence only a refresh of your Web page is needed to see the effects of your
changes.

Note that Scala.js and sbt do not play well enough together for incremental
compilation to work. So `package-js` will recompile all your source files
everytime.

## Troubleshooting

### I don't find `scalajs-runtime.js` Scala.js

You have probably forgotten to execute `package-js` from the sbt prompt in
Scala.js.

### I have unresolved dependencies on `scalajs-...`

You have probably forgotten to execute `publish-local` from the sbt prompt in
Scala.js.
