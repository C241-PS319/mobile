package com.c241ps319.patera.ui.report

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.c241ps319.patera.R
import com.c241ps319.patera.databinding.ActivityReportSuccessBinding

class ReportSuccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityReportSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackToHome.setOnClickListener {
            finish()
        }
    }
}