package no.ntnu.prog2007.sudokusolver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import no.ntnu.prog2007.sudokusolver.databinding.ActivityMainBinding
import no.ntnu.prog2007.sudokusolver.ui.file_selector.FileSelectorFragment
import no.ntnu.prog2007.sudokusolver.ui.home.HomeFragment
import no.ntnu.prog2007.sudokusolver.ui.info.InfoFragment
import no.ntnu.prog2007.sudokusolver.ui.insert.SudokuInsertFragment

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

}