package no.ntnu.prog2007.sudokusolver.ui.filechooser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import no.ntnu.prog2007.sudokusolver.databinding.FragmentFilechooserBinding

class FileChooserFragment : Fragment() {


    private var _binding: FragmentFilechooserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fileChooserViewModel =
            ViewModelProvider(this).get(FileChooserViewModel::class.java)

        _binding = FragmentFilechooserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.fileChooserText
        fileChooserViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}