package iPES.Parts

import iPES.Util.{Vector2D, Animatable, CustomContext, Animator}

case class Resistor(start: Vector2D, end: Vector2D, animator: Animator, width: Double = 20, lengthRatio: Double = 2) extends Animatable {
    animator.register(this)

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
