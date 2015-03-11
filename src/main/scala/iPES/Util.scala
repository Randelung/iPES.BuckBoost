package iPES

import sun.security.util.Length

object Util {
  def drawDiode(context: CustomContext, a: Vector2D, b: Vector2D, width: Double = 20): Unit = {
    val middle = (a + b) / 2
    val direction = (b - a) / (b - a).abs
    var w = width

    if (!((a - b).abs > 1.5 * width))
      w = 2d / 3 * (b - a).abs

    val point = middle + direction * Math.sqrt(3) / 4 * w
    val start = point + direction.perpendicular * w / 2
    val end = point - direction.perpendicular * w / 2
    val corner1 = middle - direction * Math.sqrt(3) / 4 * w + direction.perpendicular * w / 2
    val corner2 = corner1 - direction.perpendicular * w

    context.beginPath()
      .moveTo(a)
      .lineTo(b)
      .moveTo(start)
      .lineTo(end)
      .stroke()
      .moveTo(point)
      .stroke()
      .lineTo(corner1)
      .lineTo(corner2)
      .lineTo(point)
      .fill()
      .moveTo(b)
  }

  def drawResistor(context: CustomContext, a: Vector2D, b: Vector2D, width: Double = 20, lengthRatio: Double = 2) = {
    val middle = (b + a) / 2
    val direction = (b - a) / (b - a).abs
    var w = width

    if (!((a - b).abs > 1.5 * width * lengthRatio))
      w = 2d / 3 * (b - a).abs / lengthRatio

    val corner1 = middle + direction * w * lengthRatio / 2 + direction.perpendicular * w / 2
    val corner2 = corner1 - direction.perpendicular * w
    val corner3 = corner2 - direction * w * lengthRatio
    val corner4 = corner3 + direction.perpendicular * w

    context.beginPath()
      .moveTo(a)
      .lineTo(b)
      .stroke()
      .moveTo(corner1)
      .fillRect(corner1, corner3)
      .moveTo(b)
  }

  def drawCapacitor(context: CustomContext, a: Vector2D, b: Vector2D, width: Double = 20, lengthRatio: Double = 0.3) = {
    val middle = (b + a) / 2
    val direction = (b - a) / (b - a).abs
    var w = width

    if (!((a - b).abs > 1.5 * width * lengthRatio))
      w = 2d / 3 * (b - a).abs / lengthRatio

    val corner1 = middle + direction * w * lengthRatio / 2 + direction.perpendicular * w / 2
    val corner2 = corner1 - direction.perpendicular * w
    val corner3 = corner2 - direction * w * lengthRatio
    val corner4 = corner3 + direction.perpendicular * w

    val lineWidth = context.getLineWidth

    context.beginPath()
      .moveTo(a)
      .lineTo(middle - direction * w * lengthRatio / 2)
      .moveTo(middle + direction * w * lengthRatio / 2)
      .lineTo(b)
      .stroke()
      .beginPath()
      .setLineWidth(2 * lineWidth)
      .moveTo(corner1)
      .lineTo(corner2)
      .moveTo(corner3)
      .lineTo(corner4)
      .stroke()
      .setLineWidth(lineWidth)
      .moveTo(b)
  }

  def drawInductor(context: CustomContext, a: Vector2D, b: Vector2D, width: Double = 20, lengthRatio: Double = 2) = {
    val middle = (b + a) / 2
    val direction = (b - a) / (b - a).abs
    var w = width

    if (!((a - b).abs > 1.5 * width * lengthRatio))
      w = 2d / 3 * (b - a).abs / lengthRatio

    val corner1 = middle + direction * w * lengthRatio / 2 + direction.perpendicular * w / 2
    val corner2 = corner1 - direction.perpendicular * w
    val corner3 = corner2 - direction * w * lengthRatio
    val corner4 = corner3 + direction.perpendicular * w

    context.beginPath()
      .moveTo(a)
      .lineTo(middle - direction * w * lengthRatio / 2)
      .moveTo(middle + direction * w * lengthRatio / 2)
      .lineTo(b)
      .stroke()
      .strokeRect(corner1, corner3)
      .moveTo(b)
  }

  def drawDCSource(context: CustomContext, a: Vector2D, b: Vector2D, width: Double = 20, lengthRatio: Double = 0.3) = {
    val middle = (b + a) / 2
    val direction = (b - a) / (b - a).abs
    var w = width

    if (!((a - b).abs > 1.5 * width * lengthRatio))
      w = 2d / 3 * (b - a).abs / lengthRatio

    val corner1 = middle - direction * w * lengthRatio / 2 + direction.perpendicular * w
    val corner2 = corner1 - direction.perpendicular * w * 2
    val corner3 = corner2 + direction * w * lengthRatio + direction.perpendicular * w / 2
    val corner4 = corner3 + direction.perpendicular * w

    val lineWidth = context.getLineWidth

    context.beginPath()
      .moveTo(a)
      .lineTo(middle - direction * w * lengthRatio / 2)
      .moveTo(middle + direction * w * lengthRatio / 2)
      .lineTo(b)
      .stroke()
      .moveTo(corner3)
      .lineTo(corner4)
      .stroke()
      .beginPath()
      .setLineWidth(2 * lineWidth)
      .moveTo(corner1)
      .lineTo(corner2)
      .stroke()
      .setLineWidth(lineWidth)
      .moveTo(b)
  }

  def drawSwitch(context: CustomContext, a: Vector2D, b: Vector2D, closed: Boolean = true, length: Double = 20) = {
    val middle = (b + a) / 2
    val direction = (b - a) / (b - a).abs
    var w = length

    if (!((a - b).abs > 1.5 * length))
      w = 2d / 3 * (b - a).abs

    val start = middle - direction * w / 2
    val end = middle + direction * w / 2
    var open = end
    if (!closed)
      open = end + direction.perpendicular * w / 4

    val fillstyle = context.getFillStyle

    context.beginPath()
      .moveTo(a)
      .lineTo(start)
      .lineTo(open)
      .moveTo(end)
      .lineTo(b)
      .stroke()
      .moveTo(start)
      .beginPath()
      .setFillStyle("#ffffff")
      .arc(start, w / 10, 0, 2*Math.PI)
      .fill()
      .stroke()
      .moveTo(end)
      .beginPath()
      .arc(end, w / 10, 0, 2*Math.PI)
      .fill()
      .stroke()
      .setFillStyle(fillstyle)
      .moveTo(b)
  }
}