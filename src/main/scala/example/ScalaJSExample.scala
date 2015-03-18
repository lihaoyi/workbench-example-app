package example

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import org.scalajs.dom.html
import scala.util.Random
import scalajs.concurrent.JSExecutionContext.Implicits.queue
import org.scalajs.dom.ext
case class Point(x: Int, y: Int){
  def +(p: Point) = Point(x + p.x, y + p.y)
  def /(d: Int) = Point(x / d, y / d)
}

@JSExport
object ScalaJSExample {
  @JSExport
  def main(canvas: html.Canvas): Unit = {
    val ctx = canvas.getContext("2d")
                    .asInstanceOf[dom.CanvasRenderingContext2D]

    canvas.width = dom.innerWidth
    canvas.height = dom.innerHeight

    ctx.fillStyle = "black"
    ctx.fillRect(0, 0, 10000, 20000)
    val prefixes = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val allPrefixes = for{
      a <- prefixes
      b <- prefixes
      c <- prefixes
    } yield s"$a$b$c"
    var i = 0

    def run(prefix: String) = {
      i += 1
      println(i + "\t" +  allPrefixes.length)
      val fut = ext.Ajax.get(s"http://api.openweathermap.org/data/2.5/find?q=$prefix&type=like&mode=json")
      fut.foreach{ xhr =>
//        println(xhr.responseText)
//        println(prefix + "\t" + xhr.responseText.length)
        val parsed = js.JSON.parse(xhr.responseText)
//        dom.console.log(parsed)
        parsed.list.map{ el: js.Dynamic =>
          val x = el.coord.lon.asInstanceOf[Double]
          val y = el.coord.lat.asInstanceOf[Double]
          val t = el.main.temp.asInstanceOf[Double] // 250 -> 350
          val screenX =  (x / 180 + 1) / 2 * canvas.width
          val screenY = canvas.height - (y / 90 + 1) / 2 * canvas.height

          val tScaled = ((t - 260) / 50 * 255).toInt // 0 - 255
          val (r, g, b) = color(tScaled)
          println(t)
          ctx.fillStyle = s"rgb($r, $g, $b)"
          ctx.fillRect(screenX - 2, screenY - 2, 4, 4)
        }

      }
    }
    def color(tScaled: Int) = { // 0 -> 255
      val r = math.max(tScaled - 128, 0) * 2
      val g = (128 - math.abs(tScaled - 128)) * 2
      val b = math.max(128 - tScaled, 0) * 2
      (r, g, b)
    }
//    println(color(0))
//    println(color(128))
//    println(color(255))
    dom.setInterval(() => run(allPrefixes(i)), 10)
  }
}
