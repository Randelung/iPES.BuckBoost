package iPES.BuckBoost

import iPES.Util.{CustomContext, Vector2D}
import org.scalajs.dom
import org.scalajs.dom.raw.MouseEvent

class Graph2(context_bottom: CustomContext, context_top: CustomContext, callback: Graph2Callback) {
    private val corner_top_left = Vector2D(0.1 * context_bottom.canvas.width, 0.1 * context_bottom.canvas.height)
    private val corner_top_right = Vector2D(0.95 * context_bottom.canvas.width, 0.1 * context_bottom.canvas.height)
    private val corner_bottom_left = Vector2D(0.1 * context_bottom.canvas.width, 0.9 * context_bottom.canvas.height)
    private val corner_bottom_right = Vector2D(0.95 * context_bottom.canvas.width, 0.9 * context_bottom.canvas.height)
    private val factor_height = (corner_bottom_left - corner_top_left).y / 2
    private val factor_width = (corner_top_right - corner_top_left).x / (2 * Math.PI)

    context_top.canvas.onselectstart = (_: Any) => false

    context_top.canvas.onmousedown = (e: MouseEvent) => {
        val temp = Vector2D(e.clientX - context_top.canvas.getBoundingClientRect().left, e.clientY - context_top.canvas.getBoundingClientRect().top)
        dom.document.onselectstart = (_: Any) => false
        if (insideGraph(temp)) {
            context_top.canvas.onmousemove = (e: dom.MouseEvent) => {
                var closed = false
                val temp = Vector2D(e.clientX - context_top.canvas.getBoundingClientRect().left, e.clientY - context_top.canvas.getBoundingClientRect().top)
                if (insideGraph(temp)) {
                    context_top.clearRect(0, 0, context_top.canvas.width, context_top.canvas.height)
                        .setFillStyle("#ff0000")
                        .beginPath()
                        .arc(Vector2D(temp.x, y(temp.x)), 4, 0, 2 * Math.PI)
                        .fill()
                    callback.onClick(1 + Math.sin((temp.x - corner_bottom_left.x) / factor_width))
                }
            }
            context_top.canvas.onmousemove(e)
        }
    }

    context_top.canvas.onmouseup = (_: Any) => {
        context_top.canvas.onmousemove = (_: Any) => null
        dom.document.onselectstart = (_: Any) => null
    }

    context_top.canvas.onmouseleave = (_: Any) => context_top.canvas.onmousemove = (_: Any) => null

    dom.document.onmouseup = (_: Any) => dom.document.onselectstart = (_: Any) => null

    context_bottom.setLineWidth(2)
    context_top.setLineWidth(2)

    context_bottom.setStrokeStyle("green")
    context_bottom.beginPath()
    for (x <- 0 to ((corner_bottom_right.x - corner_bottom_left.x) / 1.5).asInstanceOf[Int]) {
        context_bottom.lineTo(x + corner_bottom_left.x, corner_bottom_left.y - (1 - Math.sqrt(x * 1.5 / (corner_bottom_right.x - corner_bottom_left.x).asInstanceOf[Int])) * 200)
    }
    context_bottom.stroke()

    context_bottom.setStrokeStyle("gray")
        .setFillStyle("blue")
    for (currentCurve <- 1 to 9) {
        context_bottom.beginPath()
        for (x <- 0 to (corner_bottom_right.x - corner_bottom_left.x).asInstanceOf[Int]) {
            val normedCurrent = x * 1.5 / (corner_bottom_right.x - corner_bottom_left.x).asInstanceOf[Int]
            val dutyFactor = currentCurve / 10.0
            val d = if (dutyFactor == 0 || dutyFactor == 1) 0.5 else dutyFactor
            if (normedCurrent >= (1 - d) * (1 - d))
                context_bottom.lineTo(x + corner_bottom_left.x, corner_bottom_left.y - d * 200)
            else
                context_bottom.lineTo(x + corner_bottom_left.x, corner_bottom_left.y - Math.sqrt(normedCurrent) * d / (1 - d) * 200)
        }
        context_bottom.stroke()

        context_bottom.fillText((Math.round(currentCurve / 10.0 / (1 - currentCurve / 10.0) * 10) / 10.0).toString(), corner_bottom_right.x + 5, corner_bottom_right.y + 3 - currentCurve * 20)
    }
    context_bottom.fillText("U2/U1 = const", corner_top_right + Vector2D(-50, 0))

    context_bottom.setStrokeStyle("black")
        .beginPath()
        .moveTo(corner_top_left)
        .lineTo(corner_bottom_left)
        .lineTo(corner_bottom_right)
        .stroke()

    context_bottom.context.font = "20px Arial"
    context_bottom.setFillStyle("black")
        .fillText("i", corner_bottom_right + Vector2D(5, 8))
        .fillText("D", corner_top_left + Vector2D(-6, -5))

    context_top.beginPath()
        .setFillStyle("#ff0000")
        .arc(Vector2D(Math.PI * factor_width + corner_bottom_left.x, y(Math.PI * factor_width + corner_bottom_left.x)), 4, 0, 2 * Math.PI)
        .fill()

    def y(x: Double): Double = {
        corner_bottom_left.y / 2 + corner_top_left.y / 2 - Math.sin((x - corner_bottom_left.x) / factor_width) * factor_height
    }

    def insideGraph(vector2D: Vector2D): Boolean = {
        (vector2D.x > corner_bottom_left.x
            && vector2D.x < corner_bottom_right.x
            && vector2D.y > corner_top_left.y
            && vector2D.y < corner_bottom_left.y)
    }
}

trait Graph2Callback {
    def onClick(modifier: Double): Unit
}