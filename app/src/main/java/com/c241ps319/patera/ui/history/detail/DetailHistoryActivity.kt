package com.c241ps319.patera.ui.history.detail

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.c241ps319.patera.R
import com.c241ps319.patera.databinding.ActivityDetailHistoryBinding

class DetailHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide()
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get Data From Intent
        val result = intent.getStringExtra(EXTRA_RESULT)
        val cause = intent.getStringExtra(EXTRA_CAUSE)
        val picture = intent.getStringExtra(EXTRA_PICTURE)
        val healing = intent.getStringExtra(EXTRA_HEALING)
        val prevention = intent.getStringExtra(EXTRA_PREVENTION)
        val cost = intent.getStringExtra(EXTRA_COST)

        // set data
        Glide.with(this).load(picture).into(binding.resultImage)
        binding.resultText.text = result
        binding.shimmerLayout.visibility = View.GONE
        binding.tvPrevention.visibility = View.VISIBLE
        binding.tvPrevention.text =
            prevention ?: ""
        binding.tvCost.visibility = View.VISIBLE
        binding.tvCost.text =
            cost ?: ""
        binding.tvCause.visibility = View.VISIBLE
        binding.tvCause.text =
            cause ?: ""
        binding.tvHealing.visibility = View.VISIBLE
        binding.tvHealing.text =
            healing ?: ""


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    companion object {
        const val EXTRA_RESULT = "EXTRA_RESULT"
        const val EXTRA_CAUSE = "EXTRA_CAUSE"
        const val EXTRA_HEALING = "EXTRA_HEALING"
        const val EXTRA_PREVENTION = "EXTRA_PREVENTION"
        const val EXTRA_COST = "EXTRA_COST"
        const val EXTRA_PICTURE = "EXTRA_PICTURE"
    }
}