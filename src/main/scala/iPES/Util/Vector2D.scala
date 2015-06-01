package iPES.Util

case class Vector2D(x: Double, y: Double) {
  def +(that: Vector2D) = {
    require(that != null, "NullPointer exception")
    new Vector2D(this.x + that.x, this.y + that.y)
  }

  def -(that: Vector2D) = {
    require(that != null, "NullPointer exception")
    this + new Vector2D(-that.x, -that.y)
  }

  def *(that: Vector2D) = {
    require(that != null, "NullPointer exception")
    this.x*that.x + this.y*that.y
  }

  def *(that: Double) = {
    new Vector2D(that*this.x, that*this.y)
  }

  def abs = {
    Math.sqrt(x*x + y*y)
  }

  def angleTo(that: Vector2D) = {
    require(that != null, "NullPointer exception")
    Math.acos(this*that/(this.abs*that.abs))
  }

  def /(that: Double) = {
    require(that != 0, "Division by zero")
    new Vector2D(this.x / that, this.y / that)
  }

  def perpendicular = {
    if (this.x == 0)
      new Vector2D(-this.y, this.x)
    else
      new Vector2D(this.y, -this.x)
  }
}
