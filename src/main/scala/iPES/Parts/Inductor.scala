package iPES.Parts

import iPES.Util._

/**
 * Basic inductor component.
 * @param start         Start vector of component
 * @param end           End vector of component
 * @param animator      Animator in which to register itself
 * @param inputHandler  Input handler in which to register itself. Can be omitted.
 * @param width         Width of component. Default is 20px.
 * @param lengthRatio   Length to width ratio. Default is 2.
 */
case class Inductor(start: Vector2D, end: Vector2D, animator: Animator, inputHandler: InputHandler = null, width: Double = 20, lengthRatio: Double = 2) extends Animatable {
    if (inputHandler != null)
        inputHandler registerArea(this, start - Vector2D(10, 10), end + Vector2D(10, 10))
    animator.register(this)

    /**
     * Draws a European inductor symbol.
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

        context.beginPath()
            .moveTo(start)
            .lineTo(end)
            .stroke()
            .moveTo(corner1)
            .fillRect(corner1, corner3)
            .moveTo(end)
    }
}
