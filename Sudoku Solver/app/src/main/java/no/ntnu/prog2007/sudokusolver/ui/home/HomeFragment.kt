package no.ntnu.prog2007.sudokusolver.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import no.ntnu.prog2007.sudokusolver.MainActivity
import no.ntnu.prog2007.sudokusolver.R
import no.ntnu.prog2007.sudokusolver.databinding.FragmentHomeBinding

/**
 * The fragment that represents the home page.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.insertButton.setOnClickListener{onInsertButtonClick()}
        binding.galleryButton.setOnClickListener{onGalleryButtonClick()}
        binding.infoButton.setOnClickListener{onInfoButtonClick()}

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Changes the fragment to the insert fragment.
     */
    private fun onInsertButtonClick() {
        val mainActivity = (activity as MainActivity)
        val btmNavView = mainActivity.findViewById<BottomNavigationView>(R.id.nav_view)
        btmNavView.selectedItemId = R.id.navigation_sudoku_insert
    }

    /**
     * Changes the fragment to the file selector fragment.
     */
    private fun onGalleryButtonClick() {
        val mainActivity = (activity as MainActivity)
        val btmNavView = mainActivity.findViewById<BottomNavigationView>(R.id.nav_view)
        btmNavView.selectedItemId = R.id.navigation_file_selector
    }

    /**
     * Changes the fragment to the info fragment.
     */
    private fun onInfoButtonClick(){
        val mainActivity = (activity as MainActivity)
        val btmNavView = mainActivity.findViewById<BottomNavigationView>(R.id.nav_view)
        btmNavView.selectedItemId = R.id.navigation_info
    }
}