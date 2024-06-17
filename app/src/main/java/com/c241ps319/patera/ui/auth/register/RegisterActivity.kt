package com.c241ps319.patera.ui.auth.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.c241ps319.patera.R
import com.c241ps319.patera.databinding.ActivityRegisterBinding
import com.c241ps319.patera.ui.auth.RegisterSuccessActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        supportActionBar?.hide();
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnRegisterGoogle.setOnClickListener {
            val intent = Intent(this, RegisterSuccessActivity::class.java)
            startActivity(intent)
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }
    }
}