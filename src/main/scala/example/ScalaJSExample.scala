package example

import scala.scalajs.js
import js.Dynamic.{ global => g }

object ScalaJSExample {
  def main(): Unit = {
    val paragraph = g.document.createElement("p")
    paragraph.innerHTML = "<strong>It works!</strong>"
    g.document.getElementById("playground").appendChild(paragraph)
  }
}
