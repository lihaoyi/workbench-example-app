package example
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import scala.concurrent.Future
import scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scalatags.JsDom.all._
import upickle.default._
import upickle.Js

object ClientApi extends Api {
  def list(path: String): Future[Seq[String]] = {
    dom.ext.Ajax.post(
      url = "/api/" + listEndpoint,
      data = upickle.json.write(writeJs(Js.Obj("path" -> Js.Str(path))))
    ).map(_.responseText)
     .map(upickle.json.read)
     .map(readJs[Seq[String]])
  }
}

@JSExport
object ScalaJSExample {
  @JSExport
  def main(): Unit = {
    
    val inputBox = input.render
    val outputBox = div.render

    def updateOutput() = {
      ClientApi.list(inputBox.value).foreach { paths =>
        outputBox.innerHTML = ""
        outputBox.appendChild(
          ul(
            for(path <- paths) yield {
              li(path)
            }
          ).render
        )
      }
    }
    inputBox.onkeyup = {(e: dom.Event) =>
      updateOutput()
    }
    updateOutput()
    dom.document.body.appendChild(
      div(
        cls:="container",
        h1("File Browser"),
        p("Enter a file path to s"),
        inputBox,
        outputBox
      ).render
    )
  }
}
