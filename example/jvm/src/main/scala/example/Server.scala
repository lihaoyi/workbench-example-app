package example
import upickle.default._
import upickle.Js
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
      body(margin:=0)(
        script("example.ScalaJSExample().main()")
      )
    )
}
object AutowireServer extends autowire.Server[Js.Value, Reader, Writer]{
  def read[Result: Reader](p: Js.Value) = upickle.default.readJs[Result](p)
  def write[Result: Writer](r: Result) = upickle.default.writeJs(r)
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
              AutowireServer.route[Api](Server)(
                autowire.Core.Request(
                  s,
                  upickle.json.read(e).asInstanceOf[Js.Obj].value.toMap
                )
              ).map(upickle.json.write)
            }
          }
        }
      }
    }
  }

  def list(path: String) = {
    val chunks = path.split("/", -1)
    val prefix = "./" + chunks.dropRight(1).mkString("/")
    val files = Option(new java.io.File(prefix).list()).toSeq.flatten
    files.filter(_.startsWith(chunks.last))
  }
}
