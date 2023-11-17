package no.ntnu.prog2007.sudokusolver.view

import androidx.lifecycle.ViewModel
import no.ntnu.prog2007.sudokusolver.game.SudokuGame


class SudokuViewModel() : ViewModel() {
    val sudokuGame = SudokuGame()
}