package no.ntnu.prog2007.sudokusolver

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import no.ntnu.prog2007.sudokusolver.databinding.FragmentSudokuSolvedBinding
import no.ntnu.prog2007.sudokusolver.game.Cell
import no.ntnu.prog2007.sudokusolver.view.SudokuBoard
import no.ntnu.prog2007.sudokusolver.viewmodel.SudokuViewModel


class SudokuSolvedFragment : Fragment(), SudokuBoard.OnTouchListener {
    private lateinit var binding: FragmentSudokuSolvedBinding
    private lateinit var viewModel: SudokuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val solvedCells = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList("Cells", Cell::class.java)
        } else {
            arguments?.getParcelableArrayList("Cells")
        }
        solvedCells?.forEach {
            Log.d("SudokuSolvedFragment", "Cell: ${it.row}, ${it.column}, ${it.isInputCell}")
        }

        val fragmentBinding = FragmentSudokuSolvedBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        binding.sudokuBoard.registerListener(this)
        viewModel = ViewModelProvider(this)[SudokuViewModel::class.java]
        viewModel.sudokuGame.selectedLiveData.observe(viewLifecycleOwner) { updateSelectedCellUI(it) }
        viewModel.sudokuGame.cellsLiveData.observe(viewLifecycleOwner) { updateCells(solvedCells) }
        viewModel.sudokuGame.setCells(solvedCells?: listOf())

        return fragmentBinding.root
    }

    private fun updateCells(cells: List<Cell>?) = cells?.let {
        binding.sudokuBoard.updateCells(cells)
        cells.forEach {
            Log.d("SudokuSolvedFragment", "Cell: ${it.row}, ${it.column}, ${it.isInputCell}")
        }
    }

    private fun updateSelectedCellUI(cell: Pair<Int, Int>?) = cell?.let {
        binding.sudokuBoard.updateSelectedCell(cell.first, cell.second)
    }


    override fun onCellTouched(row: Int, column: Int) {
        viewModel.sudokuGame.updateSelectedCell(row, column)
    }

}