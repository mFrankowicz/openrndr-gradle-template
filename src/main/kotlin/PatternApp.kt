import ca.CA
import org.openrndr.Program
import org.openrndr.*
import org.openrndr.draw.Drawer
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.*
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.FontImageMap
import org.openrndr.math.Vector2
import org.openrndr.math.map
import qtree.Particle
import qtree.QPoint
import qtree.QRectangle
import qtree.Quadtree

class PatternApp: Program() {

    val gridSize = 50

    var caList = Array(gridSize) { x -> Array(gridSize) { y-> CA(0) }}
    var next = caList
    override fun setup() {

        caList.tl(15, 15, CA(1))
        caList.t(15, 15, CA(0))
        caList.l(15, 15, CA(1))

        caList.br(15, 15, CA(0))
        caList.b(15, 15, CA(1))
        caList.r(15, 15, CA(0))

        for(x in 0 until gridSize) {
            for (y in 0 until gridSize) {
                val r = Math.random()
                when(r) {
                    in 0.0..0.05 -> caList[x][y].state = 1
                    else -> caList[x][y].state = 0

                }

            }
        }


    }

    var counter = 1

    override fun draw() {

        if(counter % 3 == 0) {
            for (x in 1 until gridSize - 1) {
                for (y in 1 until gridSize - 1) {
                    //caList[x][y].changeState(x, y, caList)
                    when {
                        caList[x][y].firstSymetry(x, y, caList) -> caList[x][y].rotateLeft(x, y, caList)
                        caList[x][y].secondSymetry(x, y, caList) -> caList[x][y].rotateTwoLeft(x, y, caList)
                        caList[x][y].thirdSymetry(x, y, caList) -> caList[x][y].invertVertical(x, y, caList)
                        //caList[x][y].countNeighs(x, y, caList) in 3..7-> caList[x][y].rotateLeft(x, y, caList)
                        //caList[x][y].countNeighs(x, y, caList) >= 8 -> caList[x][y].changeState()
                    }

                    next[x][y] = caList[x][y]
                }
            }
        }
        counter++

        for(x in 0 until gridSize) {
            for (y in 0 until gridSize) {
                next[x][y].show(x * 13, y * 13, this)
            }
        }

        swap()


        /*val range = QRectangle(mouse.position.x, mouse.position.y, 50.0, 50.0)
        val points = qt.query(range, mutableListOf())
        println(points.size)
        qt.show(this)
        drawer.pushStyle()
        drawer.fill = ColorRGBa.TRANSPARENT
        drawer.strokeWeight = 1.0
        drawer.pushTransforms()
        drawer.translate(range.x, range.y)
        drawer.translate(-50.0, -50.0)
        drawer.rectangle(0.0, 0.0, range.w*2, range.h*2)
        drawer.popTransforms()
        drawer.popStyle()
        points.forEach {
            drawer.circle(Vector2(it.x, it.y), 5.0)
        }*/
    }

    fun swap() {
        val temp = next
        next = caList
        caList = next
    }


}

fun <T> Array<Array<T>>.tl(x: Int, y: Int, value: T){
    this[x-1][y-1] = value
}

fun <T> Array<Array<T>>.t(x: Int, y: Int, value: T){
    this[x][y-1] = value
}

fun <T> Array<Array<T>>.tr(x: Int, y: Int, value: T){
    this[x+1][y-1] = value
}

fun <T> Array<Array<T>>.l(x: Int, y: Int, value: T){
    this[x-1][y] = value
}

fun <T> Array<Array<T>>.c(x: Int, y: Int, value: T){
    this[x][y] = value
}

fun <T> Array<Array<T>>.r(x: Int, y: Int, value: T){
    this[x+1][y] = value
}

fun <T> Array<Array<T>>.bl(x: Int, y: Int, value: T){
    this[x-1][y+1] = value
}

fun <T> Array<Array<T>>.b(x: Int, y: Int, value: T){
    this[x][y+1] = value
}

fun <T> Array<Array<T>>.br(x: Int, y: Int, value: T){
    this[x+1][y+1] = value
}