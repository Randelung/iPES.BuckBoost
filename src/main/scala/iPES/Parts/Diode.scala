package iPES.Parts

import iPES.Util._

/**
 * Basic diode component.
 * @param start			Start vector of component
 * @param end			End vector of component
 * @param animator		Animator in which to register itself
 * @param inputHandler	Input handler in which to register itself. Can be omitted.
 * @param width			Width of diode. Default value is 20px.
 */
case class Diode(start: Vector2D, end: Vector2D, animator: Animator, inputHandler: InputHandler = null, width: Double = 20) extends Animatable {
    if (inputHandler != null)
        inputHandler registerArea(this, start - Vector2D(width / 2, width / 2), end + Vector2D(width / 2, width / 2))
    animator.register(this)

    /**
     * Draws diode symbol.
     * @param context	Context in which to draw
     */
    override def draw(context: CustomContext): Unit = {
        val middle = (start + end) / 2
        val direction = (end - start) / (end - start).abs
        var w = width

        if (!((start - end).abs > 1.5 * width))
            w = 2d / 3 * (end - start).abs

        val point = middle + direction * Math.sqrt(3) / 4 * w
        val localstart = point + direction.perpendicular * w / 2
        val localend = point - direction.perpendicular * w / 2
        val corner1 = middle - direction * Math.sqrt(3) / 4 * w + direction.perpendicular * w / 2
        val corner2 = corner1 - direction.perpendicular * w

        context.beginPath()
            .moveTo(start)
            .lineTo(end)
            .moveTo(localstart)
            .lineTo(localend)
            .stroke()
            .moveTo(point)
            .stroke()
            .lineTo(corner1)
            .lineTo(corner2)
            .lineTo(point)
            .fill()
            .moveTo(end)
    }

    /**
     * Basic animation needs to be augmented. Flow direction is set to [[FlowDirection]].STOP if a reverse current is
     * detected.
     * @param context	Context in which to draw.
     * @param tick		Tick counter for which to draw.
     */
    override def animate(context: CustomContext, tick: Int): Unit = {
        if (flowDirection == FlowDirection.REVERSE)
            flowDirection = FlowDirection.STOP
        super.animate(context, tick)
    }
}
