package example

import js.Dynamic.{ global => g }

object ScalaJSExample {
  def main(): Unit = {
    val paragraph = g.document.createElement("p")
    paragraph.updateDynamic("innerHTML")("<strong>It works!</strong>")
    g.document.getElementById("playground").appendChild(paragraph)
  }
}
