package iPES.Parts

import iPES.Util.{Vector2D, Animatable, Animator}

case class Wire(start: Vector2D, end: Vector2D, animator: Animator) extends Animatable {
    animator.register(this)
}
