package iPES.Util

import org.scalajs.dom

/**
 * Distributes clicks (and later maybe keyboard inputs) to the respective components, which then pass it on to the
 * circuit logic.
 * @param context	Context which is to be monitored for input
 */
class InputHandler(context: CustomContext) {
	private var items: Map[Animatable, (Vector2D, Vector2D)] = Map()

	context.canvas.onclick = (e: dom.MouseEvent) => eventHandler(e)

	/**
	 * Registers a component for input callbacks.
	 * @param item		The component that is to be registered
	 * @param cornerOne	One corner of rectangular area
	 * @param cornerTwo	Opposite corner of area
	 */
	def registerArea(item: Animatable, cornerOne: Vector2D, cornerTwo: Vector2D): Unit = {
		if (cornerOne.x < cornerTwo.x)
			if (cornerOne.y < cornerTwo.y)
				items += (item ->(cornerOne, cornerTwo))
			else
				items += (item ->(Vector2D(cornerOne.x, cornerTwo.y), Vector2D(cornerTwo.x, cornerOne.y)))
		else if (cornerOne.y < cornerTwo.y)
			items += (item ->(Vector2D(cornerTwo.x, cornerOne.y), Vector2D(cornerOne.x, cornerTwo.y)))
		else
			items += (item ->(cornerTwo, cornerOne))
	}

	/**
	 * Removes the callback from the registry.
	 * @param item	Component to be removed
	 */
	def unregisterArea(item: Animatable): Unit = items -= item

	private def eventHandler(e: dom.MouseEvent): Unit = {
		items foreach (i => {
			val (item: Animatable, (a: Vector2D, b: Vector2D)) = i
			if (a.x <= e.pageX - context.canvas.offsetLeft
				&& b.x >= e.pageX - context.canvas.offsetLeft
				&& a.y <= e.pageY - context.canvas.offsetTop
				&& b.y >= e.pageY - context.canvas.offsetTop) {
				item.onClick(Vector2D(e.clientX - context.canvas.getBoundingClientRect().left, e.clientY - context.canvas.getBoundingClientRect().top) - item.start)
				context.clearRect(a, b)
				e.stopPropagation()
				e.preventDefault()
			}
		})
	}
}
