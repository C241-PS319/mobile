package com.c241ps319.patera.ui.result

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.c241ps319.patera.R
import com.c241ps319.patera.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private val viewModel: ResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        val result = intent.getStringExtra(EXTRA_RESULT)

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

        val layoutManager = LinearLayoutManager(this)
        binding.rvNews.layoutManager = layoutManager
        binding.shimmerLayout.startShimmer()

//        TODO: Display recommendation
//        viewModel.news.observe(this){
//                articles ->
//            when(articles){
//                is ResultState.Loading -> {
//                    binding.shimmerLayout.startShimmer()
//                }
//                is ResultState.Success -> {
//                    showRecyclerView()
//                    setNewsData(articles.data)
//                }
//                is ResultState.Error -> {
//                    Log.e("ResultActivity", "Error: $articles")
//                    Toast.makeText(this, "Error: $articles", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }
}