package no.ntnu.prog2007.sudokusolver.game

import androidx.lifecycle.MutableLiveData

class SudokuGame {
    var selectedLiveData = MutableLiveData<Pair<Int, Int>>()
    var cellsLiveData = MutableLiveData<List<Cell>>()

    private var selectedCellRow = -1
    private var selectedCellColumn = -1

    private val board: Board

    init {
        val cells = List(9*9) { i -> Cell(i/9, i%9, 0) }
        board = Board(9, cells)
        selectedLiveData.postValue(Pair(selectedCellRow, selectedCellColumn))
        cellsLiveData.postValue(board.cells)
    }

    fun handleInput(number: Int) {
        if (selectedCellRow == -1 || selectedCellColumn == -1) return
        if (board.getCell(selectedCellRow, selectedCellColumn).isInputCell) return
        board.getCell(selectedCellRow, selectedCellColumn).value = number
        cellsLiveData.postValue(board.cells)
    }

    fun updateSelectedCell(row: Int, column: Int) {
        if (!board.getCell(row, column).isInputCell) {
            selectedCellRow = row
            selectedCellColumn = column
            selectedLiveData.postValue(Pair(row, column))
        }
    }


    fun getCells(): List<Cell> {
        return board.cells
    }

    fun setCells(cells: List<Cell>) {
        board.cells = cells
    }

    fun getBoard(): Board {
        return board
    }
}