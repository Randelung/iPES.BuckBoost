package iPES.Util

import org.scalajs.dom

import scala.scalajs.js.Array

/**
 * Wrapper class for `org.scalajs.dom.CanvasRenderingContext2D` to support [[Vector2D]] and command chaining. direct access to the
 * original context is possible using the argument `context`, or the member `canvas`. See documentation of
 * CanvasRedneringContext2D for usage details.
 * @param context   Usual canvas context.
 */
case class CustomContext(context: dom.CanvasRenderingContext2D) {

    def lineTo(that: Vector2D): CustomContext = {
        context.lineTo(that.x, that.y)
        this
    }

    def lineTo(x: Double, y: Double): CustomContext = {
        context.lineTo(x, y)
        this
    }

    def moveTo(that: Vector2D): CustomContext = {
        context.moveTo(that.x, that.y)
        this
    }

    def moveTo(x: Double, y: Double): CustomContext = {
        context.moveTo(x, y)
        this
    }

    def beginPath(): CustomContext = {
        context.beginPath()
        this
    }

    def stroke(): CustomContext = {
        context.stroke()
        this
    }

    def fill(): CustomContext = {
        context.fill()
        this
    }

    def closePath(): CustomContext = {
        context.closePath()
        this
    }

    def setFillStyle(style: scala.scalajs.js.Any): CustomContext = {
        context.fillStyle = style
        this
    }

    def getFillStyle = context.fillStyle

    def setStrokeStyle(style: scala.scalajs.js.Any): CustomContext = {
        context.strokeStyle = style
        this
    }

    def getStrokeStyle = context.strokeStyle

    def rect(x: Double, y: Double, w: Double, h: Double): CustomContext = {
        context.rect(x, y, w, h)
        this
    }

    def rect(start: Vector2D, w: Double, h: Double): CustomContext = {
        context.rect(start.x, start.y, w, h)
        this
    }

    def rect(start: Vector2D, corner: Vector2D): CustomContext = {
        context.rect(start.x, start.y, corner.x - start.x, corner.y - start.y)
        this
    }

    def strokeRect(x: Double, y: Double, w: Double, h: Double): CustomContext = {
        context.strokeRect(x, y, w, h)
        this
    }

    def strokeRect(start: Vector2D, w: Double, h: Double): CustomContext = {
        context.strokeRect(start.x, start.y, w, h)
        this
    }

    def strokeRect(start: Vector2D, corner: Vector2D): CustomContext = {
        context.strokeRect(start.x, start.y, corner.x - start.x, corner.y - start.y)
        this
    }

    def setLineWidth(lineWidth: Double): CustomContext = {
        context.lineWidth = lineWidth
        this
    }

    def getLineWidth = context.lineWidth

    def fillRect(start: Vector2D, corner: Vector2D): CustomContext = {
        context.fillRect(start.x, start.y, corner.x - start.x, corner.y - start.y)
        this
    }

    def fillRect(x: Double, y: Double, w: Double, h: Double): CustomContext = {
        context.fillRect(x, y, w, h)
        this
    }

    def fillRect(start: Vector2D, w: Double, h: Double): CustomContext = {
        context.fillRect(start.x, start.y, w, h)
        this
    }

    def arc(start: Vector2D, radius: Double, startAngle: Double, endAngle: Double, anticlockwise: Boolean = false): CustomContext = {
        context.arc(start.x, start.y, radius, startAngle, endAngle, anticlockwise)
        this
    }

    def clearRect(x: Double, y: Double, w: Double, h: Double): CustomContext = {
        context.clearRect(x, y, w, h)
        this
    }

    def clearRect(a: Vector2D, b: Vector2D): CustomContext = {
        context.clearRect(a.x, a.y, b.x - a.x, b.y - a.y)
        this
    }

    def canvas = context.canvas

    def setLineDash(segments: Array[Double]): CustomContext = {
        context.setLineDash(segments)
        this
    }

    def fillText(text: String, v: Vector2D): CustomContext = {
        context.fillText(text, v.x, v.y)
        this
    }

    def fillText(text: String, x: Double, y: Double): CustomContext = {
        context.fillText(text, x, y)
        this
    }
}
