package iPES

import org.scalajs.dom

import scala.collection.mutable

class Animator(bottomContext: CustomContext, topContext: CustomContext) {
  private var list = new mutable.MutableList[Animatable]
  private var tick: Int = 0

  def animate(): Unit = {
    dom.setInterval(() => animatorLoop(), 250)
  }

  def register(that: Animatable) = {
    list += that
    that.draw(bottomContext)
  }

  def unregister(that: Animatable) = {
    list = list diff List(that)
    redraw()
  }

  def redraw(): Unit = {
    bottomContext.clearRect(Vector2D(0, 0), Vector2D(bottomContext.canvas.width, bottomContext.canvas.height))
    list foreach(_.draw(bottomContext))
  }

  private def animatorLoop(): Unit = {
    topContext.clearRect(Vector2D(0, 0), Vector2D(bottomContext.canvas.width, bottomContext.canvas.height))
    tick = (tick + 1) % 4
    list foreach(_.animate(topContext, tick))
  }
}