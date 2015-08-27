package iPES.Parts

import iPES.Util._

/**
 * Basic switch component.
 * @param start			Start vector of switch
 * @param end			End vector of switch
 * @param animator		Animator, in which to register itself
 * @param inputHandler	Input handler, in which to register itself. Can be omitted, but is not recommended, since a
 *                        switch is an active component.
 * @param closed		Initial (and current) state of switch. Default is closed/conducting.
 * @param length		Length of switch piece itself. Default is 20 pixels.
 */
case class Switch(start: Vector2D, end: Vector2D, animator: Animator, inputHandler: InputHandler = null, var closed: Boolean = true, length: Double = 20) extends Animatable {
    if (inputHandler != null)
        inputHandler registerArea(this, start - Vector2D(10, 10), end + Vector2D(10, 10))
    animator.register(this)

    /**
     * Draws the switch layout to the lower context.
     * @param context	Context in which to draw
     */
    override def draw(context: CustomContext): Unit = {
        val middle = (end + start) / 2
        val direction = (end - start) / (end - start).abs
        var w = length

        if (!((start - end).abs > 1.5 * length))
            w = 2d / 3 * (end - start).abs

        val localstart = middle - direction * w / 2
        val localend = middle + direction * w / 2
        var open = end
        if (!closed)
            open = localend - direction.perpendicular * w / 4

        val fillstyle = context.getFillStyle

        context.beginPath()
            .moveTo(start)
            .lineTo(localstart)
            .lineTo(open)
            .moveTo(localend)
            .lineTo(end)
            .stroke()
            .moveTo(localstart)
            .beginPath()
            .setFillStyle("#ffffff")
            .arc(localstart, w / 10, 0, 2 * Math.PI)
            .fill()
            .stroke()
            .moveTo(localend)
            .beginPath()
            .arc(localend, w / 10, 0, 2 * Math.PI)
            .fill()
            .stroke()
            .setFillStyle(fillstyle)
            .moveTo(end)
    }

    /**
     * More elaborate animation. Since the switch stops conducting when it's opened, the animation should mirror that.
     * @param context	Context in which to draw.
     * @param tick		Tick counter for which to draw.
     */
    override def animate(context: CustomContext, tick: Int): Unit = {
        if (closed)
            super.animate(context, tick)
    }

    /**
     * Toggles state, preserves flow direction
     */
    def toggle(): Unit = {
        closed = !closed
    }

    /**
     * Closes switch, reinstates preserved flow direction
     */
    def setClosed(): Unit = {
        closed = true
    }

    /**
     * Opens switch, preserves flow direction
     */
    def setOpen(): Unit = {
        closed = false
    }
}
