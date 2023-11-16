package no.ntnu.prog2007.sudokusolver

/**
 * Represents a sudoku grid.
 */
class Sudoku() {

    private val n = 9
    private val grid = List(n) { MutableList(n) { 0 } }
    var solved = false
        private set
    private var multipleSolutions = false
    var solution = List(n) { List(n) { 0 } }
        private set

    /**
     * Makes a sudoku grid based on an initial grid.
     * @param initialGrid is the initial grid to make the sudoku grid out of.
     */
    constructor(initialGrid: List<List<Int>>): this() {
        require(initialGrid.size == n && initialGrid.all { it.size == n }) {
            "Initial grid needs to be a $n times $n grid." }
        for (row in 0 until n) {
            for (col in 0 until n) {
                setValue(row, col, initialGrid[row][col])
            }
        }
    }

    /**
     * Sets a value at a cell in the grid.
     * @param row is the row of the cell.
     * @param col is the column of the cell.
     * @param value the value to be set.
     */
    fun setValue(row: Int, col: Int, value: Int) {
        require(value in 0..n) {
            "The value must be between 0 and $n inclusive, not $value"}
        require(value == 0 || isValid(row, col, value)) {
            "The position ($row, $col) is not valid for the value $value."
        }
        grid[row][col] = value
    }

    /**
     * Returns the value of a cell.
     * @param row is the row of the cell.
     * @param col is the column of the cell.
     * @return the value of the cell.
     */
    fun getValue(row: Int, col: Int): Int {
        return grid[row][col]
    }

    /**
     * Solves the sudoku grid. Checks if there are more than one solution.
     */
    fun solve() {
        val cell = findLeastSolutionCell()
        val row = cell.first
        val col = cell.second
        if (row == -1 || col == -1) {   // found a solution (no empty cells)
            if (solved) {
                solution = emptyList()
                multipleSolutions = true
                solved = false
            } else {
                solution = grid.copy()
                solved = true
            }
        } else {
            val values = findValidValuesSortConflict(row, col)
            values.forEach {
                val value = it.second
                grid[row][col] = value
                solve()
                if (multipleSolutions) return
                grid[row][col] = 0
            }
        }
    }

    /**
     * Check if the grid has at least the minimum number of clues which is 17.
     * @return true if it has at lest 17 clues or false if it has less then 17
     */
    private fun hasAtLeastMinimumClues(): Boolean {
        var numberOfClues = 0
        for (row in grid) {
            for (number in row) {
                if (number == 0) continue
                numberOfClues++
                if (numberOfClues >= 17) return true
            }
        }
        return false
    }

    /**
     * Replicates a grid.
     * @return the replicated grid.
     */
    private fun List<List<Int>>.copy(): List<List<Int>> {
        val copiedGrid = mutableListOf<MutableList<Int>>()

        for (row in this) {
            val newRow = row.toMutableList()
            copiedGrid.add(newRow)
        }

        return copiedGrid
    }

    /**
     * Solves the sudoku grid. Finds the first solution and changes the grid to it.
     * @return true if the grid was solved or false if the grid is unsolvable.
     */
    fun solveFindFirst(): Boolean {
        val cell = findLeastSolutionCell()
        val row = cell.first
        val col = cell.second
        if (row == -1 || col == -1) {
            solution = grid.copy()
            solved = true
            return true
        }             // found a solution (no empty cells)
        val values = findValidValuesSortConflict(row, col)
        values.forEach {
            val value = it.second
            grid[row][col] = value
            if (solveFindFirst()) return true
            grid[row][col] = 0
        }
        return false
    }

    /**
     * Checks if the value is a valid value in a cell in the grid.
     * @param row is the row of the cell.
     * @param col is the column of the cell.
     * @param value is the value to check.
     * @return true if it valid of false if not.
     */
    private fun isValid(row: Int, col: Int, value: Int): Boolean {
        for (i in 0 until n) {
            if (grid[row][i] == value       // is the value in the row
                || grid[i][col] == value    // or is the value in the column
                || grid[3 * (row / 3) + i / 3][3 * (col / 3) + i % 3] == value)
                // or is the value in the 3 x 3 box
                return false
        }
        return true
    }

    /**
     * Counts the number of valid possible values of a cell in the grid.
     * @param row the row of the cell.
     * @param col the column of the cell.
     * @return the number of possible values.
     */
    private fun countValidValues(row: Int, col: Int): Int {
        var count = 0
        for (i in 1..n) if (isValid(row, col, i)) count++
        return count
    }

    /**
     * Finds the position of the cell with the least number of valid possible values.
     * @return the position of the cell as a pair of int
     * or a pair of -1 if there are no empty cells left.
     */
    private fun findLeastSolutionCell(): Pair<Int, Int> {
        var minCount = n + 1
        var minPos = Pair(-1, -1)
        var row = 0
        var col = 0

        while (row < n) {
            if (grid[row][col] == 0) { // if empty cell
                val count = countValidValues(row, col)
                if (count < minCount) {
                    // update the new minimum
                    minCount = count
                    minPos = Pair(row, col)
                }
            }
            col++
            if (col == n) { // if reaching the edge of the grid
                col = 0
                row++
            }
        }
        return minPos
    }

    /**
     * Counts all the conflicts for a value in the row, column and the box given a cell position.
     * @param row the row position of the cell.
     * @param col the column position of the cell.
     * @param value is the value to count the conflicts of.
     * @return the number of conflicts for the value.
     */
    private fun countConflicts(row: Int, col: Int, value: Int): Int {
        var conflicts = 0

        for (i in 0 until n) {
            // count the conflicts for the value in the row
            if (i != col                        // is not in the column
                && grid[row][i] != 0            // and is not an empty cell
                && !isValid(row, i, value)){    // and is not a valid value
                conflicts++
            }
            // count the conflicts for the value in the column
            if (i != row && grid[i][col] != 0 && !isValid(i, col, value)) conflicts++
            // count the conflicts in the box
            val boxRow = 3 * (row / 3) + i / 3
            val boxCol = 3 * (col / 3) + i % 3
            if ((boxRow != row || boxCol != col)
                // is nether in the row nor the column (already checked)
                && grid[boxRow][boxCol] != 0 && !isValid(boxRow, boxCol, value)) conflicts++
        }
        return conflicts
    }

    /**
     * Finds all valid numbers in a cell and sorts them by number of conflicts.
     * @param row is the row of the cell.
     * @param col is the column of the cell.
     * @return a sorted list of all valid values in pairs of number of conflicts and value.
     */
    private fun findValidValuesSortConflict(row: Int, col: Int): List<Pair<Int, Int>> {
        val values = mutableListOf<Pair<Int, Int>>()

        for (i in 1..n) {
            if (!isValid(row, col, i)) continue
            val conflicts = countConflicts(row, col, i)
            values.add(Pair(conflicts, i))
        }
        values.sortBy { pair -> pair.first  }
        return values
    }

    private fun gridToSting(grid: List<List<Int>>): String {
        val sb = StringBuilder()
        for (row in 0 until n) {
            if (row % 3 == 0 && row != 0) sb.append("---------------------\n")
            for (col in 0 until n) {
                if (col % 3 == 0 && col != 0) sb.append("| ")
                sb.append(grid[row][col]).append(' ')
            }
            sb.append('\n')
        }
        return sb.toString()
    }

    fun solutionToString() = gridToSting(solution)

    override fun toString() = gridToSting(grid)
}