package iPES

trait iPESLogic {
  final val FORWARD = 1
  final val STOP = 0
  final val REVERSE = -1
}

trait Animatable extends iPESLogic {
  def draw(context: CustomContext): Unit
  def animate(context: CustomContext, tick: Int): Unit
}

trait Clickable extends Animatable {
  def onClick(position: Vector2D): Unit

  val a: Vector2D
  val b: Vector2D
}