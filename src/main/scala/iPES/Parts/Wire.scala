package iPES.Parts

import iPES.Util.{Animatable, Animator, InputHandler, Vector2D}

/**
 * Most basic component. Since trait already includes the basic drawing and animation methods, they don't need to be
 * overridden.
 * @param start         Start vector of wire piece
 * @param end           End vector of wire piece
 * @param animator      Animator, in which to register itself
 * @param inputHandler  Input handler, in which to register itself. Can be omitted.
 */
case class Wire(start: Vector2D, end: Vector2D, animator: Animator, inputHandler: InputHandler = null) extends Animatable {
    if (inputHandler != null)
        inputHandler registerArea(this, start - Vector2D(10, 10), end + Vector2D(10, 10))
    animator.register(this)
}
