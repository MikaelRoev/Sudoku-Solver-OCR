package no.ntnu.prog2007.sudokusolver.game

/**
 * Represents a Sudoku board.
 *
 * @property size The size of the board, e.g. 9 for a 9x9 board.
 * @property cells The cells of the board.
 */
class Board(val size: Int, var cells: List<Cell>) {

    /**
     * Returns the cell at the given row and column.
     *
     * @param row The row of the cell.
     * @param column The column of the cell.
     * @return The cell at the given row and column.
     */
    fun getCell(row: Int, column: Int) = cells[row * size + column]
}