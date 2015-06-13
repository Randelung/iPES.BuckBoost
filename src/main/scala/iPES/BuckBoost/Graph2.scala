package iPES.BuckBoost

import iPES.Util.{CustomContext, Vector2D}
import org.scalajs.dom
import org.scalajs.dom.raw.MouseEvent

class Graph2(context_bottom: CustomContext, context_top: CustomContext, callback: Graph2Callback) {
    private val corner_top_left = Vector2D(0.05 * context_bottom.canvas.width, 0.05 * context_bottom.canvas.height)
    private val corner_top_right = Vector2D(0.95 * context_bottom.canvas.width, 0.05 * context_bottom.canvas.height)
    private val corner_bottom_left = Vector2D(0.05 * context_bottom.canvas.width, 0.95 * context_bottom.canvas.height)
    private val corner_bottom_right = Vector2D(0.95 * context_bottom.canvas.width, 0.95 * context_bottom.canvas.height)
    private val factor_height = (corner_bottom_left - corner_top_left).y / 2
    private val factor_width = (corner_top_right - corner_top_left).x / (2 * Math.PI)

    context_top.canvas.onselectstart = (_: Any) => false

    context_bottom.setLineWidth(2)
    context_top.setLineWidth(2)
    context_top.setFillStyle("#ff0000")

    context_top.canvas.onmousedown = (e: MouseEvent) => {
        val temp = Vector2D(e.clientX - context_top.canvas.getBoundingClientRect().left, e.clientY - context_top.canvas.getBoundingClientRect().top)
        if (insideGraph(temp)) {
            context_top.canvas.onmousemove = (e: dom.MouseEvent) => {
                var closed = false
                val temp = Vector2D(e.clientX - context_top.canvas.getBoundingClientRect().left, e.clientY - context_top.canvas.getBoundingClientRect().top)
                if (insideGraph(temp)) {
                    context_top.clearRect(0, 0, context_top.canvas.width, context_top.canvas.height)
                    context_top.beginPath()
                        .arc(Vector2D(temp.x, y(temp.x)), 4, 0, 2 * Math.PI)
                        .fill()
                    callback.onClick(1 + Math.sin((temp.x - corner_bottom_left.x) / factor_width))
                }
            }
            context_top.canvas.onmousemove(e)
        }
    }

    context_top.canvas.onmouseup = (_: Any) => context_top.canvas.onmousemove = (_: Any) => null

    context_top.canvas.onmouseleave = (_: Any) => context_top.canvas.onmousemove = (_: Any) => null

    var i = corner_bottom_left.x

    context_bottom.beginPath()
        .moveTo(corner_top_left)
        .lineTo(corner_bottom_left)
        .lineTo(corner_bottom_right)
        .stroke()

    context_bottom.beginPath()

    def insideGraph(vector2D: Vector2D): Boolean = {
        (vector2D.x > corner_bottom_left.x
            && vector2D.x < corner_bottom_right.x
            && vector2D.y > corner_top_left.y
            && vector2D.y < corner_bottom_left.y)
    }

    while (i < corner_bottom_right.x + 1) {
        context_bottom.lineTo(i, y(i))
        i += 1
    }
    context_bottom.stroke()

    def y(x: Double): Double = {
        corner_bottom_left.y / 2 + corner_top_left.y / 2 - Math.sin((x - corner_bottom_left.x) / factor_width) * factor_height
    }


    context_top.beginPath()
        .arc(Vector2D(0.5 * Math.PI * factor_width + corner_bottom_left.x, y(0.5 * Math.PI * factor_width + corner_bottom_left.x)), 4, 0, 2 * Math.PI)
        .fill()
}

trait Graph2Callback {
    def onClick(modifier: Double): Unit
}