import org.w3c.dom.*
import kotlin.browser.*

const val canvasSize = 700.0

fun main(args: Array<String>) {
    val (context, _) = canvas()
    renderRectangles(context, listOf(Rect(100.0, 200.0), Rect(300.0, 500.0)))
}

var scale = 1.0
var rotation = 0.deg

fun renderRectangles(context: CanvasRenderingContext2D, rectangles: List<Rect>) {

    window.requestAnimationFrame {
        clearCanvas(context)
        updateRotation()
        updateScale()

        for (rect in rectangles) {
            rect.updatePosition()
            Matrix()
                .scale(scale, scale, rect.center)
                .rotate(rotation, rect.center)
                .applyOn(context)
            context.fillRect(rect.x, rect.y, rect.width, rect.height)
        }
        renderRectangles(context, rectangles)
    }
}

private fun clearCanvas(context: CanvasRenderingContext2D){
    Matrix().applyOn(context)
    context.clearRect(.0, .0, canvasSize, canvasSize)
    context.fillStyle = "red"
}

private fun updateRotation() {
    rotation += 2.deg
}

var scaleFactor = 1.01
private fun updateScale() {
    scale *= scaleFactor
    if (scale !in 0.5..2.0)
        scaleFactor = 1 / scaleFactor
}

data class Rect(var x: Double, var y: Double, val width: Double = 200.0, val height: Double = 100.0) {
    private var dx = 2.0
    private var dy = 2.0

    var position: Point
        get() = Point(x, y)
        set(value) {
            x = value.x
            y = value.y
        }

    val center: Point
        get() = Point(x + .5 * width, y + .5 * height)

    fun updatePosition() {
        if (x !in 0.0..canvasSize) dx *= -1
        if (y !in 0.0..canvasSize) dy *= -1
        position += Point(dx, dy)
    }
}

fun canvas(): Pair<CanvasRenderingContext2D, HTMLCanvasElement> {
    val canvas = (document.createElement("canvas") as HTMLCanvasElement).apply {
        height = canvasSize.toInt()
        width = canvasSize.toInt()
    }
    requireNotNull(document.querySelector("body")).appendChild(canvas)
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    return context to canvas
}