package iPES.Util

import org.scalajs.dom

import scala.collection.mutable

class Animator(bottomContext: CustomContext, topContext: CustomContext) {
    private var list = new mutable.MutableList[Animatable]
    private var tick: Int = 0

    def animate(): Unit = {
        dom.setInterval(() => animatorLoop(), Animator.timeInterval)
    }

    private def animatorLoop(): Unit = {
        topContext.clearRect(Vector2D(0, 0), Vector2D(bottomContext.canvas.width, bottomContext.canvas.height))
        tick = (tick + 1) % Animator.steps
        list foreach (_.animate(topContext, tick))
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
        list foreach (_.draw(bottomContext))
    }
}

object Animator {
    val steps = 100
    val timeInterval = 10
    val speedFactor = 30
}