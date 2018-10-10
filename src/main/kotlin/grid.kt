import org.openrndr.Program
import org.openrndr.color.ColorHSLa
import org.openrndr.color.ColorHSVa
import org.openrndr.math.Quaternion
import org.openrndr.math.Vector2
import org.openrndr.shape.Rectangle
import org.openrndr.math.transforms.transform

class Grid {

    private val xCount = 40
    private val yCount = 40
    private val cellSize = 10.0
    private val cellOffset = cellSize + 2.0

    lateinit var cellGrid : Array<Array<Cell>>

    fun setupGrid() {

        cellGrid = cell2dim(xCount, yCount, cellOffset, cellOffset, cellSize)

        for(x in 1 until xCount-1) {
            for(y in 1 until yCount-1) {
                cellGrid[x][y].getNeighCell(x, y, cellGrid)
                cellGrid[x][y].connectExtrems()
            }
        }
        cellGrid[20][20].generateRandomConnections()


    }

    fun createGrid(program: Program) {

        with(program) {

            for (x in 0 until xCount) {
                for (y in 0 until yCount) {
                    drawer.fill = cellGrid[x][y].color.toRGBa()
                    drawer.pushTransforms()
                    drawer.translate(cellGrid[x][y].pos.x + 30, cellGrid[x][y].pos.y + 30)
                    drawer.translate(-5.0, -5.0)
                    cellGrid[x][y].getShape(this)
                   // drawer.rectangle(0.0, 0.0, cell.size, cell.size)
                    drawer.popTransforms()


                }
            }

            //this.drawer.rectangles((0 until indexes).map { Rectangle(cellList[it].pos.x, cellList[it].pos.y, cellList[it].size, cellList[it].size) })
            /*cellList.forEachIndexed { index, cell ->
                cell.dr += 10
                drawer.fill = cellList[index].color.toRGBa()
                drawer.strokeWeight = 0.0
                drawer.pushTransforms()
                drawer.model *= transform {
                    translate(cellList[index].pos)
                    rotate(Quaternion.fromAngles(0.0,0.0,cellList[index].dr))
                    translate(Vector2(-5.0, -5.0))
                }
                drawer.rectangle(0.0, 0.0, cellList[index].size, cellList[index].size)
                drawer.popTransforms()
            }*/
        }

    }

    fun update() {
        for(x in 1 until xCount-1) {
            for(y in 1 until yCount-1) {
                cellGrid[x][y].getNeighCell(x, y, cellGrid)
                cellGrid[x][y].connectExtrems()
                //cellGrid[x][y].generateRandomConnections()
            }
        }
        for(x in 19..21) {
            for(y in 19..21) {
                println("x: $x, y: $y = ${cellGrid[x][y].connections}")
            }
        }

    }

    private fun random(): Double {
        return Math.random()
    }

    private fun random(min : Double, max: Double): Double {
        return Math.random()
    }

}

fun vector22dim(xSize: Int, ySize: Int) : Array<Array<Vector2>> {
    return Array(xSize) { x -> Array(ySize) { y -> Vector2(0.0, 0.0)}}
}

fun vector22dim(xSize: Int, ySize: Int, xOffset: Double, yOffset: Double = xOffset) : Array<Array<Vector2>> {
    return Array(xSize) { x -> Array(ySize) { y -> Vector2(x * xOffset, y * xOffset)}}
}

fun cell2dim(xSize: Int, ySize: Int, xOffset: Double, yOffset: Double, size : Double) : Array<Array<Cell>> {
    return Array(xSize) {
        x -> Array(ySize) {
        y -> Cell(Vector2(x * xOffset, y * yOffset), ColorHSVa(20.0, 1.0, 0.9), size)} }
}

