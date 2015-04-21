package iPES.Parts

import iPES.{Animatable, CustomContext, Vector2D}

case class Wire(a: Vector2D, b: Vector2D) extends Animatable {
  var flowDirection = FORWARD

  def draw(context: CustomContext): Unit = {
    context.beginPath()
      .moveTo(a)
      .lineTo(b)
      .stroke()
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
