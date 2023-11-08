package no.ntnu.prog2007.sudokusolver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import no.ntnu.prog2007.sudokusolver.databinding.ActivityMainBinding
import no.ntnu.prog2007.sudokusolver.databinding.ActivitySudokuBoardBinding

class SudokuBoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySudokuBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySudokuBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = SudokuInsertFragment()

        fragmentTransaction.replace(binding.SudokuBoardContainer.id, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}