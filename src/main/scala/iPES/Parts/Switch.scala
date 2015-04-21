package iPES.Parts

import iPES.{Clickable, Animatable, CustomContext, Vector2D}

case class Switch(a: Vector2D, b: Vector2D, var closed: Boolean = true, length: Double = 20) extends Clickable {
  var flowDirection = FORWARD

  def draw(context: CustomContext): Unit = {
    val middle = (b + a) / 2
    val direction = (b - a) / (b - a).abs
    var w = length

    if (!((a - b).abs > 1.5 * length))
      w = 2d / 3 * (b - a).abs

    val start = middle - direction * w / 2
    val end = middle + direction * w / 2
    var open = end
    if (!closed)
      open = end + direction.perpendicular * w / 4

    val fillstyle = context.getFillStyle

    context.beginPath()
      .moveTo(a)
      .lineTo(start)
      .lineTo(open)
      .moveTo(end)
      .lineTo(b)
      .stroke()
      .moveTo(start)
      .beginPath()
      .setFillStyle("#ffffff")
      .arc(start, w / 10, 0, 2*Math.PI)
      .fill()
      .stroke()
      .moveTo(end)
      .beginPath()
      .arc(end, w / 10, 0, 2*Math.PI)
      .fill()
      .stroke()
      .setFillStyle(fillstyle)
      .moveTo(b)
  }

  def animate(context: CustomContext, tick: Int): Unit = {
    if (flowDirection == STOP || !closed)
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

  def onClick(position: Vector2D): Unit = {
    closed = !closed
  }
}
