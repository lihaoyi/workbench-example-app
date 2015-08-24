package example

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import org.scalajs.dom.html
import scala.util.Random

case class Vec2D(x: Double, y: Double){
  def +(p: Vec2D) = Vec2D(x + p.x, y + p.y)
  def -(p: Vec2D) = Vec2D(x - p.x, y - p.y)
  def /(d: Double) = Vec2D(x / d, y / d)
  def *(d: Double) = Vec2D(x * d, y * d)
}

@JSExport
object ScalaJSExample {
  @JSExport
  def main(canvas: html.Canvas): Unit = {
    val ctx = canvas.getContext("2d")
                    .asInstanceOf[dom.CanvasRenderingContext2D]
    ctx.fillStyle = "black"
    canvas.width = 1000
    canvas.height = 1000
    val canvasHeight = 1000
    ctx.fillRect(0, 0, canvasHeight, canvasHeight)

    def drawLine(color: js.Any, pts: Vec2D*) = {
      println(pts)
      ctx.strokeStyle = color
      ctx.fillStyle = color

      ctx.beginPath()
      ctx.moveTo(pts(0).x.toInt + canvasHeight/2, pts(0).y.toInt + canvasHeight/2)
      for(pt <- pts) ctx.lineTo(pt.x.toInt + canvasHeight/2, pt.y.toInt + canvasHeight/2)
      ctx.closePath()
      ctx.fill()

    }

    val startCanvasRatio = 0.10
    val segmentsPer360 = 20
    val startAngle = -45
    val totalAngle = 1440
    val numSegments = segmentsPer360 * totalAngle / 360
    val degreesPerSegment = 360 / segmentsPer360
    val segmentGapFraction = 0.2
    val growth = 1.5
    val growthPerDegree = math.pow(growth, 1.0 / 360)
    val shearRatio = 0.3
    def offset(degrees: Double) = {
      val radius = canvasHeight/2 * math.pow(growthPerDegree, degrees) * startCanvasRatio
      val radians = (degrees + startAngle) * math.Pi / 180
      Vec2D(
        (math.cos(radians) * radius).toInt,
        (math.sin(radians) * radius).toInt
      )
    }
    ctx.lineWidth = 6


    def doLoop(color: (Int, Vec2D, Vec2D) => js.Any, myOffset: Int) = {
      for(step <- 0 until numSegments){
        def makePoints(degrees: Double) = {
          val startPoint = offset(degrees)
          val endPoint = offset(degrees + degreesPerSegment)
          (startPoint, endPoint, (endPoint - startPoint) * segmentGapFraction)
        }


        throw new Exception("HALLOOOO")
        val (start, end, gap) = makePoints(step * degreesPerSegment + myOffset)
        val (start2, end2, gap2) = makePoints(step * degreesPerSegment + degreesPerSegment * shearRatio)

        drawLine(
          color(step, start, start2 * growth),
          start,
          end - gap,
          (end2 - gap2) * growth,
          start2 * growth
        )
      }
    }
    doLoop((_, _, _) => "grey", degreesPerSegment)
    doLoop(
      (step, p1, p2)=> {
        val gradient = ctx.createLinearGradient(
          p1.x.toInt + canvasHeight/2,
          p1.y.toInt + canvasHeight/2,
          p2.x.toInt + canvasHeight/2,
          p2.y.toInt + canvasHeight/2
        )
        gradient.addColorStop(0, "black")
        gradient.addColorStop(
          1,
          "hsl(" + (step * degreesPerSegment * 360 / totalAngle) + ", 50%, 50%)"
        )
        gradient
      } ,


      0
    )

  }
}
