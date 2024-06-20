package com.c241ps319.patera.ui.result

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.c241ps319.patera.data.ResultState
import com.c241ps319.patera.databinding.ActivityResultBinding
import com.c241ps319.patera.ui.ViewModelFactory
import com.c241ps319.patera.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    //    View Model For Upload Image
    private val uploadImageViewModel: UploadImageViewModel by viewModels()

    //    View Model For Get Recommendation
    private val recommendationViewModel by viewModels<RecommendationViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var urlImageUploaded: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        // Get data from intent
        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val result = intent.getStringExtra(EXTRA_RESULT)
        val indexLabel = intent.getIntExtra(EXTRA_INDEX, 0)
        val pathLabel = indexLabel + 1
        //  get Token
        val token = intent.getStringExtra(EXTRA_TOKEN)

        binding.btnBack.setOnClickListener {
            finish()
        }

        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.resultImage.setImageURI(it)
        }

        result?.let {
            Log.d("Result", "showResult: $it")
            binding.resultText.text = it
        }
        binding.shimmerLayout.startShimmer()

//        Upload image to get URL
        // Mengonversi URI menjadi File
        val file = uriToFile(imageUri, this)
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        uploadImageViewModel.uploadImage(body).observe(this) { res ->
            when (res) {
                is ResultState.Loading -> {
                    showLoading(true)
                }

                is ResultState.Error -> {
                    showToast(res.error.toString())
                    showLoading(false)
                }

                is ResultState.Success -> {
                    showLoading(false)
                    urlImageUploaded = res.data.dataImage?.urlImage!!

                    // Pemanggilan fungsi untuk mendapatkan rekomendasi hanya dijalankan setelah upload berhasil
                    recommendationViewModel.getRecommendation(
                        token!!,
                        pathLabel,
                        urlImageUploaded
                    ).observe(this) { state ->
                        when (state) {
                            is ResultState.Loading -> {
                                showLoading(true)
                            }

                            is ResultState.Error -> {
                                showToast(state.error.toString())
                                showLoading(false)
                            }

                            is ResultState.Success -> {
                                showLoading(false)
                                showToast("Sukses menyimpan riwayat")

                                // Set data rekomendasi
                                binding.shimmerLayout.visibility = View.GONE
                                binding.tvPrevention.visibility = View.VISIBLE
                                binding.tvPrevention.text =
                                    state.data.recommendationData?.prevention ?: ""
                                binding.tvCost.visibility = View.VISIBLE
                                binding.tvCost.text =
                                    state.data.recommendationData?.prevention ?: ""
                                binding.tvCause.visibility = View.VISIBLE
                                binding.tvCause.text =
                                    state.data.recommendationData?.prevention ?: ""
                                binding.tvHealing.visibility = View.VISIBLE
                                binding.tvHealing.text =
                                    state.data.recommendationData?.prevention ?: ""
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
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
        const val EXTRA_INDEX = "extra_index"
        const val EXTRA_TOKEN = "extra_token"
        private val TAG = ResultActivity::class.java.simpleName
    }
}