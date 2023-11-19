package no.ntnu.prog2007.sudokusolver.ui.file_selector

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import no.ntnu.prog2007.sudokusolver.FileManager
import no.ntnu.prog2007.sudokusolver.MainActivity
import no.ntnu.prog2007.sudokusolver.R
import no.ntnu.prog2007.sudokusolver.databinding.FragmentFilechooserBinding
import no.ntnu.prog2007.sudokusolver.game.Board.Companion.fromGridToCells
import java.io.File

class FileSelectorFragment : Fragment() {
    companion object {
        const val CHOSEN_GRID_KEY = "no.ntnu.prog2007.sudokusolver.CHOSEN_GRID_KEY"
    }

    private lateinit var binding: FragmentFilechooserBinding

    private var chosenSudokuGrid: List<List<Int>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilechooserBinding.inflate(inflater, container, false)

        openFileChooser()

        return binding.root
    }

    /**
     * Opens a file chooser activity page for choosing a sudoku file.
     */
    private fun openFileChooser() {
        val getContent = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    handleSelectedFile(uri)
                }
            } else {
                parentFragmentManager.popBackStack()
            }
        }
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_TITLE, "Select a file")
            val initialDirectory = File(requireContext().filesDir, getString(R.string.files_dir))
            if (!initialDirectory.exists()) {
                initialDirectory.mkdirs()
            }
            putExtra(
                "android.content.extra.SHOW_ADVANCED",
                File(initialDirectory, "your_file_name").absolutePath
            )
        }
        getContent.launch(intent)
    }

    /**
     * Defines what happens when you get a uri from the file chooser activity page.
     * @param uri the uri of the file chooser activity page.
     */
    private fun handleSelectedFile(uri: Uri) {
        try {
            val selectedFileType = getFileType(uri)
            if (selectedFileType == null) {
                Log.e("handleActivityResult", "Error when getting file type")
                showToast("Error when getting file type")
                openFileChooser()
                return
            }
            if (!FileManager.isSupportedFileType(selectedFileType)) {
                showToast("Unsupported file type")
                openFileChooser()
                return
            }
            chosenSudokuGrid = FileManager.readInputStream(
                requireContext().contentResolver.openInputStream(uri)!!, selectedFileType)

            goToInsertFragment()
        } catch (e: Exception) {
            Log.e("handleActivityResult", "Error processing file", e)
            showToast("Error processing file")
            openFileChooser()
            return
        }
    }

    /**
     * Changes the fragment to the insert fragment.
     */
    private fun goToInsertFragment() {
        val mainActivity = (activity as MainActivity)
        val insertFragment = mainActivity.sudokuInsertFragment.apply {
            arguments = Bundle().apply {
                val cells = fromGridToCells(chosenSudokuGrid!!)
                putParcelableArrayList(CHOSEN_GRID_KEY, ArrayList(cells))
            }
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, insertFragment).commit()
        val btmNavView = mainActivity.findViewById<BottomNavigationView>(R.id.nav_view)
        btmNavView.selectedItemId = R.id.navigation_sudoku_insert
    }

    /**
     * Returns the file name of an uri or null if there are none.
     * @param uri to get the file name from.
     * @return the file name of an uri or null if there are none.
     */
    private fun getFileName(uri: Uri): String? {
        var fileName: String? = null
        if (uri.scheme.equals("content")) {
            val cursor = requireContext().contentResolver.
            query(uri, null, null, null, null)
            cursor.use {
                if (it != null && it.moveToFirst()) {
                    val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIndex > 0) fileName = it.getString(columnIndex)
                }
            }
        }
        return fileName
    }

    /**
     * Returns the file extension of an uri or null if there are none.
     * @param uri to get the file extension from.
     * @return the file extension of an uri or null if there are none.
     */
    private fun getFileType(uri: Uri): String? {
        val fileName = getFileName(uri)
        return fileName?.substring(fileName.lastIndexOf(".") + 1)
    }

    private fun showToast(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
    }
}