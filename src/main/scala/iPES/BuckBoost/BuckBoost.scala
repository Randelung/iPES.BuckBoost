package iPES.BuckBoost

import iPES.{Util, CustomContext, Vector2D}

import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import dom.document
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
    context_bottom.beginPath()
      .moveTo(bottom_corner_left)
      .lineTo(bottom_corner_right)
      .moveTo(top_corner_right)
      .lineTo(top_middle_right)
      .stroke()
    Util.drawDiode(context_bottom, top_middle_right, top_middle_left)
    Util.drawResistor(context_bottom, top_middle_left, bottom_middle_left)
    Util.drawCapacitor(context_bottom, top_middle_right, bottom_middle_right)
  }
}