package no.ntnu.prog2007.sudokusolver.game

import androidx.lifecycle.MutableLiveData

class SudokuGame {
    var selectedLiveData = MutableLiveData<Pair<Int, Int>>()

    private var selectedCellRow = -1
    private var selectedCellColumn = -1

    init {
        selectedLiveData.postValue(Pair(selectedCellRow, selectedCellColumn))
    }

    fun updateSelectedCell(row: Int, column: Int) {
        selectedCellRow = row
        selectedCellColumn = column
        selectedLiveData.postValue(Pair(row, column))
    }
}