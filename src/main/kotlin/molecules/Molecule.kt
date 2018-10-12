package molecules

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.left
import arrow.data.valid
import org.openrndr.Program
import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector2
import org.openrndr.math.map
import kotlin.math.roundToInt

data class Molecule(val factor: Int, val generation: Int, val capacity: Int, var position: Vector2) {

    var subdivided = false
    var validLR: Pair<Boolean, Boolean> = Pair(false, false)
    var validLLoc = Pair(0, 0)
    var validRLoc = Pair(0, 0)

    val charges = mutableListOf<Charge>()

    var insertLeftThenRight = false

    lateinit var left: Molecule
    lateinit var right: Molecule

    val offset = offsetMain

//    fun insert(charge: Charge): Boolean {
//
//        return if(charges.size < capacity) {
//            charges.add(charge)
//            true
//        } else {
//            if(!subdivided) subdvide()
//
//            insertLeftThenRight = when(insertLeftThenRight) {
//                true -> false
//                else -> true
//            }
//
//            when {
//                left.insert(charge) && countCharges() -> true
//                right.insert(charge)&& !countCharges() -> true
//                else -> false
//            }
//        }
//    }

    fun insert(charge: Charge, x: Int, y: Int, grid: Array<Array<Option<Molecule>>>): Boolean {

        return if(charges.size < capacity) {
            charges.add(charge)
            true
        } else {
            if(!subdivided) subdvide(x, y, grid)

            insertLeftThenRight = when(insertLeftThenRight) {
                true -> false
                else -> true
            }
            when {
                validLR.first && countCharges() && left.insert(charge, validLLoc.first.coerceIn(1 until grid.size-1), validLLoc.second.coerceIn(1 until grid.size-1), grid) -> true
                validLR.second && !countCharges() && right.insert(charge, validLLoc.first.coerceIn(1 until grid.size-1), validLLoc.second.coerceIn(1 until grid.size-1), grid) -> true
                else -> false
            }
        }
    }

    fun findEmptySpace(x: Int, y: Int, grid: Array<Array<Option<Molecule>>>) : Option<Pair<Int, Int>> {
        val neight = listOf(
                Pair(grid[x-1][y-1], Pair(x-1, y-1)),
                Pair(grid[x][y-1], Pair(x, y-1)),
                Pair(grid[x+1][y-1], Pair(x+1, y-1)),
                Pair(grid[x-1][y], Pair(x-1, y)),
                Pair(grid[x+1][y], Pair(x+1, y)),
                Pair(grid[x-1][y+1], Pair(x-1, y+1)),
                Pair(grid[x][y+1], Pair(x, y+1)),
                Pair(grid[x+1][y+1], Pair(x+1, y+1)))

        var emptyCount = 0
        neight.forEach {
            if(it.first == None) emptyCount++
        }
        if(emptyCount < 1) {
            println("Full")
            return None
        }

        val searchRandom = map(0.0, 1.0, 0.0, 7.0, Math.random()).roundToInt()
        return if(neight[searchRandom].first == None) {
             println("found ${neight[searchRandom].first}")
             Some(neight[searchRandom].second)
        } else {
            findEmptySpace(x, y, grid)
        }
    }


//    fun subdvide(){
//
//        left = when {
//            factor > 1 -> Molecule(factor / 2, generation, capacity, position.let {it.copy( x = it.x - horizontalOffset, y = it.y + verticalOfsset)})
//            else -> Molecule(4, generation + 1, capacity, position.let {it.copy( x = it.x - horizontalOffset, y = it.y + verticalOfsset)})
//        }
//
//        right = when {
//            factor > 1 -> Molecule(factor / 2, generation, capacity, position.let {it.copy( x = it.x + horizontalOffset, y = it.y + verticalOfsset)} )
//            else -> Molecule(4, generation + 1, capacity, position.let {it.copy( x = it.x + horizontalOffset, y = it.y + verticalOfsset)})
//        }
//
//        subdivided = true
//
//    }

    fun subdvide(x: Int, y: Int, grid: Array<Array<Option<Molecule>>>){

        val spaceL = findEmptySpace(x, y, grid)

        when(spaceL) {
            is Some -> {
                left = when {
                    factor > 1 -> Molecule(factor / 2, generation, capacity, Vector2(spaceL.t.first.toDouble() * offset, spaceL.t.second.toDouble()* offset))
                    else -> Molecule(8, generation + 1, capacity, Vector2(spaceL.t.first.toDouble()* offset, spaceL.t.second.toDouble()* offset))
                }
                grid[spaceL.t.first][spaceL.t.second] = Some(left)
                validLLoc = Pair(spaceL.t.first, spaceL.t.second)
            }
        }

        val spaceR = findEmptySpace(x, y, grid)
        when(spaceR) {
            is Some -> {
                right = when {
                    factor > 1 -> Molecule(factor / 2, generation, capacity, Vector2(spaceR.t.first.toDouble()* offset, spaceR.t.second.toDouble()* offset))
                    else -> Molecule(8, generation + 1, capacity, Vector2(spaceR.t.first.toDouble()* offset, spaceR.t.second.toDouble()* offset))
                }
                grid[spaceR.t.first][spaceR.t.second] = Some(right)
                validRLoc = Pair(spaceR.t.first, spaceR.t.second)
            }
        }
        subdivided = true
        validLR = Pair(spaceL != None, spaceR != None)

    }

    fun countCharges(): Boolean {
        var positives = 0
        var negatives = 0
        charges.forEach {
            when {
                it.pole == Pole.POSITIVE -> positives++
                it.pole == Pole.NEGATIVE -> negatives++
                else -> positives or negatives
            }
        }

        return positives > negatives
    }

    fun print() {
//        if(subdivided) {
//            println(left)
//            left.print()
//            println("""
//                ------
//                ------
//            """.trimIndent())
//            println(right)
//            right.print()
//        }
    }

}

data class Charge(val pole: Pole)

enum class Pole {
    POSITIVE,
    NEGATIVE;
}