package com.mrololo.sapper.game.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.mrololo.sapper.game.core.Cell
import com.mrololo.sapper.R


class FieldView : View {

    var fieldSize = 1
    var field: Array<Cell>? = null
    var cellSize = 0f
        private set

    private var colors: IntArray? = null
    private var paint = Paint()
    private var cellCenter = 0f
    private var circleRadius = 0f
    private var strokeWidth = 1f

    constructor(context: Context?) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val rateScale = 20
        val scale = resources.displayMetrics.density
        strokeWidth = scale*rateScale/fieldSize
        cellSize = (width/fieldSize).toFloat()
        cellCenter = cellSize/2
        circleRadius = cellSize/2 - strokeWidth/2
        colors = context.resources.getIntArray(R.array.colors)
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        canvas.drawPaint(paint)

        for((index, cell) in field!!.withIndex()) {
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = strokeWidth

            val i = index % fieldSize
            val j = index / fieldSize
            if (cell.opened) {
                when(cell.status) {
                    0 -> {
                        paint.color = Color.BLACK
                        canvas.drawCircle(getForCircle(i), getForCircle(j),
                            circleRadius, paint)
                    }
                    9 -> {
                        paint.color = Color.RED
                        paint.style = Paint.Style.FILL
                        canvas.drawCircle(getForCircle(i), getForCircle(j),
                            circleRadius, paint)
                    }
                    else -> {
                        val magicFigure = 1.35f
                        paint.color = colors!![cell.status - 1]
                        canvas.drawCircle(getForCircle(i), getForCircle(j),
                            circleRadius, paint)
                        paint.textSize = cellCenter
                        canvas.drawText("${cell.status}",
                            i*cellSize +cellCenter/magicFigure,
                            j*cellSize + magicFigure*cellCenter, paint)
                    }
                }

            } else if (cell.flagged) {
                paint.color = Color.RED
                canvas.drawLine(getForFlag(i), getForFlag(j), getForFlag(i),
                    (j+1)*cellSize - strokeWidth, paint)
                paint.style = Paint.Style.FILL
                canvas.drawRect(getForFlag(i), getForFlag(j),
                    i*cellSize+2*circleRadius,
                    getForCircle(j), paint)
            } else {
                paint.color = Color.LTGRAY
                paint.style = Paint.Style.FILL
                canvas.drawCircle(getForCircle(i), getForCircle(j),
                    cellCenter, paint)
            }
        }
    }

    private fun getForCircle(i: Int): Float {
        return (i*cellSize + cellCenter)
    }

    private fun getForFlag(i: Int): Float {
        return  (i*cellSize + strokeWidth)
    }
}
