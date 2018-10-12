import kotlinx.coroutines.experimental.runBlocking
import molecules.MoleculeApp
import org.openrndr.*
import org.openrndr.color.ColorRGBa

fun main(args: Array<String>) = runBlocking {
    application(MoleculeApp(),
            configuration {
                width = 800
                height = 800
            })
}