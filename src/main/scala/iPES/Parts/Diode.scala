package iPES.Parts

import iPES.{Animatable, CustomContext, Vector2D}

case class Diode(a: Vector2D, b: Vector2D, width: Double = 20) extends Animatable {
  var flowDirection = FORWARD

  def draw(context: CustomContext): Unit = {
    val middle = (a + b) / 2
    val direction = (b - a) / (b - a).abs
    var w = width

    if (!((a - b).abs > 1.5 * width))
      w = 2d / 3 * (b - a).abs

    val point = middle + direction * Math.sqrt(3) / 4 * w
    val start = point + direction.perpendicular * w / 2
    val end = point - direction.perpendicular * w / 2
    val corner1 = middle - direction * Math.sqrt(3) / 4 * w + direction.perpendicular * w / 2
    val corner2 = corner1 - direction.perpendicular * w

    context.beginPath()
      .moveTo(a)
      .lineTo(b)
      .moveTo(start)
      .lineTo(end)
      .stroke()
      .moveTo(point)
      .stroke()
      .lineTo(corner1)
      .lineTo(corner2)
      .lineTo(point)
      .fill()
      .moveTo(b)
  }

  def animate(context: CustomContext, tick: Int): Unit = {
    if (flowDirection == STOP || flowDirection == REVERSE)
      return
    val style = context.getFillStyle
    context.setFillStyle("#FFFF00")
    if (flowDirection == FORWARD) {
      context.beginPath()
        .arc(a + (b - a) * tick / 4, 4, 0, 2 * Math.PI)
        .fill()
        .setFillStyle(style)
    }
  }
}
