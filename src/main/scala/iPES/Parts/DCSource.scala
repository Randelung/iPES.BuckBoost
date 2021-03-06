package iPES.Parts

import iPES.Util._

/**
 * Basic DC source component.
 * @param start			Start vector of component
 * @param end			End vector of component
 * @param animator		Animator in which to register itself
 * @param inputHandler	Input handler in which to register itself. Can be omitted.
 * @param width			Width of component. Default is 20px.
 * @param lengthRatio	Length to width ratio. Default is 0.3.
 */
case class DCSource(start: Vector2D, end: Vector2D, animator: Animator, inputHandler: InputHandler = null, width: Double = 20, lengthRatio: Double = 0.3) extends Animatable {
    if (inputHandler != null)
        inputHandler registerArea(this, start - Vector2D(width / 2, width / 2), end + Vector2D(width / 2, width / 2))
    animator.register(this)

    /**
     * Draws basic double line DC source symbol.
     * @param context	Context in which to draw
     */
    override def draw(context: CustomContext): Unit = {
        val middle = (end + start) / 2
        val direction = (end - start) / (end - start).abs
        var w = width

        if (!((start - end).abs > 1.5 * width * lengthRatio))
            w = 2d / 3 * (end - start).abs / lengthRatio

        val corner1 = middle - direction * w * lengthRatio / 2 + direction.perpendicular * w
        val corner2 = corner1 - direction.perpendicular * w * 2
        val corner3 = corner2 + direction * w * lengthRatio + direction.perpendicular * w / 2
        val corner4 = corner3 + direction.perpendicular * w

        val lineWidth = context.getLineWidth

        context.beginPath()
            .moveTo(start)
            .lineTo(middle - direction * w * lengthRatio / 2)
            .moveTo(middle + direction * w * lengthRatio / 2)
            .lineTo(end)
            .stroke()
            .moveTo(corner3)
            .lineTo(corner4)
            .stroke()
            .beginPath()
            .setLineWidth(2 * lineWidth)
            .moveTo(corner1)
            .lineTo(corner2)
            .stroke()
            .setLineWidth(lineWidth)
            .moveTo(end)
    }

    /**
     * Current direction is reversed for forward flow.
     * @param context	Context in which to draw.
     * @param tick		Tick counter for which to draw.
     */
    override def animate(context: CustomContext, tick: Int): Unit = {
        if (flowDirection == FlowDirection.STOP)
            return
        flowDirection = if (flowDirection == FlowDirection.FORWARD) FlowDirection.REVERSE else FlowDirection.FORWARD
        super.animate(context, tick)
        flowDirection = if (flowDirection == FlowDirection.FORWARD) FlowDirection.REVERSE else FlowDirection.FORWARD
    }
}
