package example
import upickle._
import spray.routing.SimpleRoutingApp
import akka.actor.ActorSystem
import scala.concurrent.ExecutionContext.Implicits.global
import spray.http.{MediaTypes, HttpEntity}

object Template{
  import scalatags.Text.all._
  import scalatags.Text.tags2.title
  val txt =
    "<!DOCTYPE html>" +
    html(
      head(
        title("Example Scala.js application"),
        meta(httpEquiv:="Content-Type", content:="text/html; charset=UTF-8"),
        script(`type`:="text/javascript", src:="/client-fastopt.js"),
        script(`type`:="text/javascript", src:="//localhost:12345/workbench.js"),
        link(
          rel:="stylesheet",
          `type`:="text/css",
          href:="META-INF/resources/webjars/bootstrap/3.2.0/css/bootstrap.min.css"
        )
      ),
      body(margin:=0, onload:="ScalaJSExample().main()")
    )
}
object Server extends SimpleRoutingApp with Api{
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    startServer("0.0.0.0", port = 8080) {
      get{
        pathSingleSlash {
          complete{
            HttpEntity(
              MediaTypes.`text/html`,
              Template.txt
            )
          }
        } ~
        getFromResourceDirectory("")
      } ~
      post {
        path("api" / Segments){ s =>
          extract(_.request.entity.asString) { e =>
            complete {
              val res = autowire.Macros.route[Api](Server)(
                autowire.Request(s, upickle.read[Map[String, String]](e))
              )
              res.foreach(r => println("RES::: " + r))
              res
            }
          }
        }
      }
    }
  }

  def list(path: String): Seq[String] = {
    val chunks = path.split("/", -1)
    val prefix = "./" + chunks.dropRight(1).mkString("/")
    val files = Option(new java.io.File(prefix).list()).toSeq.flatten
    files.filter(_.startsWith(chunks.last))
  }
}
