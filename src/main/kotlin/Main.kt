import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.document
import kotlin.browser.window
import kotlin.random.Random


const val width = 800

fun main(args: Array<String>) {

    val (context, canvas) = canvas()
    val squares = generateSquares(4000)
    renderSquares(squares, context)
}

fun Matrix.applyOn(context: CanvasRenderingContext2D){
    context.setTransform(a, b, c, d, tx, ty)
}

fun renderSquares(squares: List<Square>, context: CanvasRenderingContext2D ){
    for (square in squares) {
        square.updatePos()
        context.fillStyle = "red"
        Matrix().translate(square.x, square.y).applyOn(context)
        context.fillRect(.0, .0, square.size, square.size)
    }
    window.requestAnimationFrame {
        Matrix().applyOn(context)
        context.clearRect(.0, .0, width.toDouble(), width.toDouble())
        renderSquares(squares, context)
    }
}

fun generateSquares(count: Int)  = (1..count).map {
    Square(Random.nextDouble(800.0), Random.nextDouble(800.0), Random.nextDouble(15.0))
}


data class Square(var x:Double, val y:Double, val size:Double) {
    var dx = 1

    fun updatePos() {
        if (x > width && dx > 0) dx *= -1
        if (x < 0 && dx < 0) dx *= -1
        x += dx
    }
}

fun canvas(): Pair<CanvasRenderingContext2D, HTMLCanvasElement> {
    val canvas = document.createElement("canvas") as HTMLCanvasElement
    canvas.height = width
    canvas.width = width
    val body = requireNotNull(document.querySelector("body"))
    body.appendChild(canvas)
    val context = canvas.getContext("2d") as CanvasRenderingContext2D
    return context to canvas
}