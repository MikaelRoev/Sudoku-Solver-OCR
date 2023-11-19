package no.ntnu.prog2007.sudokusolver.ui.solved

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import no.ntnu.prog2007.sudokusolver.MainActivity
import no.ntnu.prog2007.sudokusolver.databinding.FragmentSudokuSolvedBinding
import no.ntnu.prog2007.sudokusolver.game.Board
import no.ntnu.prog2007.sudokusolver.game.Cell
import no.ntnu.prog2007.sudokusolver.ui.insert.SudokuInsertFragment.Companion.SOLVED_CELLS_KEY
import no.ntnu.prog2007.sudokusolver.ui.save_dialog.SavingFragment
import no.ntnu.prog2007.sudokusolver.view.SudokuBoard
import no.ntnu.prog2007.sudokusolver.view.SudokuViewModel


/**
 * A Fragment that contains the Solved Sudoku board.
 */
class SudokuSolvedFragment : Fragment(), SudokuBoard.OnTouchListener,
    SavingFragment.SavingDialogListener {
    private lateinit var binding: FragmentSudokuSolvedBinding
    private lateinit var viewModel: SudokuViewModel

    /**
     * Creates the view for the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val solvedCells = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList(SOLVED_CELLS_KEY, Cell::class.java)
        } else {
            arguments?.getParcelableArrayList(SOLVED_CELLS_KEY)
        }

        val fragmentBinding = FragmentSudokuSolvedBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        binding.sudokuBoard.registerListener(this)
        viewModel = ViewModelProvider(this)[SudokuViewModel::class.java]
        viewModel.sudokuGame.selectedLiveData.observe(viewLifecycleOwner) { updateSelectedCellUI(it) }
        viewModel.sudokuGame.selectedLiveData.observe(viewLifecycleOwner) { revealSolvedCell(it) }
        viewModel.sudokuGame.cellsLiveData.observe(viewLifecycleOwner) { updateCells(solvedCells) }
        viewModel.sudokuGame.setCells(solvedCells?: listOf())

        binding.revealAllButton.setOnClickListener { revealAllSolvedCells() }

        return fragmentBinding.root
    }

    /**
     * Updates the cells on the Sudoku board.
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
     * Handles touch events on cells in the sudoku Board.
     */
    override fun onCellTouched(row: Int, column: Int) {
        viewModel.sudokuGame.updateSelectedCell(row, column)
    }

    /**
     * Reveals a solved cell when pressed.
     * @param cell The cell to reveal.
     */
    private fun revealSolvedCell(cell: Pair<Int, Int>?) = cell?.let {
        binding.sudokuBoard.revealSolvedCell(cell.first, cell.second)

    }

    /**
     * Reveals all solved cells when the button is pressed.
     */
    private fun revealAllSolvedCells() {
        binding.sudokuBoard.revealAllSolvedCells()
    }

    override fun onSaveClicked(fileName: String) {
        val cells = viewModel.sudokuGame.getCells().filter { it.isInputCell || it.isRevealed }
        (activity as MainActivity).onSaveClicked(fileName, Board.fromCellsToGrid(cells))
    }

}