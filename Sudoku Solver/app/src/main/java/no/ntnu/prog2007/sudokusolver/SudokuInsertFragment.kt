package no.ntnu.prog2007.sudokusolver

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import no.ntnu.prog2007.sudokusolver.databinding.FragmentSudokuInsertBinding
import no.ntnu.prog2007.sudokusolver.game.Cell
import no.ntnu.prog2007.sudokusolver.view.SudokuBoard
import no.ntnu.prog2007.sudokusolver.viewmodel.SudokuViewModel



/**
 * A simple [Fragment] subclass.
 * Use the [SudokuInsertFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SudokuInsertFragment : Fragment(), SudokuBoard.OnTouchListener {

    private lateinit var binding: FragmentSudokuInsertBinding
    private lateinit var viewModel: SudokuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentSudokuInsertBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        binding.sudokuBoard.registerListener(this)
        viewModel = ViewModelProvider(this)[SudokuViewModel::class.java]
        viewModel.sudokuGame.selectedLiveData.observe(viewLifecycleOwner) { updateSelectedCellUI(it) }
        viewModel.sudokuGame.cellsLiveData.observe(viewLifecycleOwner) { updateCells(it) }

        val buttons = listOf(binding.noneButton, binding.oneButton, binding.twoButton,
            binding.threeButton, binding.fourButton, binding.fiveButton,
            binding.sixButton, binding.sevenButton, binding.eightButton, binding.nineButton)

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener { viewModel.sudokuGame.handleInput(index) }
        }

        return fragmentBinding.root
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




}