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

/**
 * Custom view that draws a Sudoku board and handles user input.
 */
class SudokuBoard(context: Context, attributeSet: AttributeSet): View(context, attributeSet) {

    //Fields
    private var sqrtSize = 3
    private var size = 9

    private var cellSizePx = 0F
    private var selectedCellRow = -1
    private var selectedCellCol = -1

    private var listener: OnTouchListener? = null

    private var cells: List<Cell>? = null

    // Paints for the board
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
    }
    private val inputCellTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        typeface = Typeface.DEFAULT_BOLD
    }
    private val inputCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#ACACAC")
    }

    /**
     * Calculates the size of the view based on the minimum of the width and height.
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePx = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePx, sizePx)
    }

    /**
     * Draws the Sudoku board view.
     */
    override fun onDraw(canvas: Canvas) {
        calculateSizes(width)
        fillCells(canvas)
        drawBoardLines(canvas)
        writeCellText(canvas)
    }

    /**
     * Calculates the size of the cells and the text inside the cells.
     */
    private fun calculateSizes(width: Int) {
        cellSizePx = width / size.toFloat()
        textPaint.textSize = cellSizePx / 1.5F
        inputCellTextPaint.textSize = cellSizePx / 1.4F
    }

    /**
     * Fills the cells with the correct color.
     *
     * @see fillCell
     */
    private fun fillCells(canvas: Canvas) {
        val solvedBoard = cells?.any { it.isInputCell }
        cells?.forEach {

            val r = it.row
            val c = it.column

            // If the cell is an input cell, fill it with the input cell color.
            if (it.isInputCell) {
                fillCell(canvas, r, c, inputCellPaint)

            // If the cell is selected, fill it with the selected cell color.
            } else if (r == selectedCellRow && c == selectedCellCol) {
                fillCell(canvas, r, c, selectedCellPaint)

            // For the InsertFragment.
            } else if (solvedBoard != true) {
                // If the cell is conflicting on the row or column, fill it with the conflicting cell color.
                if (r == selectedCellRow || c == selectedCellCol) {
                    fillCell(canvas, r, c, conflictingCellPaint)

                // If the cell is conflicting in the square, fill it with the conflicting cell color.
                } else if (r / sqrtSize == selectedCellRow / sqrtSize && c / sqrtSize == selectedCellCol / sqrtSize) {
                    fillCell(canvas, r, c, conflictingCellPaint)
                }
            }
        }
    }

    /**
     * Fills a cell with the given color.
     */
    private fun fillCell(canvas: Canvas, r: Int, c: Int, paint: Paint) {
        canvas.drawRect(c * cellSizePx, r * cellSizePx, (c + 1) * cellSizePx, (r + 1) * cellSizePx, paint)
    }

    /**
     * Draws the lines of the Sudoku board, surrounding and inside the board.
     */
    private fun drawBoardLines(canvas: Canvas) {
        // Drawing the surrounding lines
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), thickLinePaint)

        // Drawing the lines inside the board
        for (i in 1..size) {
            val whichPaint = when (i % sqrtSize) {
                0 -> thickLinePaint
                else -> thinLinePaint
            }
            canvas.drawLine(i * cellSizePx, 0F, i * cellSizePx, height.toFloat(), whichPaint)
            canvas.drawLine(0F, i * cellSizePx, width.toFloat(), i * cellSizePx, whichPaint)
        }
    }

    /**
     * Writes the text inside the cells.
     */
    private fun writeCellText(canvas: Canvas) {
        val solvedBoard = cells?.any { it.isInputCell }
        cells?.forEach {
            // For the InsertFragment. If the value of the cell is not 0, write the value in the cell.
            if (it.value != 0 && solvedBoard != true) {
                val row = it.row
                val column = it.column
                val valueString = it.value.toString()

                val textLimits = Rect()
                textPaint.getTextBounds(valueString, 0, valueString.length, textLimits)
                val textWidth = textPaint.measureText(valueString)
                val textHeight = textLimits.height()
                canvas.drawText(valueString, (column * cellSizePx) + cellSizePx / 2 - textWidth / 2,
                    (row * cellSizePx) + cellSizePx / 2 + textHeight / 2, textPaint)

            // For the SolvedFragment.
            } else if (solvedBoard == true) {
                val row = it.row
                val column = it.column
                // If the cell is an input cell or a revealed cell, write the value in the cell. Otherwise, write a question mark.
                val valueString = when {
                    it.isInputCell || it.isRevealed -> it.value.toString()
                    else -> "?"
                }

                val textLimits = Rect()
                // If the cell is an input cell, write the value in bold. Otherwise, write the value in normal.
                val paint = when {
                    it.isInputCell -> inputCellTextPaint
                    else -> textPaint
                }
                paint.getTextBounds(valueString, 0, valueString.length, textLimits)
                val textWidth = paint.measureText(valueString)
                val textHeight = textLimits.height()
                canvas.drawText(valueString, (column * cellSizePx) + cellSizePx / 2 - textWidth / 2,
                    (row * cellSizePx) + cellSizePx / 2 + textHeight / 2, paint)
            }
        }
    }

    /**
     * Override the onTouchEvent method to handle user input.
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouchEvent(event.x, event.y)
                true
            }
            else -> false
            }
        }

    /**
     * Handles touch events.
     */
    private fun handleTouchEvent(x: Float, y: Float) {
        val maybeSelectedCellRow = (y / cellSizePx).toInt()
        val maybeSelectedCellCol = (x / cellSizePx).toInt()
        listener?.onCellTouched(maybeSelectedCellRow, maybeSelectedCellCol)
    }

    /**
     * Updates the selected cell.
     */
    fun updateSelectedCell(row: Int, column: Int) {
        selectedCellRow = row
        selectedCellCol = column
        invalidate()
    }

    /**
     * Registers a listener for touch events.
     */
    fun registerListener(listener: OnTouchListener) {
        this.listener = listener
    }

    /**
     * Interface for touch events.
     */
    interface OnTouchListener {
        fun onCellTouched(row: Int, column: Int)
    }

    /**
     * Updates the cells of the Sudoku board.
     */
    fun updateCells(cells: List<Cell>) {
        this.cells = cells
        invalidate()
    }

    /**
     * Reveals a solved cell when pressed.
     */
    fun revealSolvedCell(row: Int, column: Int) {
        cells?.forEach {
            if (it.row == row && it.column == column) {
                it.isRevealed = true
            }
        }
        invalidate()
    }

    /**
     * Reveals all solved cells when the button is pressed.
     */
    fun revealAllSolvedCells() {
        cells?.forEach {
            if (!it.isInputCell) {
                it.isRevealed = true
            }
        }
        invalidate()
    }

    /**
     * Clears the Input Sudoku board.
     */
    fun clearSudokuBoard() {
        cells?.forEach {
            it.value = 0
            it.isRevealed = false
            it.isInputCell = false
        }
        invalidate()
    }
}
