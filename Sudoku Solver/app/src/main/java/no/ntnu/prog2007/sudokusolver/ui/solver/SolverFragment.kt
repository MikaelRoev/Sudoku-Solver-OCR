package no.ntnu.prog2007.sudokusolver.ui.solver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import no.ntnu.prog2007.sudokusolver.databinding.FragmentSolverBinding

class SolverFragment : Fragment() {

    private var _binding: FragmentSolverBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val solverViewModel =
            ViewModelProvider(this).get(SolverViewModel::class.java)

        _binding = FragmentSolverBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.solverText
        solverViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}