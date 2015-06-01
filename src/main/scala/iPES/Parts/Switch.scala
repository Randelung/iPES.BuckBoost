package iPES.Parts

import iPES.Util._

case class Switch(start: Vector2D, end: Vector2D, animator: Animator, inputHandler: InputHandler = null, var closed: Boolean = true, length: Double = 20) extends Animatable {
    if (inputHandler != null)
        inputHandler registerArea(this, start - Vector2D(10, 10), end + Vector2D(10, 10))
    animator.register(this)

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
            open = localend + direction.perpendicular * w / 4

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

    override def animate(context: CustomContext, tick: Int): Unit = {
        if (closed)
            super.animate(context, tick)
    }
}
