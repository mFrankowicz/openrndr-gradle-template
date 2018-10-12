package ca

import org.openrndr.Program
import org.openrndr.color.ColorRGBa
import org.openrndr.math.map

data class CA(var state: Int = 0) {

    fun changeState() {
       state = 0
    }

    fun show(x: Int, y: Int, program: Program) {
        with(program) {
            when(state) {
                0 -> drawer.fill = ColorRGBa.GREEN
                1  -> drawer.fill = ColorRGBa.RED
            }
            drawer.rectangle(x.toDouble(), y.toDouble(), 13.0, 13.0)
        }
    }

    private fun getNeigh(x: Int, y: Int, others: Array<Array<CA>>): List<CA> {

        val ca1 = others[x-1][y-1]
        val ca2 = others[x][y-1]
        val ca3 = others[x+1][y-1]
        val ca4 = others[x-1][y]
        val ca5 = others[x+1][y]
        val ca6 = others[x-1][y+1]
        val ca7 = others[x][y+1]
        val ca8 = others[x+1][y+1]

        return listOf(ca1, ca2, ca3, ca4, ca5, ca6, ca7, ca8)

    }

    fun firstSymetry(x: Int, y: Int, others: Array<Array<CA>>): Boolean {
        var neigh = getNeigh(x, y, others)
        return neigh[0] != neigh[7] && neigh[1] != neigh[6] && neigh[3] != neigh[4]
    }

    fun secondSymetry(x: Int, y: Int, others: Array<Array<CA>>): Boolean {
        var neigh = getNeigh(x, y, others)
        return neigh[0] != neigh[3] && neigh[1] != neigh[6] && neigh[2] != neigh[7]
    }

    fun thirdSymetry(x: Int, y: Int, others: Array<Array<CA>>): Boolean {
        var neigh = getNeigh(x, y, others)
        return neigh[0] != neigh[2] && neigh[3] != neigh[4] && neigh[5] != neigh[7]
    }

    fun countNeighs(x: Int, y: Int, others: Array<Array<CA>>): Int {
        var neigh = getNeigh(x, y, others)
        return neigh.map { it.state }.fold(0) { total, next -> next + total}
    }

    fun rotateLeft(x: Int, y: Int, others: Array<Array<CA>>) {
        rotateIndex(x, y, others, 1, 2, 3, 0, 7, 3, 5, 6)
    }

    fun rotateTwoLeft(x: Int, y: Int, others: Array<Array<CA>>) {
        rotateIndex(x, y, others, 2, 4, 7, 1, 6, 0, 3, 5)
    }

    fun invertVertical(x: Int, y: Int, others: Array<Array<CA>>) {
        rotateIndex(x, y, others, 5, 6,  7, 4, 3, 0, 1, 2)
    }

    fun randomize(x: Int, y: Int, others: Array<Array<CA>>) {
        val rnd = mutableListOf<Int>()
        while (rnd.size < 8) {
            //println(rnd.size)
            val r = map(0.0, 1.0, 0.0, 8.0, Math.random()).toInt()
            if(!rnd.contains(r))
                rnd.add(r)

        }
        rotateIndex(x, y, others, rnd[0], rnd[1], rnd[2], rnd[3], rnd[4], rnd[5], rnd[6], rnd[7])
    }

    fun rotateIndex(x: Int, y: Int, others: Array<Array<CA>>, a: Int, b: Int, c: Int, d: Int, e: Int, f: Int, g: Int, h: Int) {
        val neigh = getNeigh(x, y, others)
        others[x-1][y-1] = neigh[a]
        others[x][y-1] = neigh[b]
        others[x+1][y-1] = neigh[c]
        others[x-1][y] = neigh[d]
        others[x+1][y] = neigh[e]
        others[x-1][y+1] = neigh[f]
        others[x][y+1] = neigh[g]
        others[x+1][y+1] = neigh[h]
//        state = when(state) {
//            1 -> 0
//            else -> 1
//        }
    }
}