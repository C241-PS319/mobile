package com.c241ps319.patera.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.c241ps319.patera.data.ResultState
import com.c241ps319.patera.data.model.UserModel
import com.c241ps319.patera.databinding.ActivityLoginBinding
import com.c241ps319.patera.ui.ViewModelFactory
import com.c241ps319.patera.ui.auth.AuthViewModel
import com.c241ps319.patera.ui.auth.register.RegisterActivity
import com.c241ps319.patera.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<AuthViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLoginWithGoogle.setOnClickListener {
            showToast("Fitur Belum Tersedia")
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            var isEmptyFields = false
            if (email.isEmpty()) {
                isEmptyFields = true
                binding.edLoginEmail.error = "Tidak Boleh Kosong!"
            }
            if (password.isEmpty()) {
                isEmptyFields = true
                binding.edLoginPassword.error = "Tidak Boleh Kosong!"
            }

            if (!isEmptyFields) {
                viewModel.login(email, password).observe(this) { result ->
                    when (result) {
                        is ResultState.Loading -> {
                            showLoading(true)
                        }

                        is ResultState.Error -> {
                            showToast(result.error.toString())
                            showLoading(false)
                        }

                        is ResultState.Success -> {
                            val token = result.data.data.token
                            viewModel.getUser(token).observe(this) { userResult ->
                                when (userResult) {
                                    is ResultState.Loading -> {
                                        showLoading(true)
                                    }

                                    is ResultState.Error -> {
                                        showToast(userResult.error.toString())
                                        showLoading(false)
                                    }

                                    is ResultState.Success -> {
                                        showLoading(false)
                                        viewModel.saveSession(
                                            UserModel(
                                                name = userResult.data.userData.name,
                                                email = userResult.data.userData.email,
                                                phone = userResult.data.userData.phone,
                                                picture = userResult.data.userData.picture,
                                                token = token,
                                                isLogin = true
                                            )
                                        )
                                        val intent = Intent(this, MainActivity::class.java).apply {
                                            flags =
                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        }
                                        showToast("Login sukses!")
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
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }
}