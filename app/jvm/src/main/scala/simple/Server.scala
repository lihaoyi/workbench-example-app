package simple

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Properties

object Server{
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    val port = Properties.envOrElse("PORT", "8080").toInt
    val route = {
      get{
        pathSingleSlash{
          complete{
            HttpEntity(
              ContentTypes.`text/html(UTF-8)`,
              Page.skeleton.render
            )
          }
        } ~
        getFromResourceDirectory("")
      } ~
      post{
        path("ajax" / "list"){
          entity(as[String]) { e =>
            complete {
              upickle.default.write(list(e))
            }
          }
        }
      }
    }
    Http().bindAndHandle(route, "0.0.0.0", port = port)
  }
  def list(path: String) = {
    val (dir, last) = path.splitAt(path.lastIndexOf("/") + 1)
    val files =
      Option(new java.io.File("./" + dir).listFiles())
        .toSeq.flatten
    for{
      f <- files
      if f.getName.startsWith(last)
    } yield FileData(f.getName, f.length())
  }
}