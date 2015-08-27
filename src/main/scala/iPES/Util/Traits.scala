package iPES.Util

import iPES.Util.FlowDirection.FlowDirection

/**
 * Callback interface for the input handler.
 */
trait OnItemClickListener {
	def onItemClick(position: Vector2D): Unit
}

/**
 * Callback interface for the animator.
 */
trait Animatable {
	/**
	 * Start and end point of component; needed by all components.
	 */
	val start, end: Vector2D
	var flowDirection: FlowDirection = FlowDirection.FORWARD
	var onItemClickListener: OnItemClickListener = null

	/**
	 * Register a callback for the input handler
	 * @param onItemClickListener	The listener to be registered
	 */
	def setOnItemClickListener(onItemClickListener: OnItemClickListener): Unit = {
		this.onItemClickListener = onItemClickListener
	}

	/**
	 * Passes on click events to registered click listener
	 * @param position	Position in which the click occurred
	 */
	def onClick(position: Vector2D): Unit = {
		if (onItemClickListener != null)
			onItemClickListener.onItemClick(position)
	}

	/**
	 * Reverses flow direction
	 */
	def reverseFlow(): Unit = {
		if (flowDirection == FlowDirection.STOP)
			return
		flowDirection = if (flowDirection == FlowDirection.FORWARD) FlowDirection.REVERSE else FlowDirection.FORWARD
	}

	/**
	 * Draw component, default is just a wire.
	 * @param context	Context in which to draw
	 */
	def draw(context: CustomContext): Unit = {
		context.beginPath()
			.moveTo(start)
			.lineTo(end)
			.stroke()
	}

	/**
	 * Callback to facilitate custom animations per component; default is like a wire.
	 * @param context	Context in which to draw.
	 * @param tick		Tick counter for which to draw.
	 */
	def animate(context: CustomContext, tick: Int): Unit = {
		if (flowDirection == FlowDirection.STOP)
			return
		val style = context.getFillStyle
		context.setFillStyle("#FFFF00")
		var e = (end - start) / (end - start).abs * Animator.speedFactor
		if (flowDirection == FlowDirection.FORWARD) {
			var pos = start + e * tick / Animator.steps
			while ((pos - start).abs < (end - start).abs) {
				context.beginPath()
					.arc(pos, 4, 0, 2 * Math.PI)
					.fill()
				pos += e
			}
		}
		if (flowDirection == FlowDirection.REVERSE) {
			var pos = end - e * tick / Animator.steps
			while ((pos - end).abs < (end - start).abs) {
				context.beginPath()
					.arc(pos, 4, 0, 2 * Math.PI)
					.fill()
				pos -= e
			}
		}
		context.setFillStyle(style)
	}
}