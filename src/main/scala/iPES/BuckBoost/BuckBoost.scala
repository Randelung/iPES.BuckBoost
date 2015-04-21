package iPES.BuckBoost

import iPES.Parts._
import iPES._

import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import dom.document
import scalatags.JsDom.all._

@JSExport
object BuckBoost extends iPESLogic {
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
    val inputHandler = new InputHandler(context_top, animator)


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
    val wire1 = new Wire(top_middle_right, top_corner_right)
    animator register wire1
    val wire2 = new Wire(bottom_middle_left, bottom_corner_left)
    animator register wire2
    val wire3 = new Wire(bottom_middle_right, bottom_middle_left)
    animator register wire3
    val wire4 = new Wire(bottom_corner_right, bottom_middle_right)
    animator register wire4
    val diode = new Diode(top_middle_right, top_middle_left)
    animator register diode
    val resistor = new Resistor(top_middle_left, bottom_middle_left)
    animator register resistor
    val capacitor = new Capacitor(top_middle_right, bottom_middle_right)
    animator register capacitor
    val inductor = new Inductor(top_corner_right, bottom_corner_right)
    animator register inductor
    val dcSource = new DCSource(top_corner_left, bottom_corner_left)
    animator register dcSource
    val switch = new Switch(top_corner_left, top_middle_left)
    animator register switch
    inputHandler registerArea (switch, top_corner_left - Vector2D(0, 10), top_middle_left + Vector2D(0, 10))

    diode.flowDirection = REVERSE

    animator animate()
  }
}