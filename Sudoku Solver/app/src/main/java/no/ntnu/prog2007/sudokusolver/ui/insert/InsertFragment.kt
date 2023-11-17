package no.ntnu.prog2007.sudokusolver.ui.insert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import no.ntnu.prog2007.sudokusolver.databinding.FragmentInsertBinding

class InsertFragment : Fragment() {

    private var _binding: FragmentInsertBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val insertViewModel =
            ViewModelProvider(this).get(InsertViewModel::class.java)

        _binding = FragmentInsertBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textInsert
        insertViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}