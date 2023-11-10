package no.ntnu.prog2007.sudokusolver.fileSelector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import no.ntnu.prog2007.sudokusolver.databinding.ActivityFileSelectorBinding

/**
 * Activity page for selecting a sudoku file to load in.
 */
class FileSelectorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFileSelectorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFileSelectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fileRecyclerView.adapter = FileSelectorAdapter(filesDir.listFiles())
    }
}