# upickle/Workbench example

This is a small application which demonstrates client-server communication. It
comprises an Akka-http server and Scala.js web client talking to each other via
Ajax, using [upickle](http://www.lihaoyi.com/upickle-pprint/upickle/) for
type-safe serialization. To start, run

```
sbt ~re-start
```

And go to `localhost:8080`
