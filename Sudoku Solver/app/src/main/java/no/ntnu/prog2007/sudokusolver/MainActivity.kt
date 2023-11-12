package no.ntnu.prog2007.sudokusolver

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import no.ntnu.prog2007.sudokusolver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //TODO: move this and the connected functions wherever files should be chosen
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleActivityResult(result)
        }
    private var chosenSudokuGrid: List<List<Int>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener { openFileChooser() }
    }

    /**
     * Opens a file chooser activity page for choosing a sudoku file.
     */
    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        getContent.launch(intent)
    }

    /**
     * Defines what happens when you get a result from the file chooser activity page.
     * @param result the result of the file chooser activity page.
     */
    private fun handleActivityResult(result: ActivityResult) {
        try {
            if (result.resultCode != Activity.RESULT_OK) {
                showToast("File selection canceled")
                return
            }

            val data: Intent = result.data ?: return
            val uri = data.data ?: return

            val selectedFileType = getFileType(uri) ?: return
            if (!FileManager.isSupportedFileType(selectedFileType)) {
                showToast("Unsupported file type")
                return
            }

            //TODO: What will happen when file is chosen
            chosenSudokuGrid = FileManager.readInputStream(
                contentResolver.openInputStream(uri)!!, selectedFileType)
            binding.text.text = chosenSudokuGrid.toString()

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
            val cursor = contentResolver.
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
        Toast.makeText(applicationContext, string, Toast.LENGTH_SHORT).show()
    }
}