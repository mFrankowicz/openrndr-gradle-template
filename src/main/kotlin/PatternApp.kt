import org.openrndr.Program
import org.openrndr.*
import org.openrndr.draw.Drawer
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.*
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.FontImageMap
import qtree.QPoint
import qtree.QRectangle
import qtree.Quadtree

class PatternApp: Program() {
    val qt = Quadtree(QRectangle(300.0, 300.0, 300.0, 300.0), 4)
    override fun setup() {
        for(i in 0..10) {
            qt.insert(QPoint(Math.random() * width, Math.random() * height))
        }

        println(qt.points)
//        println(qt.northeast.points)
//        println(qt.northwest.points)
//        println(qt.southeast.points)
//        println(qt.southwest.points)
    }

    override fun draw() {
        drawer.background(ColorRGBa.WHITE)
        qt.show(this)
    }


}