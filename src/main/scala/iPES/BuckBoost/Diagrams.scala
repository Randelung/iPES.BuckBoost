package iPES.BuckBoost

import iPES.Parts._
import iPES.Util._
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.raw.{HTMLDivElement, MouseEvent}

import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

/** Example class that demonstrates the usage of the framework so far.
  *
  * \@JSExport tells the compiler that this object can't be renamed.
  * It can now be called as `iPES.BuckBoost.BuckBoost().main(div, div, div)`.
  * Since it's an object instantiation should not be possible, assuming scala.js does its job.
  * This is the main class, which contains all content other than translatable text.
  */
@JSExport
object Diagrams {

	private var instantiated = false

	/** Main method that is to be executed. Static functions are used, but dynamic instantiation from JS is also
	  * possible.
	  *
	  * @param circuitDiv   Div element in which the circuit diagram is going to be placed
	  * @param graph1Div    Div element in which the first graph will be placed
	  * @param graph2Div    Div element in which the second graph will be placed
	  */
	@JSExport
	def main(circuitDiv: HTMLDivElement, graph1Div: HTMLDivElement, graph2Div: HTMLDivElement): Unit = {

		// check for previous instantiation, just to be sure
		if (instantiated)
			return

		instantiated = true

		// get actual dimensions of element, including CSS
		val computedStyle = dom.window.getComputedStyle(circuitDiv)
		// create new canvas elements using scalatags and give them appropriate attributes and style
		val canvas_top_circuit = canvas(
			id := "canvas_top_circuit",
			position := "absolute",
			width := computedStyle.width,
			height := computedStyle.height
		)("Get a proper browser!").render
		val canvas_bottom_circuit = canvas(
			id := "canvas_bottom_circuit",
			position := "absolute",
			width := computedStyle.width,
			height := computedStyle.height
		)("Get a proper browser!").render

		// prevent selection and cursor change on mouse clicks and drags
		canvas_top_circuit.onmousedown = (e: MouseEvent) => e.preventDefault()

		// add new canvases to div
		circuitDiv.appendChild(canvas_bottom_circuit)
		circuitDiv.appendChild(canvas_top_circuit)

		// adjust both canvas coordinate systems to match the one provided by the div
		val drawing_width = computedStyle.width.dropRight(2).toInt // set coordinate system; width attribute only applies to <img>
		canvas_top_circuit.width = drawing_width
		canvas_bottom_circuit.width = drawing_width
		val drawing_height = computedStyle.height.dropRight(2).toInt
		canvas_top_circuit.height = drawing_height
		canvas_bottom_circuit.height = drawing_height

		// get contexts of both canvases to draw on
		val context_bottom = new CustomContext(canvas_bottom_circuit.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D])
		val context_top = new CustomContext(canvas_top_circuit.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D])
		// initialize animation controller
		val animator = new Animator(context_bottom, context_top)
		// initialize input controller
		val inputHandler = new InputHandler(context_top)

		// factors to be able to draw in 100 pixel sizes and scale the image
		val x_factor = drawing_width / 100
		val y_factor = drawing_height / 100

		// some useful corner points
		val top_corner_left = new Vector2D(5 * x_factor, 5 * y_factor)
		val bottom_corner_left = new Vector2D(5 * x_factor, 95 * y_factor)
		val top_corner_right = new Vector2D(95 * x_factor, 5 * y_factor)
		val bottom_corner_right = new Vector2D(95 * x_factor, 95 * y_factor)

		// additional important points
		val top_middle_left = new Vector2D(35 * x_factor, 5 * y_factor)
		val top_middle_right = new Vector2D(65 * x_factor, 5 * y_factor)
		val bottom_middle_left = new Vector2D(35 * x_factor, 95 * y_factor)
		val bottom_middle_right = new Vector2D(65 * x_factor, 95 * y_factor)

		// draw all components
		context_bottom.setLineWidth(2)
		val capacitor = new Capacitor(top_middle_right, bottom_middle_right, animator)
		val dcSource = new DCSource(top_corner_left, bottom_corner_left, animator)
		val diode = new Diode(top_middle_right, top_middle_left, animator)
		new Inductor(top_middle_left, bottom_middle_left, animator)
		val resistor = new Resistor(top_corner_right, bottom_corner_right, animator)
		val switch = new Switch(top_corner_left, top_middle_left, animator, inputHandler)
		val wire1 = new Wire(top_middle_right, top_corner_right, animator)
		val wire2 = new Wire(bottom_middle_left, bottom_corner_left, animator)
		val wire3 = new Wire(bottom_middle_right, bottom_middle_left, animator)
		val wire4 = new Wire(bottom_corner_right, bottom_middle_right, animator)
		// register callback for switch clicks
		switch.setOnItemClickListener(new OnItemClickListener {
			/** Callback for the switch. Toggles switch state and changes circuit state.
			  *
			  * @param position position where the switch was clicked (unused)
			  */
			override def onItemClick(position: Vector2D): Unit = {
				if (switch.closed) {
					capacitor.reverseFlow()
					dcSource.flowDirection = FlowDirection.STOP
					diode.flowDirection = FlowDirection.FORWARD
					switch.closed = false
					wire2.flowDirection = FlowDirection.STOP
					wire3.flowDirection = FlowDirection.REVERSE
				}
				else {
					capacitor.reverseFlow()
					dcSource.flowDirection = FlowDirection.FORWARD
					diode.flowDirection = FlowDirection.STOP
					switch.closed = true
					wire2.flowDirection = FlowDirection.FORWARD
					wire3.flowDirection = FlowDirection.STOP
				}
				animator.redraw()
			}
		})

		diode.flowDirection = FlowDirection.STOP
		resistor.flowDirection = FlowDirection.REVERSE
		wire1.flowDirection = FlowDirection.REVERSE
		wire3.flowDirection = FlowDirection.STOP
		wire4.flowDirection = FlowDirection.REVERSE

		// start the electron dots
		animator animate()

		val graph1 = setup_graph1(graph1Div, switch)
		setup_graph2(graph2Div, graph1)
	}

	private def setup_graph1(div1: html.Div, switch: Switch) = {

		val computedStyle = dom.window.getComputedStyle(div1)
		val canvas_top_graph1 = canvas(
			id := "canvas_top_graph1",
			position := "absolute",
			width := computedStyle.width,
			height := computedStyle.height
		)("Get a proper browser!").render
		val canvas_bottom_graph1 = canvas(
			id := "canvas_bottom_graph1",
			position := "absolute",
			width := computedStyle.width,
			height := computedStyle.height
		)("Get a proper browser!").render

		div1.appendChild(canvas_bottom_graph1)
		div1.appendChild(canvas_top_graph1)

		val drawing_width = computedStyle.width.dropRight(2).toInt // set coordinate system; width attribute only applies to <img>
		canvas_top_graph1.width = drawing_width
		canvas_bottom_graph1.width = drawing_width
		val drawing_height = computedStyle.height.dropRight(2).toInt
		canvas_top_graph1.height = drawing_height
		canvas_bottom_graph1.height = drawing_height

		val context_bottom = new CustomContext(canvas_bottom_graph1.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D])
		val context_top = new CustomContext(canvas_top_graph1.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D])

		new Graph1(context_bottom, context_top, new Graph1Callback {
			override def onClick(closed: Boolean): Unit = {
				if (closed != switch.closed)
					switch.onClick(null)
			}
		})
	}

	private def setup_graph2(div: html.Div, graph: Graph1) = {

		val computedStyle = dom.window.getComputedStyle(div)
		val canvas_top_graph1 = canvas(
			id := "canvas_top_graph2",
			position := "absolute",
			width := computedStyle.width,
			height := computedStyle.height
		)("Get a proper browser!").render
		val canvas_bottom_graph1 = canvas(
			id := "canvas_bottom_graph2",
			position := "absolute",
			width := computedStyle.width,
			height := computedStyle.height
		)("Get a proper browser!").render

		div.appendChild(canvas_bottom_graph1)
		div.appendChild(canvas_top_graph1)

		val drawing_width = computedStyle.width.dropRight(2).toInt // set coordinate system; width attribute only applies to <img>
		canvas_top_graph1.width = drawing_width
		canvas_bottom_graph1.width = drawing_width
		val drawing_height = computedStyle.height.dropRight(2).toInt
		canvas_top_graph1.height = drawing_height
		canvas_bottom_graph1.height = drawing_height

		val context_bottom = new CustomContext(canvas_bottom_graph1.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D])
		val context_top = new CustomContext(canvas_top_graph1.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D])

		new Graph2(context_bottom, context_top, new Graph2Callback {
			override def onClick(modifier: Vector2D): Unit = {
				graph.redraw_graph(modifier.x, modifier.y)
			}
		})
	}
}