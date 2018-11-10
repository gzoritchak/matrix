import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.document
import kotlin.browser.window

const val canvasSize = 700

fun main(args: Array<String>) {

    val (context, _) = canvas()
    val rects = listOf(Rect(100.0, 200.0), Rect(300.0, 500.0))
    renderSquares(rects, context)
}

var frameCount = 0
var scale = 1.0
var rotation = 0.deg

fun renderSquares(rects: List<Rect>, context: CanvasRenderingContext2D) {

    window.requestAnimationFrame {
        frameCount++
        val matrix = Matrix().applyOn(context)
        context.clearRect(.0, .0, canvasSize.toDouble(), canvasSize.toDouble())
        context.fillStyle = "red"
        updateRotation()
        updateScale()

        for (rect in rects) {
            rect.updatePosition()
            matrix
                .reset()
                .scale(scale, scale, rect.center)
                .rotate(rotation, rect.center)
                .applyOn(context)
            context.fillRect(rect.x, rect.y, rect.width, rect.height)

        }
        renderSquares(rects, context)
    }
}

private fun updateRotation() {
    rotation += 2.deg
}

var scaleFactor = 1.01
private fun updateScale() {
    scale *= scaleFactor
    if (scale > 2.0) {
        scaleFactor = 1 / scaleFactor
    }
    if (scale < 0.5) {
        scaleFactor = 1 / scaleFactor
    }
}

data class Rect(var x: Double, var y: Double, val width: Double = 200.0, val height: Double = 100.0) {
    var dx = 2.0
    var dy = 2.0

    var position: Point
        get() = Point(x, y)
        set(value) {
            x = value.x
            y = value.y
        }

    val center: Point
        get() = Point(x + .5 * width, y + .5 * height)

    fun updatePosition() {
        if (x > canvasSize && dx > 0) dx *= -1
        if (x < 0 && dx < 0) dx *= -1
        if (y > canvasSize && dy > 0) dy *= -1
        if (y < 0 && dy < 0) dy *= -1
        position += Point(dx, dy)
    }
}

fun canvas(): Pair<CanvasRenderingContext2D, HTMLCanvasElement> {
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    canvas.height = canvasSize
    canvas.width = canvasSize
    val body = requireNotNull(document.querySelector("body"))
    body.appendChild(canvas)
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    return context to canvas
}