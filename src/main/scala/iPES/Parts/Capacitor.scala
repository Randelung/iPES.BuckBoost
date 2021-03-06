package iPES.Parts

import iPES.Util._

/**
 * Basic capacitor component.
 * @param start         Start vector of component
 * @param end           End vector of component
 * @param animator      Animator in which to register itself
 * @param inputHandler  Input handler in which to register itself. Can be omitted.
 * @param width         Width of component. Default is 40px.
 * @param lengthRatio   Length to width ratio. Default is 0.2.
 */
case class Capacitor(start: Vector2D, end: Vector2D, animator: Animator, inputHandler: InputHandler = null, width: Double = 40, lengthRatio: Double = 0.2) extends Animatable {
    if (inputHandler != null)
        inputHandler registerArea(this, start - Vector2D(width / 2, width / 2), end + Vector2D(width / 2, width / 2))
    animator.register(this)

    /**
     * Draws a capacitor symbol.
     * @param context	Context in which to draw
     */
    override def draw(context: CustomContext): Unit = {
        val middle = (end + start) / 2
        val direction = (end - start) / (end - start).abs
        var w = width

        if (!((start - end).abs > 1.5 * width * lengthRatio))
            w = 2d / 3 * (end - start).abs / lengthRatio

        val corner1 = middle + direction * w * lengthRatio / 2 + direction.perpendicular * w / 2
        val corner2 = corner1 - direction.perpendicular * w
        val corner3 = corner2 - direction * w * lengthRatio
        val corner4 = corner3 + direction.perpendicular * w

        val lineWidth = context.getLineWidth

        context.beginPath()
            .moveTo(start)
            .lineTo(middle - direction * w * lengthRatio / 2)
            .moveTo(middle + direction * w * lengthRatio / 2)
            .lineTo(end)
            .stroke()
            .beginPath()
            .setLineWidth(2 * lineWidth)
            .moveTo(corner1)
            .lineTo(corner2)
            .moveTo(corner3)
            .lineTo(corner4)
            .stroke()
            .setLineWidth(lineWidth)
            .moveTo(end)
    }
}
