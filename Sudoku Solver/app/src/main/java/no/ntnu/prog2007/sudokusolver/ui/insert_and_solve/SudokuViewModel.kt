package no.ntnu.prog2007.sudokusolver.ui.insert_and_solve

import androidx.lifecycle.ViewModel
import no.ntnu.prog2007.sudokusolver.game.SudokuGame

/**
 * ViewModel for the Sudoku game.
 * Holds the game state and exposes it to the view.
 */
class SudokuViewModel() : ViewModel() {
    val sudokuGame = SudokuGame.getInstance()
}