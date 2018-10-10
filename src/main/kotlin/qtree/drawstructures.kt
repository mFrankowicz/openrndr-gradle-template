package qtree

data class QPoint(var x : Double, var y: Double)

data class QRectangle(var x: Double, var y: Double, var w: Double, var h: Double) {

    fun contains(point: QPoint) : Boolean = point.x >= x - w && point.x <= x + w && point.y >= y - h && point.y <= y + h

}