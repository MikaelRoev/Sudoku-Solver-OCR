package no.ntnu.prog2007.sudokusolver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import no.ntnu.prog2007.sudokusolver.databinding.ActivityMainBinding
import no.ntnu.prog2007.sudokusolver.ui.info.InfoFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.insertButton.setOnClickListener { getActivityContent() }
        binding.galleryButton.setOnClickListener { getActivityContent() }
        binding.infoButton.setOnClickListener { getActivityContent() }

    }

    private fun getActivityContent() {
        intent = Intent(this, ContentActivity::class.java)
        startActivity(intent)
    }

    private fun getFragmentInfo() {
        intent = Intent(this, InfoFragment::class.java)

    }
}
