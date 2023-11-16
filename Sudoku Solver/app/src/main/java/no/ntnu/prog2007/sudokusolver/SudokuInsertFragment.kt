package no.ntnu.prog2007.sudokusolver

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import no.ntnu.prog2007.sudokusolver.databinding.ActivitySudokuBoardBinding
import no.ntnu.prog2007.sudokusolver.databinding.FragmentSudokuInsertBinding
import no.ntnu.prog2007.sudokusolver.game.Cell
import no.ntnu.prog2007.sudokusolver.view.SudokuBoard
import no.ntnu.prog2007.sudokusolver.viewmodel.SudokuViewModel




class SudokuInsertFragment : Fragment(), SudokuBoard.OnTouchListener {

    private lateinit var binding: FragmentSudokuInsertBinding
    private lateinit var viewModel: SudokuViewModel

    private var originalCells: List<Cell>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentSudokuInsertBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        binding.sudokuBoard.registerListener(this)
        viewModel = ViewModelProvider(this)[SudokuViewModel::class.java]
        if (originalCells != null) {
            originalCells?.forEach {
                Log.d("SudokuInsertFragment", "Cell: ${it.row}, ${it.column}, ${it.value}")
            }
            viewModel.sudokuGame.setCells(originalCells!!)
            viewModel.sudokuGame.getCells().forEach {
                it.isInputCell = false
            }
            viewModel.sudokuGame.cellsLiveData.observe(viewLifecycleOwner) { updateCells(viewModel.sudokuGame.getCells()) }
        } else {
            viewModel.sudokuGame.cellsLiveData.observe(viewLifecycleOwner) { updateCells(it) }
        }
        viewModel.sudokuGame.selectedLiveData.observe(viewLifecycleOwner) { updateSelectedCellUI(it) }

        val buttons = listOf(binding.noneButton, binding.oneButton, binding.twoButton,
            binding.threeButton, binding.fourButton, binding.fiveButton,
            binding.sixButton, binding.sevenButton, binding.eightButton, binding.nineButton)

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener { viewModel.sudokuGame.handleInput(index) }
        }

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.solveButton.setOnClickListener { solveSudokuAndChangeFragment() }
    }

    private fun updateCells(cells: List<Cell>?) = cells?.let {
        binding.sudokuBoard.updateCells(cells)
    }

    private fun updateSelectedCellUI(cell: Pair<Int, Int>?) = cell?.let {
        binding.sudokuBoard.updateSelectedCell(cell.first, cell.second)
    }

    override fun onCellTouched(row: Int, column: Int) {
        viewModel.sudokuGame.updateSelectedCell(row, column)
    }

    private fun solveSudokuAndChangeFragment() {
        val cells = viewModel.sudokuGame.getCells()
        originalCells = cells
        val grid = fromCellsToGrid(cells)
        val sudoku = Sudoku(grid)
        sudoku.solve()
        if (sudoku.solved) {
            cells.forEach {
                if (it.value != 0) it.isInputCell = true
                Log.d("SudokuInsertFragment", "Cell: ${it.row}, ${it.column}, ${it.value}, ${it.isInputCell}")
            }
            val solvedCells = updateCellValues(sudoku.solution)
            solvedCells.forEach {
                if (cells[it.row*9+it.column].value != 0) it.isInputCell = true
            }

            val sudokuSolvedFragment = SudokuSolvedFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("SolvedCells", ArrayList(solvedCells))
                }
            }

            parentFragmentManager.beginTransaction().apply {
                replace(R.id.SudokuBoardContainer, sudokuSolvedFragment)
                    .addToBackStack(null).commit()
            }
        } else {
            val unsolvableAlert = AlertDialog.Builder(context).setMessage("This Sudoku is not solvable\n" +
                    "Please try again")
                .setPositiveButton("OK") { _, _ -> }.create()
            unsolvableAlert.show()
            Handler(Looper.getMainLooper()).postDelayed({
                unsolvableAlert.dismiss()
            }, 3000)
        }



    }
    private fun fromCellsToGrid(cells: List<Cell>): List<List<Int>> {
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

    private fun updateCellValues(grid: List<List<Int>>): List<Cell> {
        val cells = mutableListOf<Cell>()
        for (row in 0..8) {
            for (col in 0..8) {
                cells.add(Cell(row, col, grid[row][col]))
            }
        }
        return cells
    }


}