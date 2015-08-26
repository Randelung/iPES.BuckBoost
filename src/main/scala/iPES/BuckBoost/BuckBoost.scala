package iPES.BuckBoost

import iPES.Parts._
import iPES.Util._
import org.scalajs.dom
import org.scalajs.dom.html
import org.scalajs.dom.raw.{HTMLDivElement, HTMLInputElement}

import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

@JSExport
object BuckBoost {
    @JSExport
    def main(circuitDiv: HTMLDivElement, graph1Div: HTMLDivElement, graph2Div: HTMLDivElement): Unit = {

        /*val circuitDiv = div(
            display := "block",
            marginLeft := "auto",
            marginRight := "auto",
            marginTop := 10,
            marginBottom := 10,
            width := 400,
            height := 200).render

        val graph1Div = div(
            display := "block",
            marginLeft := "auto",
            marginRight := "auto",
            marginTop := 10,
            marginBottom := 10,
            width := 400,
            height := 250).render

        val graph2Div = div(
            display := "block",
            marginLeft := "auto",
            marginRight := "auto",
            marginTop := 10,
            marginBottom := 10,
            width := 400,
            height := 250).render

        dom.document.body.appendChild(circuitDiv)
        jQuery("body").append("<div id=\"first line\" />")
        dom.document.body.appendChild(graph1Div)
        jQuery("body").append("<div id=\"second line\" />")
        dom.document.body.appendChild(graph2Div)

        val qnaDiv = dom.document.createElement("div").asInstanceOf[HTMLDivElement]
        qnaDiv.setAttribute("id", "qna")
        dom.document.body.appendChild(qnaDiv)*/

        val computedStyle = dom.window.getComputedStyle(circuitDiv)
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
        println("debug marker 1")
        canvas_top_circuit.onselectstart = (_: Any) => false

        canvas_top_circuit.onmousedown = (_: Any) => dom.document.onselectstart = (_: Any) => false

        canvas_top_circuit.onmouseup = (_: Any) => dom.document.onselectstart = (_: Any) => null

        dom.document.onmouseup = (_: Any) => dom.document.onselectstart = (_: Any) => null

        circuitDiv.appendChild(canvas_bottom_circuit)
        circuitDiv.appendChild(canvas_top_circuit)
        println("debug marker 2")
        val drawing_width = computedStyle.width.dropRight(2).toInt // set coordinate system; width attribute only applies to <img>
        canvas_top_circuit.width = drawing_width
        canvas_bottom_circuit.width = drawing_width
        val drawing_height = computedStyle.height.dropRight(2).toInt
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

        val graph1 = setup_graph1(graph1Div, switch)
        val graph2 = setup_graph2(graph2Div, graph1)
    }

    def setup_graph1(div1: html.Div, switch: Switch) = {

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

    def setup_graph2(div: html.Div, graph: Graph1) = {

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

    @JSExport
    def checkAnswer1() = {
        if (dom.document.getElementById("answer4").asInstanceOf[HTMLInputElement].checked) {
            var feedback = dom.document.getElementById("feedback1right").asInstanceOf[HTMLDivElement]
            feedback.style.setProperty("display", "initial")
            feedback = dom.document.getElementById("feedback1wrong").asInstanceOf[HTMLDivElement]
            feedback.style.setProperty("display", "none")
        }
        else {
            var feedback = dom.document.getElementById("feedback1right").asInstanceOf[HTMLDivElement]
            feedback.style.setProperty("display", "none")
            feedback = dom.document.getElementById("feedback1wrong").asInstanceOf[HTMLDivElement]
            feedback.style.setProperty("display", "initial")
        }
    }
}