package iPES.Parts

import iPES.Util._

case class Diode(start: Vector2D, end: Vector2D, animator: Animator, width: Double = 20) extends Animatable {
    animator.register(this)

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

    override def animate(context: CustomContext, tick: Int): Unit = {
        if (flowDirection == FlowDirection.REVERSE)
            flowDirection = FlowDirection.STOP
        super.animate(context, tick)
    }
}
