package no.ntnu.prog2007.sudokusolver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import no.ntnu.prog2007.sudokusolver.databinding.ActivitySudokuBoardBinding
import no.ntnu.prog2007.sudokusolver.ui.insert.SudokuInsertFragment


class SudokuBoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySudokuBoardBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySudokuBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment = SudokuInsertFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(binding.SudokuBoardContainer.id, fragment).commit()
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
}