package iPES.BuckBoost

import iPES.Util.{CustomContext, Vector2D}
import org.scalajs.dom

class Graph1(context_bottom: CustomContext, context_top: CustomContext, callback: Graph1Callback) {
    private val corner_top_left = Vector2D(0.05 * context_bottom.canvas.width, 0.05 * context_bottom.canvas.height)
    private val corner_top_right = Vector2D(0.95 * context_bottom.canvas.width, 0.05 * context_bottom.canvas.height)
    private val corner_bottom_left = Vector2D(0.05 * context_bottom.canvas.width, 0.95 * context_bottom.canvas.height)
    private val corner_bottom_right = Vector2D(0.95 * context_bottom.canvas.width, 0.95 * context_bottom.canvas.height)
    private val factor_height = (corner_bottom_left - corner_top_left).y / 2
    private val factor_width = (corner_top_right - corner_top_left).x / (4 * Math.PI)

    private val dt = 500e-9
    private val R = 4
    private val L = 50e-6
    private val C = 20e-6
    private val uIN = 40
    private val uOUTsoll = 40
    private val punkte = corner_bottom_right.x - corner_bottom_left.x
    private val f = 1 / (dt * punkte)
    private val graph_values = new Array[Array[Double]](5)
    private var h1: Double = _
    private var h2: Double = _

    context_top.canvas.onselectstart = (_: Any) => false

    context_top.canvas.onmousedown = (e: dom.MouseEvent) => {
        val temp = Vector2D(e.clientX - context_top.canvas.getBoundingClientRect().left, e.clientY - context_top.canvas.getBoundingClientRect().top)
        if (insideGraph(temp)) {
            context_top.canvas.onmousemove = (e: dom.MouseEvent) => {
                var closed = false
                val temp = Vector2D(e.clientX - context_top.canvas.getBoundingClientRect().left, e.clientY - context_top.canvas.getBoundingClientRect().top)
                if (insideGraph(temp)) {
                    redraw_line(temp.x)
                    if ((temp.x > corner_bottom_left.x && temp.x < Math.PI * factor_width + corner_bottom_left.x)
                        || (temp.x > 2 * Math.PI * factor_width + corner_bottom_left.x && temp.x < 3 * Math.PI * factor_width + corner_bottom_left.x))
                        callback.onClick(closed = true)
                    else
                        callback.onClick(closed = false)
                }
            }
            context_top.canvas.onmousemove(e)
        }
    }

    context_top.canvas.onmouseup = (_: Any) => context_top.canvas.onmousemove = (_: Any) => null

    context_top.canvas.onmouseleave = (_: Any) => context_top.canvas.onmousemove = (_: Any) => null

    context_bottom.setLineWidth(2)
    context_top.setLineWidth(2)
    context_top.setStrokeStyle("#ff0000")
    private var h3: Double = _
    graph_values(0) = new Array[Double](punkte.asInstanceOf[Int])
    graph_values(1) = new Array[Double](punkte.asInstanceOf[Int])
    graph_values(2) = new Array[Double](punkte.asInstanceOf[Int])
    graph_values(3) = new Array[Double](punkte.asInstanceOf[Int])
    graph_values(4) = new Array[Double](punkte.asInstanceOf[Int])

    redraw_graph(1)
    redraw_line(0.5 * Math.PI * factor_width + corner_bottom_left.x)

    def insideGraph(vector2D: Vector2D): Boolean = {
        (vector2D.x > corner_bottom_left.x
            && vector2D.x < corner_bottom_right.x
            && vector2D.y > corner_top_left.y
            && vector2D.y < corner_bottom_left.y)
    }

    def redraw_graph(modifier: Double): Unit = {
        require(modifier >= 0, "sin_modifier must be >= 0")
        require(modifier <= 2, "sin_modifier must be <= 2")

        context_bottom.clearRect(0, 0, context_bottom.canvas.width, context_bottom.canvas.height)

        context_bottom.beginPath()
            .moveTo(corner_top_left)
            .lineTo(corner_bottom_left)
            .lineTo(corner_bottom_right)
            .stroke()

        val offset_left = corner_bottom_left.x.asInstanceOf[Int]
        val offset_right = corner_bottom_right.x.asInstanceOf[Int]
        for (i <- 0 to 4; x <- (offset_left to offset_right).filter(i => i % 5 == 0)) {
            graph_values(i)(x - offset_left) = y(x, modifier)
            println("Graph " + i + " location " + x)
        }

        context_bottom.beginPath()
        for (i <- 0 to 4; x <- (offset_left to offset_right).filter(i => i % 5 == 0)) {
            context_bottom.lineTo(x, graph_values(i)(x - offset_left))
        }
        context_bottom.stroke()

        /*var i = corner_bottom_left.x
        while (i < corner_bottom_right.x + 1) {
            context_bottom.lineTo(i, y(i, modifier))
            i += 1
        }
        context_bottom.stroke()*/
    }

    private def y(x: Double, modifier: Double): Double = {
        if (math.sin((x - corner_bottom_left.x) / factor_width) < 0)
            corner_bottom_left.y
        else
            corner_bottom_left.y - modifier * Math.sin((x - corner_bottom_left.x) / factor_width) * factor_height
    }

    def redraw_line(position: Double): Unit = {
        context_top.clearRect(corner_top_left, corner_bottom_right)
        context_top.beginPath()
            .moveTo(position, corner_top_left.y)
            .lineTo(position, corner_bottom_left.y)
            .stroke()
    }
}

trait Graph1Callback {
    def onClick(closed: Boolean): Unit
}