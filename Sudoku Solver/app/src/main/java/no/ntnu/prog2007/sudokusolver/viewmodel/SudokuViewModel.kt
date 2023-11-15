package no.ntnu.prog2007.sudokusolver.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import no.ntnu.prog2007.sudokusolver.game.Cell
import no.ntnu.prog2007.sudokusolver.game.SudokuGame


class SudokuViewModel() : ViewModel() {
    val sudokuGame = SudokuGame()
}