package iPES.Util

import org.scalajs.dom

import scala.collection.mutable

/**
 * Coordination of animations for circuit diagrams.<br />
 * <br />
 * Circuit elements may register themselves here, and the animator will trigger each animation callback once a tick.
 * Elements may also unregister themselves.<br />
 * <br />
 * When instantiating the class, two canvas contexts need to be supplied, one for the mostly unchanging circuitry, and
 * one where the actual animation takes place.
 *
 * @param bottomContext	Canvas context that remains mostly the same, i.e. the circuit symbols
 * @param topContext	Canvas context for current animtaion
 */
class Animator(bottomContext: CustomContext, topContext: CustomContext) {
    private var list = new mutable.MutableList[Animatable]
    private var tick: Int = 0

    /** Starts the animation loop, in constant time intervals &mdash; only call once! */
    def animate(): Unit = {
        dom.setInterval(() => animatorLoop(), Animator.timeInterval)
    }

    private def animatorLoop(): Unit = {
        topContext.clearRect(Vector2D(0, 0), Vector2D(bottomContext.canvas.width, bottomContext.canvas.height))
        tick = (tick + 1) % Animator.steps
        list foreach (_.animate(topContext, tick))
    }

    /** Allows circuit components to register themselves for animation. */
    def register(that: Animatable) = {
        list += that
        that.draw(bottomContext)
    }

    /** Removes circuit element from the animation queue. */
    def unregister(that: Animatable) = {
        list = list diff List(that)
        redraw()
    }

    /** Redraws each registered circuit element, like in case of switch trigger. */
    def redraw(): Unit = {
        bottomContext.clearRect(Vector2D(0, 0), Vector2D(bottomContext.canvas.width, bottomContext.canvas.height))
        list foreach (_.draw(bottomContext))
    }
}

/** some constants, will probably change in the future towards a grid-based system. */
object Animator {
    val steps = 100
    val timeInterval = 10
    val speedFactor = 30
}