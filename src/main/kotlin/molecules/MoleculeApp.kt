package molecules

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import org.openrndr.Program
import org.openrndr.color.ColorHSVa
import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector2
import org.openrndr.math.map
const val offsetMain = 20.0
class MoleculeApp: Program() {
    val indX = 20
    val indY = 20


    val molecule = Molecule(8, 1, 3, Vector2(indX * offsetMain, indY * offsetMain))
    var molGrid: Array<Array<Option<Molecule>>> = Array(40) {  Array(40) {Option.empty<Molecule>()}}
    override fun setup() {
        molGrid[indX][indY] = Some(molecule)
        var rl = true
        for(x in 1 until molGrid.size) {
            for(y in 1 until molGrid.size) {
                val rnd : Charge = if(rl) Charge(Pole.POSITIVE) else Charge(Pole.NEGATIVE)
                molecule.insert(rnd, indX, indY, molGrid)
                rl = when(rl) {
                    true -> false
                    else -> true
                }
            }
        }
        molGrid.forEach {
            it.forEach {
                when(it) {
                    is Some -> println(it.t)
                }
            }
        }
        molecule.print()
//        for(i in 1..100) {
//            val rnd : Charge = if(rl) Charge(Pole.POSITIVE) else Charge(Pole.NEGATIVE)
//            molecule.insert(rnd)
//            rl = when(rl) {
//                false || Math.random() < 0.1-> true
//                else -> false
//            }
//        }
//        molecule.print()

    }

    override fun draw() {

        //molecule.show(this)
        drawer.translate(15.0, 15.0)
        molGrid.forEach {
            it.forEach {
                when(it) {
                    is Some -> {
                        val colorGeneration = map(0.0, 30.0, 0.0, 360.0, it.t.generation.toDouble())
                        drawer.fill = ColorHSVa(colorGeneration, 1.0, 0.8).toRGBa()
                        drawer.stroke = ColorHSVa(colorGeneration, 1.0, 0.8).toRGBa()

                        when(it.t.factor) {
                            8 -> drawer.circle(it.t.position, 10.0)
                            4 -> drawer.circle(it.t.position, 7.0)
                            2 -> drawer.rectangle(it.t.position.x - 3.0, it.t.position.y - 3.0, 6.0, 6.0)
                            else -> drawer.circle(it.t.position, 3.0)
                        }

                        if(it.t.validLR.first) drawer.lineSegment(it.t.position, it.t.left.position)
                        if(it.t.validLR.second) drawer.lineSegment(it.t.position, it.t.right.position)
                    }
                }
            }
        }


    }



}