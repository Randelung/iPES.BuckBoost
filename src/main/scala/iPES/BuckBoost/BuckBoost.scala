package iPES.BuckBoost

import iPES.Parts._
import iPES.Util._
import iPES.Webpage.LanguageSelector
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.raw.HTMLDivElement
import org.scalajs.jquery.jQuery

import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

@JSExport
object BuckBoost {
    @JSExport
    def main(): Unit = {

        val languageSelectorDiv = dom.document.createElement("div").asInstanceOf[HTMLDivElement]
        new LanguageSelector(languageSelectorDiv)
        dom.document.body.appendChild(languageSelectorDiv)

        val circuit = div(
            margin := 10,
            width := 400,
            height := 200).render

        val graph1 = div(
            margin := 10,
            width := 400,
            height := 250).render

        val graph2 = div(
            margin := 10,
            width := 400,
            height := 250).render

        dom.document.body.appendChild(circuit)
        jQuery("body").append("<div id=\"first line\" />")
        dom.document.body.appendChild(graph1)
        jQuery("body").append("<div id=\"second line\" />")
        dom.document.body.appendChild(graph2)

        val canvas_top_circuit = canvas(
            id := "canvas_top_circuit",
            position := "absolute",
            width := circuit.style.width,
            height := circuit.style.height
        )("Get a proper browser!").render
        val canvas_bottom_circuit = canvas(
            id := "canvas_bottom_circuit",
            position := "absolute",
            width := circuit.style.width,
            height := circuit.style.height
        )("Get a proper browser!").render

        canvas_top_circuit.onselectstart = (_: Any) => false

        canvas_top_circuit.onmousedown = (_: Any) => dom.document.onselectstart = (_: Any) => false

        canvas_top_circuit.onmouseup = (_: Any) => dom.document.onselectstart = (_: Any) => null

        dom.document.onmouseup = (_: Any) => dom.document.onselectstart = (_: Any) => null

        circuit.appendChild(canvas_bottom_circuit)
        circuit.appendChild(canvas_top_circuit)

        val drawing_width = circuit.style.width.dropRight(2).toInt // set coordinate system; width attribute only applies to <img>
        canvas_top_circuit.width = drawing_width
        canvas_bottom_circuit.width = drawing_width
        val drawing_height = circuit.style.height.dropRight(2).toInt
        canvas_top_circuit.height = drawing_height
        canvas_bottom_circuit.height = drawing_height

        val context_bottom = new CustomContext(canvas_bottom_circuit.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D])
        val context_top = new CustomContext(canvas_top_circuit.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D])
        val animator = new Animator(context_bottom, context_top)
        val inputHandler = new InputHandler(context_top)

        val x_factor = drawing_width / 100
        val y_factor = drawing_height / 100

        val top_corner_left = new Vector2D(5 * x_factor, 5 * y_factor)
        val bottom_corner_left = new Vector2D(5 * x_factor, 95 * y_factor)
        val top_corner_right = new Vector2D(95 * x_factor, 5 * y_factor)
        val bottom_corner_right = new Vector2D(95 * x_factor, 95 * y_factor)

        val top_middle_left = new Vector2D(35 * x_factor, 5 * y_factor)
        val top_middle_right = new Vector2D(65 * x_factor, 5 * y_factor)
        val bottom_middle_left = new Vector2D(35 * x_factor, 95 * y_factor)
        val bottom_middle_right = new Vector2D(65 * x_factor, 95 * y_factor)

        context_bottom.setLineWidth(2)
        val capacitor = new Capacitor(top_middle_right, bottom_middle_right, animator)
        val dcSource = new DCSource(top_corner_left, bottom_corner_left, animator)
        val diode = new Diode(top_middle_right, top_middle_left, animator)
        val inductor = new Inductor(top_middle_left, bottom_middle_left, animator)
        val resistor = new Resistor(top_corner_right, bottom_corner_right, animator)
        val switch = new Switch(top_corner_left, top_middle_left, animator, inputHandler)
        val wire1 = new Wire(top_middle_right, top_corner_right, animator)
        val wire2 = new Wire(bottom_middle_left, bottom_corner_left, animator)
        val wire3 = new Wire(bottom_middle_right, bottom_middle_left, animator)
        val wire4 = new Wire(bottom_corner_right, bottom_middle_right, animator)
        switch.setOnItemClickListener(new OnItemClickListener {
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

        animator animate()

        setup_graph1(graph1, graph2, switch)
    }

    def setup_graph1(div1: html.Div, div2: html.Div, switch: Switch): Unit = {

        val canvas_top_graph1 = canvas(
            id := "canvas_top_graph1",
            position := "absolute",
            width := div1.style.width,
            height := div1.style.height
        )("Get a proper browser!").render
        val canvas_bottom_graph1 = canvas(
            id := "canvas_bottom_graph1",
            position := "absolute",
            width := div1.style.width,
            height := div1.style.height
        )("Get a proper browser!").render

        div1.appendChild(canvas_bottom_graph1)
        div1.appendChild(canvas_top_graph1)

        val drawing_width = div1.style.width.dropRight(2).toInt // set coordinate system; width attribute only applies to <img>
        canvas_top_graph1.width = drawing_width
        canvas_bottom_graph1.width = drawing_width
        val drawing_height = div1.style.height.dropRight(2).toInt
        canvas_top_graph1.height = drawing_height
        canvas_bottom_graph1.height = drawing_height

        val context_bottom = new CustomContext(canvas_bottom_graph1.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D])
        val context_top = new CustomContext(canvas_top_graph1.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D])

        val graph = new Graph1(context_bottom, context_top, new Graph1Callback {
            override def onClick(closed: Boolean): Unit = {
                if (closed != switch.closed)
                    switch.onClick(null)
            }
        })

        setup_graph2(div2, graph)
    }

    def setup_graph2(div: html.Div, graph: Graph1): Unit = {

        val canvas_top_graph1 = canvas(
            id := "canvas_top_graph2",
            position := "absolute",
            width := div.style.width,
            height := div.style.height
        )("Get a proper browser!").render
        val canvas_bottom_graph1 = canvas(
            id := "canvas_bottom_graph2",
            position := "absolute",
            width := div.style.width,
            height := div.style.height
        )("Get a proper browser!").render

        div.appendChild(canvas_bottom_graph1)
        div.appendChild(canvas_top_graph1)

        val drawing_width = div.style.width.dropRight(2).toInt // set coordinate system; width attribute only applies to <img>
        canvas_top_graph1.width = drawing_width
        canvas_bottom_graph1.width = drawing_width
        val drawing_height = div.style.height.dropRight(2).toInt
        canvas_top_graph1.height = drawing_height
        canvas_bottom_graph1.height = drawing_height

        val context_bottom = new CustomContext(canvas_bottom_graph1.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D])
        val context_top = new CustomContext(canvas_top_graph1.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D])

        new Graph2(context_bottom, context_top, new Graph2Callback {
            override def onClick(modifier: Double): Unit = {
                graph.redraw_graph(modifier)
            }
        })
    }
}