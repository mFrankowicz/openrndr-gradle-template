package qtree

import org.openrndr.Program
import org.openrndr.color.ColorRGBa

data class Quadtree(var boundary: QRectangle, val capacity: Int) {

    val points = mutableListOf<QPoint>()

    var divided = false

    lateinit var northeast: Quadtree
    lateinit var northwest: Quadtree
    lateinit var southeast: Quadtree
    lateinit var southwest: Quadtree

    fun insert(point: QPoint) {

        if(!boundary.contains(point)) return

        if(points.size < capacity) {
            points.add(point)
        } else {
            if(!divided) {
                subdvide()
            }

            northeast.insert(point)
            northwest.insert(point)
            southeast.insert(point)
            southwest.insert(point)
        }
    }

    private fun subdvide(){

        val x = boundary.x
        val y = boundary.y
        val h = boundary.h
        val w = boundary.w

        val ne = QRectangle(x + w/2, y - h/2, w / 2, h/2)
        northeast = Quadtree(ne, 4)
        val nw = QRectangle(x - w/2, y - h/2, w / 2, h/2)
        northwest = Quadtree(nw, 4)
        val se = QRectangle(x + w/2, y + h/2, w / 2, h/2)
        southeast = Quadtree(se, 4)
        val sw = QRectangle(x - w/2, y + h/2, w / 2, h/2)
        southwest = Quadtree(sw, 4)

        divided = true
    }

    fun show(program: Program) {
        with(program) {
            drawer.pushTransforms()
            drawer.fill = ColorRGBa.TRANSPARENT
            drawer.translate(boundary.x - (boundary.w/2), boundary.y - (boundary.h/2))
            drawer.strokeWeight = 1.0
            drawer.rectangle(0.0, 0.0, boundary.w*2, boundary.h*2)
            drawer.popTransforms()

            if(divided) {
                northeast.show(program)
                northwest.show(program)
                southeast.show(program)
                southwest.show(program)
            }

        }
    }

}