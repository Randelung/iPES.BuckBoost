package iPES

import org.scalajs.dom

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

  def setFillStyle(style: String): CustomContext = {
    context.fillStyle = style
    this
  }

  def setStrokeStyle(style: String): CustomContext = {
    context.strokeStyle = style
    this
  }

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
  
  def getLineWidth = {
    context.lineWidth
  }
}
