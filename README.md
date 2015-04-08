# Example Scala.js application 

This is a slightly less barebone example of an application written in
[Scala.js](http://www.scala-js.org/). In particular, it links
in libraries that are indispensible in being productive working with Scala.js.

## Get started

To get started, run `sbt ~fastOptJS` in this example project. This should
download dependencies and prepare the relevant javascript files. If you open
`localhost:12345/target/scala-2.11/classes/index-dev.html` in your browser, it will show you an animated [Sierpinski
triangle](http://en.wikipedia.org/wiki/Sierpinski_triangle) ([live demo](http://lihaoyi.github.io/workbench-example-app/triangle.html)). You can then
edit the application and see the updates be sent live to the browser
without needing to refresh the page.

## The optimized version

Run `sbt fullOptJS` and open up `index-opt.html` for an optimized (~200kb) version
of the final application, useful for final publication.

## Dodge the Dots

Take a look at the `dodge-the-dots` branch in the git repository if you
want to see a slightly more complex application that was made in 30 minutes
using this skeleton. Similar steps can be used for development or 
publication. There's a live demo [here](http://lihaoyi.github.io/workbench-example-app/dodge.html).

## Space Invaders

There's also a `space-invaders` branch, also made in 30 minutes, with its own
[live demo](http://lihaoyi.github.io/workbench-example-app/invaders.html).

## TodoMVC

The `todomvc` branch contains an implementation of the [TodoMVC example application](http://todomvc.com/), which is used to compare how the exact same application would be implemented using different languages and frameworks. This application makes heavy use of [Scalatags](https://github.com/lihaoyi/scalatags) and [Scala.Rx](https://github.com/lihaoyi/scala.rx), with heavy use of the DOM via [scala-js-dom](https://github.com/scala-js/scala-js-dom), making it a good example of how reactive web pages could be built using ScalaJS, as well as use of standalone ScalaJS libraries. [live demo](http://lihaoyi.github.io/workbench-example-app/todo.html)

## Ray Tracer

The `ray-tracer` branch contains a simple ray-tracer written using Scala.js, with a [live demo](http://lihaoyi.github.io/workbench-example-app/raytracer.html).

## Weather Map

The `mapper` branch contains an application that renders a heat map of the earth, using data pulled from OpenWeatherMap, with a [live demo](http://lihaoyi.github.io/workbench-example-app/map.html)

## Autowire

The `autowire` branch contains a small sample client-server application, using Scala.js on the client and Spray on the server. The readme on that branch contains instructions on how to run it.
