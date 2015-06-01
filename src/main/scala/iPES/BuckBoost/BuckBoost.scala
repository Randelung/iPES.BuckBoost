package iPES.BuckBoost

import javafx.css.StyleableObjectProperty

import iPES.Parts._
import iPES.Util._
import org.scalajs.dom
import org.scalajs.dom.document

import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

@JSExport
object BuckBoost {
    @JSExport
    def main(): Unit = {
        val canvas_width = 400
        val canvas_height = 400

        val canvas_top = canvas(
            id := "canvas_top",
            position := "absolute",
            left := "10px",
            top := "10px",
            width := canvas_width,
            height := canvas_height
        )("Get a proper browser!").render
        val canvas_bottom = canvas(
            id := "canvas_top",
            position := "absolute",
            left := "10px",
            top := "10px",
            width := canvas_width,
            height := canvas_height
        )("Get a proper browser!").render

        document.body.appendChild(
            div(
                canvas_bottom,
                canvas_top
            ).render
        )

        canvas_bottom.width = canvas_width
        canvas_bottom.height = canvas_height
        canvas_top.width = canvas_width
        canvas_top.height = canvas_height

        val drawing_width = canvas_width
        val drawing_height = 0.5 * drawing_width

        val context_bottom = new CustomContext(canvas_bottom.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D])
        val context_top = new CustomContext(canvas_top.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D])
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
        val inductor = new Inductor(top_corner_right, bottom_corner_right, animator)
        val resistor = new Resistor(top_middle_left, bottom_middle_left, animator)
        val switch = new Switch(top_corner_left, top_middle_left, animator, inputHandler)
        val wire1 = new Wire(top_middle_right, top_corner_right, animator)
        val wire2 = new Wire(bottom_middle_left, bottom_corner_left, animator)
        val wire3 = new Wire(bottom_middle_right, bottom_middle_left, animator)
        val wire4 = new Wire(bottom_corner_right, bottom_middle_right, animator)
        switch.setOnItemClickListener(new OnItemClickListener {
            override def onItemClick(clickable: Clickable): Unit = {
                if (clickable == switch) {
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
                        wire4.flowDirection = FlowDirection.REVERSE
                    }
                    animator.redraw()
                }
            }
        })

        diode.flowDirection = FlowDirection.STOP
        wire1.flowDirection = FlowDirection.REVERSE
        wire3.flowDirection = FlowDirection.STOP
        wire4.flowDirection = FlowDirection.REVERSE
        inductor.flowDirection = FlowDirection.REVERSE

        animator animate()
    }
}