package iPES.Util

import org.scalajs.dom

class InputHandler(context: CustomContext) {
    var items: Map[Clickable, (Vector2D, Vector2D)] = Map()

    context.canvas.onclick = (e: dom.MouseEvent) => eventHandler(e)

    def registerArea(item: Clickable, topLeft: Vector2D, bottomRight: Vector2D): Unit = items += (item ->(topLeft, bottomRight))

    def unregisterArea(item: Clickable): Unit = items - item

    def eventHandler (e: dom.MouseEvent): Unit =
    {
        items foreach (i => {
            val (item: Clickable, (a: Vector2D, b: Vector2D)) = i
            if (a.x <= e.pageX - context.canvas.offsetLeft
                && b.x >= e.pageX - context.canvas.offsetLeft
                && a.y <= e.pageY - context.canvas.offsetTop
                && b.y >= e.pageY - context.canvas.offsetTop) {
                item.onClick(Vector2D(e.pageX - context.canvas.offsetLeft, e.pageY - context.canvas.offsetTop) - item.start)
                context.clearRect(a, b)
            }
        })
    }
}
