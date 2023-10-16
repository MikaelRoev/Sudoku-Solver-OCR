package no.ntnu.prog2007.sudokusolver

import org.junit.Test

import org.junit.Assert.*
import java.lang.IllegalArgumentException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SudokuUnitTest {

    @Test
    fun testConstructor_InvalidDimensions() {
        assertThrows(IllegalArgumentException::class.java) { Sudoku(listOf(listOf(0))) }
    }

    @Test
    fun testConstructor_InvalidValues() {
        assertThrows(IllegalArgumentException::class.java) {
            Sudoku(
                listOf(
                    listOf(5, 3, 0, /*|*/ 0, 7, 0, /*|*/ 0, 0, 0),
                    listOf(6, 0, 0, /*|*/ 1, 9, 5, /*|*/ 0, 0, 0),
                    listOf(0, 9, 8, /*|*/ 0, 0, 0, /*|*/ 0, 6, 0),
                    //-------------------------------------------
                    listOf(8, 0, 0, /*|*/ 0, 6, 0, /*|*/ 0, 0, 3),
                    listOf(4, 0, 0, /*|*/ 8, 10, 3, /*|*/ 0, 0, 1),
                    listOf(7, 0, 0, /*|*/ 0, 2, 0, /*|*/ 0, 0, 6),
                    //-------------------------------------------
                    listOf(0, 6, 0, /*|*/ 0, 0, 0, /*|*/ 2, 8, 0),
                    listOf(0, 0, 0, /*|*/ 4, 1, 9, /*|*/ 0, 0, 5),
                    listOf(0, 0, 0, /*|*/ 0, 8, 0, /*|*/ 0, 7, 9),
                )
            )
        }

        assertThrows(IllegalArgumentException::class.java) {
            Sudoku(
                listOf(
                    listOf(5, 3, 0, /*|*/ 0, 7, 0, /*|*/ 0, 0, 0),
                    listOf(6, 0, 0, /*|*/ -1, 9, 5, /*|*/ 0, 0, 0),
                    listOf(0, 9, 8, /*|*/ 0, 0, 0, /*|*/ 0, 6, 0),
                    //-------------------------------------------
                    listOf(8, 0, 0, /*|*/ 0, 6, 0, /*|*/ 0, 0, 3),
                    listOf(4, 0, 0, /*|*/ 8, 0, 3, /*|*/ 0, 0, 1),
                    listOf(7, 0, 0, /*|*/ 0, 2, 0, /*|*/ 0, 0, 6),
                    //-------------------------------------------
                    listOf(0, 6, 0, /*|*/ 0, 0, 0, /*|*/ 2, 8, 0),
                    listOf(0, 0, 0, /*|*/ 4, 1, 9, /*|*/ 0, 0, 5),
                    listOf(0, 0, 0, /*|*/ 0, 8, 0, /*|*/ 0, 7, 9),
                )
            )
        }
        assertThrows(IllegalArgumentException::class.java) {
            Sudoku(
                listOf(
                    listOf(5, 3, 8, /*|*/ 0, 7, 0, /*|*/ 0, 0, 0),
                    listOf(6, 0, 0, /*|*/ 1, 9, 5, /*|*/ 0, 0, 0),
                    listOf(0, 9, 8, /*|*/ 0, 0, 0, /*|*/ 0, 6, 0),
                    //-------------------------------------------
                    listOf(8, 0, 0, /*|*/ 0, 6, 0, /*|*/ 0, 0, 3),
                    listOf(4, 0, 0, /*|*/ 8, 0, 3, /*|*/ 0, 0, 1),
                    listOf(7, 0, 0, /*|*/ 0, 2, 0, /*|*/ 0, 0, 6),
                    //-------------------------------------------
                    listOf(0, 6, 0, /*|*/ 0, 0, 0, /*|*/ 2, 8, 0),
                    listOf(0, 0, 0, /*|*/ 4, 1, 9, /*|*/ 0, 0, 5),
                    listOf(0, 0, 0, /*|*/ 0, 8, 0, /*|*/ 0, 7, 9),
                )
            )
        }
    }

    @Test
    fun testSolve_solvable() {
        testSudoku(
        listOf(
            listOf(5, 3, 0, /*|*/ 0, 7, 0, /*|*/ 0, 0, 0),
            listOf(6, 0, 0, /*|*/ 1, 9, 5, /*|*/ 0, 0, 0),
            listOf(0, 9, 8, /*|*/ 0, 0, 0, /*|*/ 0, 6, 0),
            //-------------------------------------------
            listOf(8, 0, 0, /*|*/ 0, 6, 0, /*|*/ 0, 0, 3),
            listOf(4, 0, 0, /*|*/ 8, 0, 3, /*|*/ 0, 0, 1),
            listOf(7, 0, 0, /*|*/ 0, 2, 0, /*|*/ 0, 0, 6),
            //-------------------------------------------
            listOf(0, 6, 0, /*|*/ 0, 0, 0, /*|*/ 2, 8, 0),
            listOf(0, 0, 0, /*|*/ 4, 1, 9, /*|*/ 0, 0, 5),
            listOf(0, 0, 0, /*|*/ 0, 8, 0, /*|*/ 0, 7, 9),
            )
        )
    }

    @Test
    fun testSolve_drArtoInkalaSudoku1() {
        testSudoku(drArtoInkalaSudoku1)
    }

    @Test
    fun testSolve_drArtoInkalaSudoku2() {
        testSudoku(drArtoInkalaSudoku2)
    }

    @Test
    fun testSolve_hardSudoku() {
        testSudoku(hardSudoku)
    }

    @Test
    fun testSolve_unsolvable() {
        val sudoku = Sudoku(
            listOf(
                listOf(5, 3, 0, /*|*/ 0, 7, 0, /*|*/ 0, 0, 0),
                listOf(6, 0, 0, /*|*/ 1, 9, 5, /*|*/ 0, 0, 0),
                listOf(0, 9, 0, /*|*/ 0, 0, 0, /*|*/ 0, 6, 0),
                //-------------------------------------------
                listOf(0, 0, 0, /*|*/ 0, 6, 0, /*|*/ 0, 0, 3),
                listOf(4, 0, 0, /*|*/ 0, 0, 3, /*|*/ 0, 0, 1),
                listOf(7, 0, 0, /*|*/ 0, 0, 0, /*|*/ 0, 0, 6),
                //-------------------------------------------
                listOf(0, 6, 0, /*|*/ 0, 0, 0, /*|*/ 2, 0, 0),
                listOf(0, 0, 0, /*|*/ 4, 1, 9, /*|*/ 0, 0, 5),
                listOf(0, 0, 0, /*|*/ 0, 0, 0, /*|*/ 0, 7, 9),
            )
        )
        sudoku.solve()
        assertTrue(!sudoku.solved)
    }

    private fun testSudoku(grid: List<List<Int>>) {
        val sudoku = Sudoku(grid)
        val startTime = System.currentTimeMillis()
        sudoku.solve()
        val time = System.currentTimeMillis() - startTime
        println("Solution time: $time ms:")
        assertTrue(sudoku.solved)
        val counts = List(9) { 0 }.toMutableList()
        for (row in sudoku.solution) {
            for (value in row) {
                counts[value - 1]++
            }
        }
        counts.forEach { assertEquals(9, it) }
        print(sudoku.solutionToString())
    }
}