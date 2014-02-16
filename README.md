# Example Scala.js application 

This is a slightly less barebone example of an application written in
[Scala.js](https://github.com/lampepfl/scala-js). In particular, it links
in libraries that are indispensible in being productive working with Scala.js.

## Get started

To get started, run `sbt ~packageJS` in this example project. This should
download dependencies and prepare the relevant javascript files. If you open
`index-dev.html` in your browse, it will show you an animated [Sierpinski
triangl](http://en.wikipedia.org/wiki/Sierpinski_triangle) ([live demo](http://lihaoyi.github.io/workbench-example-app/triangle.html)). You can then
edit the application and see the updates be sent live to the browser
without needing to refresh the page.

## The optimized version

Run `sbt optimizeJS` and open up `index.html` for an optimized (~200kb) version
of the final application.

## Dodge the Dots

Take a look at the `dodge-the-dots` branch in the git repository if you
want to see a slightly more complex application that was made in 30 minutes
using this skeleton. Similar steps can be used for development (`sbt ~packageJS`) or 
publication (`sbt optimizeJS`). There's a live demo [here](http://lihaoyi.github.io/workbench-example-app/dodge.html).
