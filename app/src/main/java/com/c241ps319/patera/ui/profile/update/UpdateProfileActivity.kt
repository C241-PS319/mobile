package com.c241ps319.patera.ui.profile.update

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.c241ps319.patera.R
import com.c241ps319.patera.data.ResultState
import com.c241ps319.patera.data.model.UserModel
import com.c241ps319.patera.databinding.ActivityUpdateProfileBinding
import com.c241ps319.patera.ui.ViewModelFactory
import com.c241ps319.patera.ui.profile.ProfileViewModel

class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileBinding
    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        Data Sebelumnya
        val nameOld = intent.getStringExtra(EXTRA_NAME)
        val emailOld = intent.getStringExtra(EXTRA_EMAIL)

//        get Token
        val token = intent.getStringExtra(EXTRA_TOKEN)

        // Set click listener on the back button
        binding.backButton.setOnClickListener {
            finish()
        }

        supportActionBar?.hide()

//        Default Value
        binding.editTextFullName.setText(nameOld)
        binding.editTextEmail.setText(emailOld)

        binding.buttonSave.setOnClickListener {

            val name = binding.editTextFullName.text.toString()
            val email = binding.editTextEmail.text.toString()

            var isEmptyFields = false
            if (name.isEmpty()) {
                isEmptyFields = true
                binding.editTextFullName.error = "Tidak Boleh Kosong!"
            }
            if (email.isEmpty()) {
                isEmptyFields = true
                binding.editTextEmail.error = "Tidak Boleh Kosong!"
            }
            if (!isEmptyFields) {

                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.confirm))
                    .setMessage(getString(R.string.update_profile_confirmation))
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        viewModel.updateProfile(token!!, name, email).observe(this) { result ->
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
                                        showToast("Profile berhasil diupdate!")

                                        // Update data Session
                                        viewModel.updateSession(
                                            UserModel(
                                                name = name, email = email, token = token
                                            )
                                        )
                                        showLoading(false)
                                    }
                                }
                            }
                        }
                    }
                    .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                        // Cancel the dialog
                        dialog.dismiss()
                    }
                    .create()
                    .show()

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
        const val EXTRA_NAME = "EXTRA_NAME"
        const val EXTRA_EMAIL = "EXTRA_EMAIL"
        const val EXTRA_TOKEN = "EXTRA_TOKEN"

    }
}