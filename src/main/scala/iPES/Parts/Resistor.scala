package iPES.Parts

import iPES.{Animatable, CustomContext, Vector2D}

case class Resistor(a: Vector2D, b: Vector2D, width: Double = 20, lengthRatio: Double = 2) extends Animatable {
  var flowDirection = FORWARD

  def draw(context: CustomContext): Unit = {
    val middle = (b + a) / 2
    val direction = (b - a) / (b - a).abs
    var w = width

    if (!((a - b).abs > 1.5 * width * lengthRatio))
      w = 2d / 3 * (b - a).abs / lengthRatio

    val corner1 = middle + direction * w * lengthRatio / 2 + direction.perpendicular * w / 2
    val corner2 = corner1 - direction.perpendicular * w
    val corner3 = corner2 - direction * w * lengthRatio
    val corner4 = corner3 + direction.perpendicular * w

    context.beginPath()
      .moveTo(a)
      .lineTo(b)
      .stroke()
      .moveTo(corner1)
      .fillRect(corner1, corner3)
      .moveTo(b)
  }

  def animate(context: CustomContext, tick: Int): Unit = {
    if (flowDirection == STOP)
      return
    val style = context.getFillStyle
    context.setFillStyle("#FFFF00")
    if (flowDirection == FORWARD) {
      context.beginPath()
        .arc(a + (b - a) * tick / 4, 4, 0, 2 * Math.PI)
        .fill()
        .setFillStyle(style)
    }
    if (flowDirection == REVERSE) {
      context.beginPath()
        .arc(a + (b - a) * (4 - tick) / 4, 4, 0, 2 * Math.PI)
        .fill()
        .setFillStyle(style)
    }
  }
}
