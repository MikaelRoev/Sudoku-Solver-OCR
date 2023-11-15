package no.ntnu.prog2007.sudokusolver.game

class Board(val size: Int, var cells: List<Cell>) {
    fun getCell(row: Int, column: Int) = cells[row * size + column]
}