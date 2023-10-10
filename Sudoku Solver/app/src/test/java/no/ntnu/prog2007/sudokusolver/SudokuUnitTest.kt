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
        val sudoku = Sudoku(
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
        print(sudoku.toString())
        assertTrue(sudoku.solve())
        print(sudoku.toString())
        val counts = List(9) { 0 }.toMutableList()
        for (row in 0 until 9) {
            for (col in 0 until 9) {
                counts[sudoku.getValue(row, col) - 1]++
            }
        }
        counts.forEach { assertEquals(9, it) }
    }

    @Test
    fun testSolve_unsolvable() {
        val sudoku = Sudoku(
            listOf(
                listOf(5, 3, 0, /*|*/ 0, 7, 0, /*|*/ 0, 0, 0),
                listOf(6, 0, 0, /*|*/ 1, 9, 5, /*|*/ 0, 0, 0),
                listOf(0, 9, 8, /*|*/ 0, 0, 0, /*|*/ 0, 6, 0),
                //-------------------------------------------
                listOf(8, 0, 0, /*|*/ 0, 6, 0, /*|*/ 0, 0, 3),
                listOf(4, 0, 0, /*|*/ 8, 0, 3, /*|*/ 0, 0, 1),
                listOf(7, 0, 0, /*|*/ 0, 0, 0, /*|*/ 0, 0, 6),
                //-------------------------------------------
                listOf(0, 6, 0, /*|*/ 0, 0, 0, /*|*/ 2, 8, 0),
                listOf(0, 0, 0, /*|*/ 4, 1, 9, /*|*/ 0, 0, 5),
                listOf(0, 0, 0, /*|*/ 0, 8, 0, /*|*/ 0, 7, 9),
            )
        )
        assertTrue(!sudoku.solve())
    }
}