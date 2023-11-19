package no.ntnu.prog2007.sudokusolver.game

import androidx.lifecycle.MutableLiveData

/**
 * Represents a Sudoku game.
 */
class SudokuGame {
    // Fields
    var selectedLiveData = MutableLiveData<Pair<Int, Int>>()
    var cellsLiveData = MutableLiveData<List<Cell>>()

    private var selectedCellRow = -1
    private var selectedCellColumn = -1

    private val board: Board

    /**
     * Initializes a new instance of the SudokuGame class.
     */
    init {
        val cells = List(9*9) { i -> Cell(i/9, i%9, 0) }
        board = Board(9, cells)
        selectedLiveData.postValue(Pair(selectedCellRow, selectedCellColumn))
        cellsLiveData.postValue(board.cells)
    }

    /**
     * Companion object for the SudokuGame class.
     * Holds the singleton instance of the SudokuGame class.
     */
    companion object {
        private var instance: SudokuGame? = null
        fun getInstance(): SudokuGame {
            if (instance == null) {
                instance = SudokuGame()
            }
            return instance!!
        }
    }

    /**
     * Handles the input of a number to a cell.
     * @param number The number to input.
     */
    fun handleInput(number: Int) {
        if (selectedCellRow == -1 || selectedCellColumn == -1) return
        if (board.getCell(selectedCellRow, selectedCellColumn).isInputCell) return
        board.getCell(selectedCellRow, selectedCellColumn).value = number
        cellsLiveData.postValue(board.cells)
    }

    /**
     * Updates the selected cell.
     * @param row The row of the cell.
     * @param column The column of the cell.
     */
    fun updateSelectedCell(row: Int, column: Int) {
        if (!board.getCell(row, column).isInputCell) {
            selectedCellRow = row
            selectedCellColumn = column
            selectedLiveData.postValue(Pair(row, column))
        }
    }


    /**
     * Gets the cells of the game.
     * @return The cells of the game.
     */
    fun getCells(): List<Cell> {
        return board.cells
    }

    /**
     * Sets the cells of the game.
     * @param cells The cells to set.
     */
    fun setCells(cells: List<Cell>) {
        board.cells = cells
    }
}