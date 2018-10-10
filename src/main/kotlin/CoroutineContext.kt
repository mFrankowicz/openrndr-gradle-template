import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.cancelChildren
import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.yield
import org.openrndr.Program
import org.openrndr.color.ColorHSVa
import org.openrndr.math.map
import org.openrndr.shape.Rectangle

class CoroutineContext {
    fun CoroutineScope.produceComputation() = produce {

        while(true) {
            val list = Array(100) {x -> Array(100) {y ->  Math.random() * 10}}
            send(list)
        }
    }

    fun CoroutineScope.square(numbers: ReceiveChannel<Array<Array<Double>>>): ReceiveChannel<MutableList<Rectangle>> = produce {
        val recList = mutableListOf<Rectangle>()

            numbers.receive().forEachIndexed { x, list ->
                list.forEachIndexed { y, numbers ->
                    recList.add(Rectangle(x * 10.0, y * 10.0, numbers, numbers))
                }
            }
        send(recList)

    }

    fun consumeComputation(program: Program) = runBlocking {
        val computation = produceComputation()
        val result = square(computation)
        program.drawer.rectangles(result.receive())

        coroutineContext.cancelChildren()

    }

}