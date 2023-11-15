package no.ntnu.prog2007.sudokusolver.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import no.ntnu.prog2007.sudokusolver.game.Cell
import kotlin.math.min

class SudokuBoard(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    private var sqrtSize = 3
    private var size = 9

    private var cellSizePx = 0F
    private var selectedCellRow = -1
    private var selectedCellCol = -1

    private var listener: OnTouchListener? = null

    private var cells: List<Cell>? = null

    private val thickLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 6F
    }

    private val thinLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 2F
    }

    private val selectedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#B3E5FC")
    }
    private val conflictingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#EFEDEF")
    }

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        textSize = 50F
    }

    private val inputCellTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        textSize = 54F
        typeface = Typeface.DEFAULT_BOLD
    }

    private val inputCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#ACACAC")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePx = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePx, sizePx)
    }

    override fun onDraw(canvas: Canvas) {
        cellSizePx = (width / size).toFloat()
        fillCells(canvas)
        drawBoardLines(canvas)
        writeCellText(canvas)
    }

    private fun fillCells(canvas: Canvas) {
        cells?.forEach {
            val r = it.row
            val c = it.column
            if (it.isInputCell) {
                fillCell(canvas, r, c, inputCellPaint)
            } else if (r == selectedCellRow && c == selectedCellCol) {
                fillCell(canvas, r, c, selectedCellPaint)
            } else if (r == selectedCellRow || c == selectedCellCol) {
                fillCell(canvas, r, c, conflictingCellPaint)
            } else if (r / sqrtSize == selectedCellRow / sqrtSize && c / sqrtSize == selectedCellCol / sqrtSize) {
                fillCell(canvas, r, c, conflictingCellPaint)
            }
        }
    }

    private fun fillCell(canvas: Canvas, r: Int, c: Int, paint: Paint) {
        canvas.drawRect(c * cellSizePx, r * cellSizePx, (c + 1) * cellSizePx, (r + 1) * cellSizePx, paint)
    }

    private fun drawBoardLines(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), thickLinePaint)

        for (i in 1..size) {
            val whichPaint = when (i % sqrtSize) {
                0 -> thickLinePaint
                else -> thinLinePaint
            }
            canvas.drawLine(i * cellSizePx, 0F, i * cellSizePx, height.toFloat(), whichPaint)
            canvas.drawLine(0F, i * cellSizePx, width.toFloat(), i * cellSizePx, whichPaint)
        }
    }

    private fun writeCellText(canvas: Canvas) {
        val solvedBoard = cells?.any { it.isInputCell }
        cells?.forEach {
            if (it.value != 0) {
                val row = it.row
                val column = it.column
                val valueString = it.value.toString()

                val whichPaint = if (it.isInputCell) inputCellTextPaint else textPaint
                val textLimits = Rect()
                whichPaint.getTextBounds(valueString, 0, valueString.length, textLimits)
                val textWidth = whichPaint.measureText(valueString)
                val textHeight = textLimits.height()
                canvas.drawText(valueString, (column * cellSizePx) + cellSizePx / 2 - textWidth / 2,
                    (row * cellSizePx) + cellSizePx / 2 + textHeight / 2, whichPaint)
            } else if (solvedBoard == true && !it.isInputCell) {
                val row = it.row
                val column = it.column
                val valueString = "?"

                val whichPaint = if (it.isInputCell) inputCellTextPaint else textPaint
                val textLimits = Rect()
                whichPaint.getTextBounds(valueString, 0, valueString.length, textLimits)
                val textWidth = whichPaint.measureText(valueString)
                val textHeight = textLimits.height()
                canvas.drawText(valueString, (column * cellSizePx) + cellSizePx / 2 - textWidth / 2,
                    (row * cellSizePx) + cellSizePx / 2 + textHeight / 2, whichPaint)
            }
        }
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouchEvent(event.x, event.y)
                true
            }
            else -> false
            }
        }

    private fun handleTouchEvent(x: Float, y: Float) {
        val maybeSelectedCellRow = (y / cellSizePx).toInt()
        val maybeSelectedCellCol = (x / cellSizePx).toInt()
        listener?.onCellTouched(maybeSelectedCellRow, maybeSelectedCellCol)
    }

    fun updateSelectedCell(row: Int, column: Int) {
        selectedCellRow = row
        selectedCellCol = column
        invalidate()
    }

    fun registerListener(listener: OnTouchListener) {
        this.listener = listener
    }

    interface OnTouchListener {
        fun onCellTouched(row: Int, column: Int)
    }

    fun updateCells(cells: List<Cell>) {
        this.cells = cells
        invalidate()
    }
}
