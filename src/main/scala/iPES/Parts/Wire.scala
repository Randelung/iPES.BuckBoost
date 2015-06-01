package iPES.Parts

import iPES.Util.{InputHandler, Vector2D, Animatable, Animator}

case class Wire(start: Vector2D, end: Vector2D, animator: Animator, inputHandler: InputHandler = null) extends Animatable {
    if (inputHandler != null)
        inputHandler registerArea(this, start - Vector2D(10, 10), end + Vector2D(10, 10))
    animator.register(this)
}
