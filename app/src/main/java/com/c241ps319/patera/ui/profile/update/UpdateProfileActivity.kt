package com.c241ps319.patera.ui.profile.update

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.c241ps319.patera.databinding.ActivityUpdateProfileBinding

class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener on the back button
        binding.backButton.setOnClickListener {
            finish()
        }
    }
}