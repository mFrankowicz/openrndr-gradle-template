import kotlinx.coroutines.experimental.runBlocking
import org.openrndr.*
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.math.Quaternion
import org.openrndr.math.Vector2
import org.openrndr.math.transforms.rotate
import org.openrndr.math.transforms.transform
import org.openrndr.shape.Color
import org.openrndr.shape.Rectangle
import org.openrndr.shape.contour

class TemplateProgram: Program() {

    var dt = 0.0

    val grid = Grid()

    override fun setup() {
        grid.setupGrid()
    }

    override fun draw() {
        drawer.background(ColorRGBa.WHITE)
        grid.update()
        grid.createGrid(this)

        /*drawer.rectangles((0 until 100).map { Rectangle(positions[it].plus(Vector2(dt, 0.0)).x, positions[it].y, 10.0, 10.0) })
        drawer.model *= transform {
            rotate(Quaternion.fromAngles(dt, dt, 0.0))
        }
        dt+= 0.1*/
    }

}

fun main(args: Array<String>) = runBlocking {
    application(PatternApp(),
            configuration {
                width = 600
                height = 600
            })
}