package no.ntnu.prog2007.sudokusolver.game

/**
 * Represents a Sudoku board.
 *
 * @property size The size of the board, e.g. 9 for a 9x9 board.
 * @property cells The cells of the board.
 */
class Board(val size: Int, var cells: List<Cell>) {
    companion object {
        /**
         * Converts a grid of integers to a list of cells.
         * @param grid The grid to convert.
         * @return The list of cells.
         */
        fun fromGridToCells(grid: List<List<Int>>): List<Cell> {
            val cells = mutableListOf<Cell>()
            for (row in 0..8) {
                for (col in 0..8) {
                    cells.add(Cell(row, col, grid[row][col]))
                }
            }
            return cells
        }

        /**
         * Converts a list of cells to a grid of integers.
         * @param cells The cells to convert.
         * @return The grid of integers.
         */
        fun fromCellsToGrid(cells: List<Cell>): List<List<Int>> {
            val grid = mutableListOf<MutableList<Int>>()
            for (row in 0..8) {
                val newRow = mutableListOf<Int>()
                for (col in 0..8) {
                    newRow.add(cells[row*9+col].value)
                }
                grid.add(newRow)
            }
            return grid
        }
    }

    /**
     * Returns the cell at the given row and column.
     *
     * @param row The row of the cell.
     * @param column The column of the cell.
     * @return The cell at the given row and column.
     */
    fun getCell(row: Int, column: Int) = cells[row * size + column]
}