package iPES.Util

import iPES.Util.FlowDirection.FlowDirection

trait OnItemClickListener {
    def onItemClick(position: Vector2D): Unit
}

trait Animatable {
    var flowDirection : FlowDirection = FlowDirection.FORWARD
    var onItemClickListener: OnItemClickListener = null

    def setOnItemClickListener(onItemClickListener: OnItemClickListener): Unit = {
        this.onItemClickListener = onItemClickListener
    }

    def onClick(position: Vector2D): Unit = {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(position)
    }

    def reverseFlow(): Unit = {
        if (flowDirection == FlowDirection.STOP)
            return
        flowDirection = if (flowDirection == FlowDirection.FORWARD) FlowDirection.REVERSE else FlowDirection.FORWARD
    }

	val start, end : Vector2D

	def draw(context: CustomContext): Unit = {
        context.beginPath()
            .moveTo(start)
            .lineTo(end)
            .stroke()
    }

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