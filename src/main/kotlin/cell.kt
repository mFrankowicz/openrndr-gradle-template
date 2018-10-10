import org.openrndr.Program
import org.openrndr.math.Vector2
import org.openrndr.color.ColorHSLa
import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import org.openrndr.math.map
import org.openrndr.shape.Shape
import org.openrndr.shape.shape

data class Cell(var pos: Vector2, var color: ColorHSVa, var size: Double, var energy: Double = 0.0) {

    var connections = Connections()
    lateinit var neighs: NeighCell
    var connectionsCount = 0
    var maxConnections = 3
    fun getNeighCell(x: Int, y: Int, grid: Array<Array<Cell>>) {
        neighs = NeighCell(grid[x-1][y-1], grid[x][y-1], grid[x+1][y-1],
                grid[x-1][y], grid[x+1][y],
                grid[x-1][y+1], grid[x][y+1], grid[x+1][y+1])

    }

    fun calculateHarmonics() {

    }

    fun updateColors() {
        color = ColorHSVa(map(0.0, 100.0, 0.0, 360.0, energy), 1.0, 1.0)
    }

    fun generateConnections(code: String) {
        if(connectionsCount <= maxConnections) {
            if (code == "lt") connections.lt = true; connectionsCount++
            if (code == "t") connections.t = true; connectionsCount++
            if (code == "rt") connections.rt = true; connectionsCount++
            if (code == "l") connections.l = true; connectionsCount++
            if (code == "r") connections.r = true; connectionsCount++
            if (code == "lb") connections.lb = true; connectionsCount++
            if (code == "b") connections.b = true; connectionsCount++
            if (code == "rb") connections.rb = true; connectionsCount++
        }
    }

    fun generateRandomConnections() {
        val codes = arrayOf("lt", "t", "rt", "l", "r", "lb", "b", "rb")
        for(i in 0 until maxConnections) {
            val random = map(0.0, 1.0, 0.0, 7.0, Math.random()).toInt()
            generateConnections(codes[random])
            connectionsCount++
        }
    }

    fun connectExtrems() {

        if(neighs.lt.connections.rb) connections.lt = true
        if(neighs.t.connections.b) connections.t = true
        if(neighs.rt.connections.lb ) connections.rt = true
        if(neighs.l.connections.r) connections.l = true
        if(neighs.r.connections.l) connections.r = true
        if(neighs.lb.connections.rt) connections.lb = true
        if(neighs.b.connections.t) connections.b = true
        if(neighs.rb.connections.lt) connections.rb = true

    }

    fun getShape(program: Program) {
        with(program) {
            if (connections.lt) {
                drawer.lineSegment(Vector2(0.0, 0.0),Vector2(-size, -size))
            }
            if (connections.t) {
                drawer.lineSegment(Vector2(0.0, 0.0),Vector2(0.0, -size))
            }
            if (connections.rt) {

                drawer.lineSegment(Vector2(0.0, 0.0),Vector2(size, -size))
            }
            if (connections.l) {

                drawer.lineSegment(Vector2(0.0, 0.0),Vector2(-size, 0.0))
            }
            if (connections.r) {

                drawer.lineSegment(Vector2(0.0, 0.0),Vector2(size, 0.0))
            }
            if (connections.lb) {

                drawer.lineSegment(Vector2(0.0, 0.0),Vector2(-size, size))
            }
            if (connections.b)  {

                drawer.lineSegment(Vector2(0.0, 0.0),Vector2(0.0, size))
            }

            if (connections.rb)  {

                drawer.lineSegment(Vector2(0.0, 0.0),Vector2(size, size))
            }
        }
    }
}

data class NeighCell(val lt: Cell, val t: Cell, val rt: Cell, val l: Cell, val r: Cell, val lb: Cell, val b: Cell, val rb: Cell)

data class Connections(var lt: Boolean = false, var t: Boolean = false, var rt: Boolean = false, var l: Boolean = false, var r: Boolean = false, var lb: Boolean = false, var b: Boolean = false, var rb: Boolean = false)