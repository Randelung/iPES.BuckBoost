package iPES.Util

import org.scalajs.dom

class InputHandler(context: CustomContext) {
    var items: Map[Animatable, (Vector2D, Vector2D)] = Map()

    context.canvas.onselectstart = (_: Any) => false

    context.canvas.onclick = (e: dom.MouseEvent) => eventHandler(e)

    def registerArea(item: Animatable, cornerOne: Vector2D, cornerTwo: Vector2D): Unit = {
        var topLeft, bottomRight: Vector2D = null
        if (cornerOne.x < cornerTwo.x)
            if (cornerOne.y < cornerTwo.y)
                items += (item -> (cornerOne, cornerTwo))
            else
                items += (item -> (Vector2D(cornerOne.x, cornerTwo.y), Vector2D(cornerTwo.x, cornerOne.y)))
        else
            if (cornerOne.y < cornerTwo.y)
                items += (item -> (Vector2D(cornerTwo.x, cornerOne.y), Vector2D(cornerOne.x, cornerTwo.y)))
            else
                items += (item -> (cornerTwo, cornerOne))
    }

    def unregisterArea(item: Animatable): Unit = items -= item

    def eventHandler (e: dom.MouseEvent): Unit =
    {
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
