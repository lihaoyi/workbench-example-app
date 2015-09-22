package example
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import scala.util.Random
import scala.concurrent.Future
import scalajs.concurrent.JSExecutionContext.Implicits.runNow
import scalatags.JsDom.all._
import upickle.default._
import upickle.Js
import autowire._

object Client extends autowire.Client[Js.Value, Reader, Writer]{
  override def doCall(req: Request): Future[Js.Value] = {
    dom.ext.Ajax.post(
      url = "/api/" + req.path.mkString("/"),
      data = upickle.json.write(Js.Obj(req.args.toSeq:_*))
    ).map(_.responseText)
     .map(upickle.json.read)
  }

  def read[Result: Reader](p: Js.Value) = readJs[Result](p)
  def write[Result: Writer](r: Result) = writeJs(r)
}


@JSExport
object ScalaJSExample {
  @JSExport
  def main(): Unit = {
    
    val inputBox = input.render
    val outputBox = div.render

    def updateOutput() = {
      Client[Api].list(inputBox.value).call().foreach { paths =>
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
