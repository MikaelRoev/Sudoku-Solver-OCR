package no.ntnu.prog2007.sudokusolver

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import no.ntnu.prog2007.sudokusolver.databinding.ActivityHomeBinding
import no.ntnu.prog2007.sudokusolver.ui.info.InfoFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        binding = ActivityHomeBinding.inflate(layoutInflater)
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
