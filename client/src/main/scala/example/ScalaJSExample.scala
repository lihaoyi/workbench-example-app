package example
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import scala.util.Random
import scala.concurrent.Future
import scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scalatags.JsDom.all._
import upickle._
object Ajax extends autowire.Client[Api]{
  override def callRequest(req: autowire.Request): Future[String] = {
    val url = "/api/" + req.path.mkString("/")

    dom.extensions.Ajax.post(
      url = url,
      data = upickle.write(req.args)
    ).map(_.responseText)
  }
}


@JSExport
object ScalaJSExample {
  @JSExport
  def main(): Unit = {
    
    val inputBox = input.render
    val outputBox = div.render

    def updateOutput() = {
      Ajax(_.list(inputBox.value)).foreach { paths =>
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
