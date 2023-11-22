package no.ntnu.prog2007.sudokusolver.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import no.ntnu.prog2007.sudokusolver.R
import no.ntnu.prog2007.sudokusolver.databinding.FragmentInfoBinding

/**
 * Fragment that represents the info page.
 */
class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)

        binding.textInfo.text =
            getString(R.string.info_text,
                getString(R.string.title_insert),
                getString(R.string.title_home),
                getString(R.string.title_file_selector))
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}