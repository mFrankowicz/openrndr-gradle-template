package qtree

import java.awt.geom.Point2D
import kotlin.math.sqrt

//fun dist(xa: Double, ya: Double, xb: Double, yb: Double) = sqrt(Math.pow(xa - xb, 2.0) + Math.pow(ya - yb, 2.0))

//fun dist(xa: Double, ya: Double, xb: Double, yb: Double) = Math.hypot(xa - xb, ya - yb)

fun dist(xa: Double, ya: Double, xb: Double, yb: Double) = Point2D.distance(xa, ya, xb, yb)