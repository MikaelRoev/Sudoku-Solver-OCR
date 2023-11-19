package no.ntnu.prog2007.sudokusolver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.fragment.app.Fragment
import no.ntnu.prog2007.sudokusolver.databinding.ActivityMainBinding
import no.ntnu.prog2007.sudokusolver.ui.file_selector.FileSelectorFragment
import no.ntnu.prog2007.sudokusolver.ui.home.HomeFragment
import no.ntnu.prog2007.sudokusolver.ui.info.InfoFragment
import no.ntnu.prog2007.sudokusolver.ui.insert_and_solve.SudokuInsertFragment
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    lateinit var sudokuInsertFragment: SudokuInsertFragment
        private set
    private lateinit var fileChooserFragment: FileSelectorFragment
    private lateinit var infoFragment: InfoFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        homeFragment = HomeFragment()
        sudokuInsertFragment = SudokuInsertFragment()
        fileChooserFragment = FileSelectorFragment()
        infoFragment = InfoFragment()

        replaceFragment(homeFragment)
        binding.navView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_home -> replaceFragment(homeFragment)
                R.id.navigation_sudoku_insert -> replaceFragment(sudokuInsertFragment)
                R.id.navigation_file_selector -> replaceFragment(fileChooserFragment)
                R.id.navigation_info -> replaceFragment(infoFragment)
                else -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate()
        } else {
            super.onBackPressed()
        }
    }

    /**
     * Replaces the current fragment with the given fragment.
     */
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    /**
     * Saves the grid to file
     * @param fileName of the file
     * @param grid to be saved
     */
     fun onSaveClicked(fileName: String, grid: List<List<Int>>) {
        val directoryName = getString(R.string.files_dir)
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val fileDirectory = File(dir, directoryName)
        // Create the directory if it doesn't exist
        if (!fileDirectory.exists()) {
            fileDirectory.mkdirs()
        }

        val name = "$fileName.spf"
        val filepath = File(fileDirectory, name).absolutePath
        if (FileManager.writeFileSPF(filepath, grid)) {
            Toast.makeText(this, "$name saved successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error saving file", Toast.LENGTH_SHORT).show()
        }
    }
}