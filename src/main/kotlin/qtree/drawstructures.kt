package qtree

import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolated
import org.openrndr.math.Vector2
import org.openrndr.math.map
import org.openrndr.shape.Rectangle

data class QPoint(var x : Double, var y: Double)

data class QRectangle(var x: Double, var y: Double, var w: Double, var h: Double) {

    fun intersects(range: QRectangle): Boolean = !(range.x - range.w > x  + w || range.x + range.w < x - w || range.y - range.h > y + h || range.y  + range.h < y - h)

    fun contains(point: QPoint) : Boolean = point.x >= x - w && point.x <= x + w && point.y >= y - h && point.y <= y + h

}

data class Particle(var x: Double, var y: Double, var r: Double, var highlight : Boolean = false) {



    companion object {

        fun buildRandom(r: Double): Particle {
            val x = map(0.0, 1.0, 200.0, 230.0, Math.random())
            val y = map(0.0, 1.0, 200.0, 230.0, Math.random())
            return Particle(x, y, r)
        }

    }

    fun move() {
        x += map(0.0, 1.0, -1.0, 1.0, Math.random())
        y += map(0.0, 1.0, -1.0, 1.0, Math.random())
    }

    private fun intersectsOther(other: Particle): Boolean = dist(x, y, other.x, other.y) < (r + other.r)

    private fun intersects(others: Array<Particle>): Boolean {
        var b = false
        for(i in 0 until others.size) {
            b =  !this.equals(others[i]) && intersectsOther(others[i])
        }
        return b
    }

    fun show(program: Program, others: Array<Particle>) {
        with(program) {
            if(intersects(others)) {
                drawer.fill = ColorRGBa.PINK
            } else {
                drawer.fill = ColorRGBa.GREEN
            }
//            drawer.pushTransforms()
//
//            drawer.translate(x, y)

            drawer.circle(Vector2(x, y), r*2)
            drawer.circle(Vector2(x, y), r-1)
            others.forEach {
                drawer.lineSegment(x, y, it.x, it.y)
            }

            //drawer.popTransforms()
        }
    }

}