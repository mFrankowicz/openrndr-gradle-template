package qtree

import org.openrndr.Program
import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector2
import org.openrndr.math.map

data class Quadtree(var boundary: QRectangle, val capacity: Int, val dividedCount: Int) {

    val points = mutableListOf<QPoint>()
    var divided = false

    lateinit var northeast: Quadtree
    lateinit var northwest: Quadtree
    lateinit var southeast: Quadtree
    lateinit var southwest: Quadtree

    fun insert(point: QPoint): Boolean {

        if(!boundary.contains(point)) return false

        if(points.size < capacity) {
            points.add(point)
            return true
        } else {
            if(!divided) {
                subdvide()
            }

            return when {
                northeast.insert(point) -> true
                northwest.insert(point) -> true
                southeast.insert(point) -> true
                southwest.insert(point) -> true
                else -> false
            }

        }
    }

    private fun subdvide(){

        val x = boundary.x
        val y = boundary.y
        val h = boundary.h
        val w = boundary.w

        val ne = QRectangle(x + w/2, y - h/2, w / 2, h/2)
        northeast = Quadtree(ne, 4, dividedCount + 1)
        val nw = QRectangle(x - w/2, y - h/2, w / 2, h/2)
        northwest = Quadtree(nw, 4, dividedCount + 1)
        val se = QRectangle(x + w/2, y + h/2, w / 2, h/2)
        southeast = Quadtree(se, 4, dividedCount + 1)
        val sw = QRectangle(x - w/2, y + h/2, w / 2, h/2)
        southwest = Quadtree(sw, 4, dividedCount + 1)

        divided = true
    }

    fun query(range: QRectangle, found: MutableList<QPoint>): MutableList<QPoint> {

        if(!boundary.intersects(range)) {
            return mutableListOf()
        } else {
            points.forEach {
                if(range.contains(it)) {
                    found.add(it)
                }
            }
        }

        if(divided) {
            northeast.query(range, found)
            northwest.query(range, found)
            southwest.query(range, found)
            southeast.query(range, found)

        }

        return found
    }

    fun show(program: Program) {
        with(program) {
            drawer.pushTransforms()
            drawer.fill = ColorHSVa(map(0.0, 10.0, 0.0, 360.0, dividedCount.toDouble()), 0.8, 0.5).toRGBa()
            drawer.translate(boundary.x, boundary.y)
            drawer.translate(- (boundary.w),  - (boundary.h))
            drawer.strokeWeight = 0.0
            drawer.rectangle(0.0, 0.0, boundary.w*2, boundary.h*2)
            drawer.popTransforms()

            if(divided) {
                northeast.show(program)
                northwest.show(program)
                southeast.show(program)
                southwest.show(program)
//                println(northwest.points.count())
//                println(northeast.points.count())
//                println(southwest.points.count())
//                println(southeast.points.count())
//
//                println(northwest.dividedCount)
//                println(northeast.dividedCount)
//                println(southwest.dividedCount)
//                println(southeast.dividedCount)
            }

        }
    }

}