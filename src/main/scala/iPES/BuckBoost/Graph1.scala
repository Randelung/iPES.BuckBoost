package iPES.BuckBoost

import iPES.Util.{CustomContext, Vector2D}
import org.scalajs.dom

import scala.scalajs.js.{Array => JSArray}

class Graph1(context_bottom: CustomContext, context_top: CustomContext, callback: Graph1Callback) {
	// define useful points of interest, like corners and midpoints
	private val corner_top_left = Vector2D(0.1 * context_bottom.canvas.width, 0.05 * context_bottom.canvas.height)
	private val corner_top_right = Vector2D(0.95 * context_bottom.canvas.width, 0.05 * context_bottom.canvas.height)
	private val corner_bottom_left = Vector2D(0.1 * context_bottom.canvas.width, 0.9 * context_bottom.canvas.height)
	private val corner_bottom_right = Vector2D(0.95 * context_bottom.canvas.width, 0.9 * context_bottom.canvas.height)
	private val middle_left = Vector2D(0.1 * context_bottom.canvas.width, 0.475 * context_bottom.canvas.height)
	private val middle_right = Vector2D(0.95 * context_bottom.canvas.width, 0.475 * context_bottom.canvas.height)
	private val middle_bottom = Vector2D(0.525 * context_bottom.canvas.width, 0.9 * context_bottom.canvas.height)

	// scaling factors for the five functions
	private val factor_height_0 = (corner_bottom_left.y - corner_top_left.y) / 4
	private val factor_height_1 = (corner_bottom_left.y - corner_top_left.y) / 200
	private val factor_height_2 = factor_height_1
	private val factor_height_3 = factor_height_1
	private val factor_height_4 = factor_height_1

	// graphing offset for the five functions
	private val offset_height_0 = 0
	private val offset_height_1 = offset_height_0
	private val offset_height_2 = (corner_bottom_left.y - corner_top_left.y) / 2
	private val offset_height_3 = offset_height_0
	private val offset_height_4 = offset_height_2

	// some constants taken from the Java applet, function not entirely clear.
	private val dt = 500e-9
	private val L = 50e-6
	private val C = 20e-6
	private val uIN = 40
	// remains unused lol
	private val uOUTsoll = 40
	private val punkte = ((corner_bottom_right.x - corner_bottom_left.x) / 2).asInstanceOf[Int]
	private val f = 1 / (dt * punkte)
	// array to store all values in
	private val graph_values = new Array[Array[Double]](5)
	private var R: Double = _
	private var h1: Double = _
	private var h2: Double = _
	private var h3: Double = _
	// location of toggle between open and closed switch
	private var onOffBorder: Double = _
	// current location of re bar
	private var currentRedbarX: Double = _

	context_top.canvas.onmousedown = (e: dom.MouseEvent) => {
		e.preventDefault()
		val temp = Vector2D(e.clientX - context_top.canvas.getBoundingClientRect().left, e.clientY - context_top.canvas.getBoundingClientRect().top)
		if (insideGraph(temp)) {
			context_top.canvas.onmousemove = (e: dom.MouseEvent) => {
				val temp = Vector2D(e.clientX - context_top.canvas.getBoundingClientRect().left, e.clientY - context_top.canvas.getBoundingClientRect().top)
				if (insideGraph(temp)) {
					redraw_line(temp.x)
					if ((temp.x > corner_bottom_left.x && temp.x < (corner_top_right.x - corner_top_left.x) / 2 * onOffBorder + corner_bottom_left.x)
						|| (temp.x > (corner_top_right.x - corner_top_left.x) / 2 + corner_bottom_left.x && temp.x < (corner_top_right.x - corner_top_left.x) / 2 + (corner_top_right.x - corner_top_left.x) / 2 * onOffBorder + corner_bottom_left.x))
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
	context_bottom.context.font = "20px Arial"
	graph_values(0) = new Array[Double](punkte)
	graph_values(1) = new Array[Double](punkte)
	graph_values(2) = new Array[Double](punkte)
	graph_values(3) = new Array[Double](punkte)
	graph_values(4) = new Array[Double](punkte)

	// initialize line and graph
	redraw_line(30 + corner_bottom_left.x)
	redraw_graph(0.75, 0.5)

	/** Recalculates all points and draws them to the top canvas.
	  *
	  * @param i0	initial current, stems from Java applet
	  * @param yNorm	function not entirely clear, stems from Java applet
	  */
	def redraw_graph(i0: Double, yNorm: Double): Unit = {

		onOffBorder = yNorm

		R = 2 * f * L / i0

		h1 = 0.25 + L / dt * (C / dt + 0.5 / R)
		h2 = -0.25 + L / dt * (C / dt - 0.5 / R)
		h3 = -0.25 + L / dt * (C / dt + 0.5 / R)

		val strokeStyle = context_bottom.getStrokeStyle
		val fillStyle = context_bottom.getFillStyle

		context_bottom.clearRect(0, 0, context_bottom.canvas.width, context_bottom.canvas.height)

		graph_values(0)(0) = 0
		graph_values(1)(0) = 0
		graph_values(2)(0) = 40
		graph_values(3)(0) = 0
		graph_values(4)(0) = 40

		val offset_left = corner_bottom_left.x.asInstanceOf[Int]
		val offset_right = corner_bottom_right.x.asInstanceOf[Int]
		for (_ <- 0 to 4) {
			var i = 1
			while (i < (offset_right - offset_left) / 2 * yNorm) {
				graph_values(0)(i) = 1
				graph_values(1)(i) = graph_values(1)(i - 1) + dt / L * uIN
				graph_values(2)(i) = graph_values(2)(i - 1) * (C / dt - 0.5 * R) / (C / dt + 0.5 / R)
				graph_values(3)(i) = graph_values(1)(i)
				graph_values(4)(i) = uIN
				i += 1
			}
			while (i < punkte) {
				graph_values(0)(i) = 0
				graph_values(1)(i) = (graph_values(1)(i - 1) * h3 - graph_values(2)(i - 1) * C / dt) / h1
				graph_values(2)(i) = (graph_values(1)(i - 1) * L / dt + graph_values(2)(i - 1) * h2) / h1
				graph_values(3)(i) = 0
				graph_values(4)(i) = uIN
				i += 1
			}
			graph_values(0)(0) = graph_values(0)(punkte - 1)
			graph_values(1)(0) = graph_values(1)(punkte - 1)
			graph_values(2)(0) = graph_values(2)(punkte - 1)
			graph_values(3)(0) = graph_values(3)(punkte - 1)
			graph_values(4)(0) = graph_values(4)(punkte - 1)
		}

		context_bottom.setStrokeStyle("orange")
		context_bottom.beginPath()
		context_bottom.moveTo(offset_left, corner_bottom_left.y - graph_values(0)(1) * factor_height_0 - offset_height_0)
		for (x <- 1 to offset_right - offset_left) {
			context_bottom.lineTo(x + offset_left, corner_bottom_left.y - graph_values(0)(x) * factor_height_0 - offset_height_0)
		}
		for (x <- 1 to offset_right - offset_left) {
			context_bottom.lineTo(x + offset_left + punkte, corner_bottom_left.y - graph_values(0)(x) * factor_height_0 - offset_height_0)
		}
		context_bottom.stroke()
		context_bottom.setFillStyle("orange")
		context_bottom.fillText("s", middle_left + Vector2D(-30, 18))

		context_bottom.setStrokeStyle("lightgreen")
		context_bottom.beginPath()
		context_bottom.moveTo(offset_left, corner_bottom_left.y - graph_values(1)(1) * factor_height_1 - offset_height_1)
		for (x <- 1 to offset_right - offset_left) {
			context_bottom.lineTo(x + offset_left, corner_bottom_left.y - graph_values(1)(x) * factor_height_1 - offset_height_1)
		}
		for (x <- 1 to offset_right - offset_left) {
			context_bottom.lineTo(x + offset_left + punkte, corner_bottom_left.y - graph_values(1)(x) * factor_height_1 - offset_height_1)
		}
		context_bottom.stroke()
		context_bottom.setFillStyle("lightgreen")
		context_bottom.fillText("iL", middle_left + Vector2D(-30, 48))

		context_bottom.setStrokeStyle("darkblue")
		context_bottom.beginPath()
		context_bottom.moveTo(offset_left, corner_bottom_left.y - graph_values(2)(1) * factor_height_2 - offset_height_2)
		for (x <- 1 to offset_right - offset_left) {
			context_bottom.lineTo(x + offset_left, corner_bottom_left.y - graph_values(2)(x) * factor_height_2 - offset_height_2)
		}
		for (x <- 1 to offset_right - offset_left) {
			context_bottom.lineTo(x + offset_left + punkte, corner_bottom_left.y - graph_values(2)(x) * factor_height_2 - offset_height_2)
		}
		context_bottom.stroke()
		context_bottom.setFillStyle("darkblue")
		context_bottom.fillText("u2", corner_top_left + Vector2D(-30, 48))

		context_bottom.setStrokeStyle("black")
		context_bottom.beginPath()
		context_bottom.moveTo(offset_left, corner_bottom_left.y - graph_values(3)(1) * factor_height_3 - offset_height_3)
		for (x <- 1 to offset_right - offset_left) {
			context_bottom.lineTo(x + offset_left, corner_bottom_left.y - graph_values(3)(x) * factor_height_3 - offset_height_3)
		}
		for (x <- 1 to offset_right - offset_left) {
			context_bottom.lineTo(x + offset_left + punkte, corner_bottom_left.y - graph_values(3)(x) * factor_height_3 - offset_height_3)
		}
		context_bottom.stroke()
		context_bottom.setFillStyle("black")
		context_bottom.fillText("i1", middle_left + Vector2D(-30, 78))

		context_bottom.setStrokeStyle("lightblue")
		context_bottom.beginPath()
		context_bottom.moveTo(offset_left, corner_bottom_left.y - graph_values(4)(1) * factor_height_4 - offset_height_4)
		for (x <- 1 to offset_right - offset_left) {
			context_bottom.lineTo(x + offset_left, corner_bottom_left.y - graph_values(4)(x) * factor_height_4 - offset_height_4)
		}
		for (x <- 1 to offset_right - offset_left) {
			context_bottom.lineTo(x + offset_left + punkte, corner_bottom_left.y - graph_values(4)(x) * factor_height_4 - offset_height_4)
		}
		context_bottom.stroke()
		context_bottom.setFillStyle("lightblue")
		context_bottom.fillText("u1", corner_top_left + Vector2D(-30, 18))

		context_bottom.setStrokeStyle(strokeStyle)
		context_bottom.setFillStyle(fillStyle)

		context_bottom.beginPath()
			.moveTo(corner_top_left)
			.lineTo(corner_bottom_left)
			.lineTo(corner_bottom_right)
			.stroke()


		context_bottom.beginPath()
			.setLineDash(JSArray(5.0))
			.moveTo(middle_left)
			.lineTo(middle_right)
			.stroke()
			.setLineDash(JSArray())

		context_bottom.fillText("0", corner_bottom_left + Vector2D(-5, 18))
		context_bottom.fillText("Tp", middle_bottom + Vector2D(-10, 18))
		context_bottom.fillText("2Tp", corner_bottom_right + Vector2D(-15, 18))

		redraw_line(currentRedbarX)
		if ((currentRedbarX > corner_bottom_left.x && currentRedbarX < (corner_top_right.x - corner_top_left.x) / 2 * onOffBorder + corner_bottom_left.x)
			|| (currentRedbarX > (corner_top_right.x - corner_top_left.x) / 2 + corner_bottom_left.x && currentRedbarX < (corner_top_right.x - corner_top_left.x) / 2 + (corner_top_right.x - corner_top_left.x) / 2 * onOffBorder + corner_bottom_left.x))
			callback.onClick(closed = true)
		else
			callback.onClick(closed = false)
	}

	/** Redraws the red line
	  *
	  * @param position where to draw the line
	  */
	private def redraw_line(position: Double): Unit = {
		context_top.clearRect(0, 0, context_bottom.canvas.width, context_bottom.canvas.height)
		context_top.beginPath()
			.moveTo(position, corner_top_left.y)
			.lineTo(position, corner_bottom_left.y)
			.stroke()
		currentRedbarX = position
	}

	/** Utility function to check mouse pointer location
	  *
	  * @param vector2D	mouse location
	  * @return			whether mouse is inside graphing area or not
	  */
	private def insideGraph(vector2D: Vector2D): Boolean = {
		(vector2D.x > corner_bottom_left.x
			&& vector2D.x < corner_bottom_right.x
			&& vector2D.y > corner_top_left.y
			&& vector2D.y < corner_bottom_left.y)
	}
}

/** Simple interface for the callback method. */
trait Graph1Callback {
	def onClick(closed: Boolean): Unit
}