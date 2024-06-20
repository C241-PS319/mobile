package com.c241ps319.patera.ui.report

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.c241ps319.patera.R
import com.c241ps319.patera.databinding.ActivityReportBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.InputStream

class ReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportBinding
    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            setImageToUploadPhoto(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        val spinner = findViewById<Spinner>(R.id.spinnerReportCategory)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.report_categories, R.layout.spinner_item
        )
        adapter.setDropDownViewResource(com.c241ps319.patera.R.layout.spinner_item)
        spinner.adapter = adapter
        spinner.setSelection(0) // Set the initial selection to the first item

        binding.llUploadPhoto.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.btnSubmit.setOnClickListener {

            if (binding.etProblemDescription.text.toString().isEmpty()) {
                binding.etProblemDescription.error = "Deskripsi tidak boleh kosong"
                showToast("Deskripsi tidak boleh kosong")
                return@setOnClickListener
            }

            // Disable UI elements immediately
            binding.btnSubmit.isEnabled = false
            binding.backButton.isEnabled = false
            binding.spinnerReportCategory.isEnabled = false

            // Start loading animation (assuming showLoading() handles this)
            showLoading(true)

            // Change button text to indicate submission in progress
            binding.btnSubmit.text = "Submitting..."

            lifecycleScope.launch {
                delay(3000)

                // On completion, navigate to success screen
                val intent = Intent(this@ReportActivity, ReportSuccessActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setImageToUploadPhoto(imageUri: Uri) {
        try {
            val imageStream: InputStream? = contentResolver.openInputStream(imageUri)
            val selectedImage = BitmapFactory.decodeStream(imageStream)
            binding.llUploadPhoto.background = BitmapDrawable(resources, selectedImage)
            binding.tvUploadPhotoHint.text = "" // Clear the hint text
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}