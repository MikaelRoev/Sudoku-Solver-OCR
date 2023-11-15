package no.ntnu.prog2007.sudokusolver

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import no.ntnu.prog2007.sudokusolver.databinding.ActivitySudokuBoardBinding
import no.ntnu.prog2007.sudokusolver.databinding.FragmentSudokuInsertBinding
import no.ntnu.prog2007.sudokusolver.game.Cell
import no.ntnu.prog2007.sudokusolver.view.SudokuBoard
import no.ntnu.prog2007.sudokusolver.viewmodel.SudokuViewModel




class SudokuInsertFragment : Fragment(), SudokuBoard.OnTouchListener {

    private lateinit var binding: FragmentSudokuInsertBinding
    private lateinit var viewModel: SudokuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentSudokuInsertBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        binding.sudokuBoard.registerListener(this)
        viewModel = ViewModelProvider(this)[SudokuViewModel::class.java]
        viewModel.sudokuGame.selectedLiveData.observe(viewLifecycleOwner) { updateSelectedCellUI(it) }
        viewModel.sudokuGame.getCells().forEach {
            it.isInputCell = false
        }
       viewModel.sudokuGame.cellsLiveData.observe(viewLifecycleOwner) { updateCells(it) }

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
        val solvedCells = viewModel.sudokuGame.getCells()
        solvedCells.forEach {
            if (it.value != 0) it.isInputCell = true
        }

        val sudokuSolvedFragment = SudokuSolvedFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList("Cells", ArrayList(solvedCells))
            }
        }

        parentFragmentManager.beginTransaction().apply {
            replace(R.id.SudokuBoardContainer, sudokuSolvedFragment)
                .addToBackStack(null).commit()
        }



    }


}