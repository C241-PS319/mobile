package com.c241ps319.patera.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.c241ps319.patera.R
import com.c241ps319.patera.data.ResultState
import com.c241ps319.patera.databinding.ActivityRegisterBinding
import com.c241ps319.patera.ui.ViewModelFactory
import com.c241ps319.patera.ui.auth.AuthViewModel
import com.c241ps319.patera.ui.auth.RegisterSuccessActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val viewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance(this)
    }

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
            showToast("Fitur Belum Tersedia")
        }

        binding.tvLogin.setOnClickListener {
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val confirmPassword = binding.edRegisterConfirmPassword.text.toString()

            var isEmptyFields = false
            if (name.isEmpty()) {
                isEmptyFields = true
                binding.edRegisterName.error = "Tidak Boleh Kosong!"
            }
            if (email.isEmpty()) {
                isEmptyFields = true
                binding.edRegisterEmail.error = "Tidak Boleh Kosong!"
            }
            if (password.isEmpty()) {
                isEmptyFields = true
                binding.edRegisterPassword.error = "Tidak Boleh Kosong!"
            }
            if (confirmPassword.isEmpty()) {
                isEmptyFields = true
                binding.edRegisterConfirmPassword.error = "Tidak Boleh Kosong!"
            }
            if (!isEmptyFields) {
                if (password != confirmPassword) {
                    // Jika password dan konfirmasi password tidak sama, tampilkan pesan kesalahan
                    binding.edRegisterConfirmPassword.error =
                        "Konfirmasi password harus sama dengan password"
                } else {
                    viewModel.register(name, email, password).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is ResultState.Loading -> {
                                    showLoading(true)
                                }

                                is ResultState.Error -> {
                                    showToast(result.error.toString())
                                    showLoading(false)
                                }

                                is ResultState.Success -> {
                                    showToast(getString(R.string.account_created_successfully))
                                    showLoading(false)
                                    val intent = Intent(this, RegisterSuccessActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private val TAG = RegisterActivity::class.java.simpleName
    }
}