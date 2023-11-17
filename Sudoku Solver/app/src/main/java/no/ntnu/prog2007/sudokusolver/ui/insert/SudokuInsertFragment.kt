package no.ntnu.prog2007.sudokusolver.ui.insert

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import no.ntnu.prog2007.sudokusolver.R
import no.ntnu.prog2007.sudokusolver.Sudoku
import no.ntnu.prog2007.sudokusolver.ui.solved.SudokuSolvedFragment
import no.ntnu.prog2007.sudokusolver.databinding.FragmentSudokuInsertBinding
import no.ntnu.prog2007.sudokusolver.game.Board
import no.ntnu.prog2007.sudokusolver.game.Cell
import no.ntnu.prog2007.sudokusolver.ui.filechooser.FileChooserFragment.Companion.CHOSEN_GRID_KEY
import no.ntnu.prog2007.sudokusolver.view.SudokuBoard
import no.ntnu.prog2007.sudokusolver.view.SudokuViewModel

/**
 * A Fragment that contains the Sudoku board and buttons for inputting numbers.
 */
class SudokuInsertFragment : Fragment(), SudokuBoard.OnTouchListener {
    companion object {
        const val SOLVED_CELLS_KEY = "no.ntnu.prog2007.sudokusolver.SOLVED_CELLS_KEY"
    }

    private lateinit var binding: FragmentSudokuInsertBinding
    private lateinit var viewModel: SudokuViewModel

    private var originalCells: List<Cell>? = null

    /**
     * Creates the view for the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentSudokuInsertBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        binding.sudokuBoard.registerListener(this)
        viewModel = ViewModelProvider(this)[SudokuViewModel::class.java]


        val chosenCells = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList(CHOSEN_GRID_KEY, Cell::class.java)
        } else {
            arguments?.getParcelableArrayList(CHOSEN_GRID_KEY)
        }
        if (chosenCells != null) originalCells = chosenCells

        // If the board has been solved, and the user returns to the insert fragment,
        // the originalCells is filled with the original input cells
        if (originalCells != null) {
            viewModel.sudokuGame.setCells(originalCells!!)
            viewModel.sudokuGame.getCells().forEach {
                it.isInputCell = false
            }
            viewModel.sudokuGame.cellsLiveData.observe(viewLifecycleOwner) { updateCells(viewModel.sudokuGame.getCells()) }

        } else {
            viewModel.sudokuGame.cellsLiveData.observe(viewLifecycleOwner) { updateCells(it) }
        }
        viewModel.sudokuGame.selectedLiveData.observe(viewLifecycleOwner) { updateSelectedCellUI(it) }
        viewModel.sudokuGame.selectedLiveData.observe(viewLifecycleOwner) { disableButtonsWrongBySudoku(it) }

        // The input buttons
        val buttons = listOf(binding.deleteButton, binding.oneButton, binding.twoButton,
            binding.threeButton, binding.fourButton, binding.fiveButton,
            binding.sixButton, binding.sevenButton, binding.eightButton, binding.nineButton)
        // Sets the onClickListener for each button
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener { viewModel.sudokuGame.handleInput(index) }
        }
        // The solve and clear buttons
        binding.solveButton.setOnClickListener { solveSudokuAndChangeFragment() }
        binding.clearButton.setOnClickListener { clearSudokuBoard()}

        return fragmentBinding.root
    }


    /**
     * Updates the cells in the Sudoku board.
     * @param cells The cells to update with.
     */
    private fun updateCells(cells: List<Cell>?) = cells?.let {
        binding.sudokuBoard.updateCells(cells)
    }

    /**
     * Updates the selected cell in the Sudoku board.
     */
    private fun updateSelectedCellUI(cell: Pair<Int, Int>?) = cell?.let {
        binding.sudokuBoard.updateSelectedCell(cell.first, cell.second)
    }

    /**
     * Handles touch events on cells in the Sudoku board.
     */
    override fun onCellTouched(row: Int, column: Int) {
        viewModel.sudokuGame.updateSelectedCell(row, column)
    }

    /**
     * Solves the Sudoku and changes the fragment to the SudokuSolvedFragment.
     * If the Sudoku is not solvable, an alert is shown.
     */
    private fun solveSudokuAndChangeFragment() {
        val cells = viewModel.sudokuGame.getCells()
        // Converts the cells to a grid of integers so that it is compatible with the Sudoku class.
        val grid = Board.fromCellsToGrid(cells)
        val sudoku = Sudoku()
        // Solves the Sudoku
        sudoku.solve()
        if (sudoku.solved) {
            originalCells = cells
            val solvedCells = Board.fromGridToCells(sudoku.solution)
            solvedCells.forEach {
                if (cells[it.row * 9 + it.column].value != 0) it.isInputCell = true
            }
            // Creates a new SudokuSolvedFragment and passes the solved cells to it.
            val sudokuSolvedFragment = SudokuSolvedFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(SOLVED_CELLS_KEY, ArrayList(solvedCells))
                }
            }

            // Changes the fragment to the SudokuSolvedFragment
            parentFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_activity_main, sudokuSolvedFragment).commit()

        // If the Sudoku is not solvable, an alert is shown.
        } else {
            val unsolvableAlert = AlertDialog.Builder(context).setMessage(
                "This Sudoku is not solvable\n" +
                    "Please try again")
                .setPositiveButton("OK") { _, _ -> }.create()
            unsolvableAlert.show()
            Handler(Looper.getMainLooper()).postDelayed({
                unsolvableAlert.dismiss()
            }, 3000)
        }
    }


    /**
     * Disables the buttons that are not possible to input in the selected cell.
     * @param cell The selected cell.
     */
    private fun disableButtonsWrongBySudoku(cell: Pair<Int, Int>?) = cell.let {
        if (cell?.first == -1 || cell?.second == -1) return
        val cells = viewModel.sudokuGame.getCells()
        val selectedCell = cells[cell!!.first*9+cell.second]
        val selectedCellRow = selectedCell.row
        val selectedCellColumn = selectedCell.column
        val buttons = listOf(binding.deleteButton, binding.oneButton, binding.twoButton,
            binding.threeButton, binding.fourButton, binding.fiveButton,
            binding.sixButton, binding.sevenButton, binding.eightButton, binding.nineButton)
        buttons.forEachIndexed { index, button ->
            // If the button is the same as the value of the selected cell, it is enabled.
            button.isEnabled = true
            if (index != selectedCell.value)
            {
                // If a number that matches the number on the button is already in the
                // same row or column, the button is disabled.
                for (i in 0..8) {
                    if (cells[selectedCellRow*9+i].value != index && cells[i*9+selectedCellColumn].value != index) continue
                    if (button != binding.deleteButton) button.isEnabled = false
                }
                // If a number that matches the number on the button is already in the
                // same 3x3 square, the button is disabled.
                for (i in selectedCellRow/3*3..selectedCellRow/3*3+2) {
                    for (j in selectedCellColumn/3*3..selectedCellColumn/3*3+2) {
                        if (cells[i*9+j].value != index) continue
                        if (button != binding.deleteButton) button.isEnabled = false
                    }
                }
            }
        }
    }

    /**
     * Clears the cells in the Sudoku board.
     */
    private fun clearSudokuBoard() {
        binding.sudokuBoard.clearSudokuBoard()
        val emptyCells = List(9*9) { i -> Cell(i/9, i%9, 0) }
        viewModel.sudokuGame.setCells(emptyCells)
    }
}