package no.ntnu.prog2007.sudokusolver.ui.filechooser

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import no.ntnu.prog2007.sudokusolver.FileManager
import no.ntnu.prog2007.sudokusolver.databinding.FragmentFilechooserBinding

class FileChooserFragment : Fragment() {
    private var _binding: FragmentFilechooserBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { handleSelectedFile(it) }
        }
    private var chosenSudokuGrid: List<List<Int>>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fileChooserViewModel =
            ViewModelProvider(this).get(FileChooserViewModel::class.java)

        _binding = FragmentFilechooserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        openFileChooser()

        val textView: TextView = binding.textFileChooser
        fileChooserViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Opens a file chooser activity page for choosing a sudoku file.
     */
    private fun openFileChooser() {
        getContent.launch("*/*")
    }

    /**
     * Defines what happens when you get a result from the file chooser activity page.
     * @param result the result of the file chooser activity page.
     */
    private fun handleSelectedFile(uri: Uri) {
        try {
            val selectedFileType = getFileType(uri) ?: return
            if (!FileManager.isSupportedFileType(selectedFileType)) {
                showToast("Unsupported file type")
                return
            }

            //TODO: What will happen when file is chosen
            chosenSudokuGrid = FileManager.readInputStream(
                requireContext().contentResolver.openInputStream(uri)!!, selectedFileType)

        } catch (e: Exception) {
            Log.e("handleActivityResult", "Error processing file", e)

            showToast("Error processing file")
        }
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