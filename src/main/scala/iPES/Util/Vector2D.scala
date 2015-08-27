package iPES.Util

/**
 * Wrapper class to make Point operations easier. Includes basic vector geometry methods.
 * @param x	X coordinate of vector
 * @param y	Y coordinate of vector
 */
case class Vector2D(x: Double, y: Double) {

	/**
	 * Subtracts the second vector from the first one
	 * @param that	Second vector
	 * @return		Resulting vector
	 */
	def -(that: Vector2D) = {
		require(that != null, "NullPointer exception")
		this + new Vector2D(-that.x, -that.y)
	}

	/**
	 * Adds two vectors
	 * @param that	Second vector
	 * @return		Resulting vector
	 */
	def +(that: Vector2D) = {
		require(that != null, "NullPointer exception")
		new Vector2D(this.x + that.x, this.y + that.y)
	}

	/**
	 * Scales the vector with factor `that`
	 * @param that	Scalar factor
	 * @return		Resulting vector
	 */
	def *(that: Double) = {
		new Vector2D(that * this.x, that * this.y)
	}

	/**
	 * Caluclates the angle to a second vector using the scalar product
	 * @param that	Second vector
	 * @return		Resulting value in rad
	 */
	def angleTo(that: Vector2D) = {
		require(that != null, "NullPointer exception")
		Math.acos(this * that / (this.abs * that.abs))
	}

	/**
	 * Takes the scalar product (dot product) with the second vector
	 * @param that	Second vector
	 * @return		Resulting value
	 */
	def *(that: Vector2D) = {
		require(that != null, "NullPointer exception")
		this.x * that.x + this.y * that.y
	}

	/**
	 * Returns the absolute length of the vector
	 * @return	Resulting value
	 */
	def abs = {
		Math.sqrt(x * x + y * y)
	}

	/**
	 * Scales the vector by a factor `1/that`
	 * @param that	Scalar factor
	 * @return		Resulting vector
	 */
	def /(that: Double) = {
		require(that != 0, "Division by zero")
		new Vector2D(this.x / that, this.y / that)
	}

	/**
	 * Returns the perpendicular vector in counterclockwise direction
	 * @return	Resulting vector
	 */
	def perpendicular = {
		new Vector2D(-this.y, this.x)
	}
}
