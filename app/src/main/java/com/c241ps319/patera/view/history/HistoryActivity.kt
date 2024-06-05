package com.c241ps319.patera.view.history

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.c241ps319.patera.R
import com.c241ps319.patera.data.local.HistoryEntity
import com.c241ps319.patera.databinding.ActivityHistoryBinding
import com.c241ps319.patera.utils.HistoryAdapter
import com.c241ps319.patera.utils.ViewModelFactory

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val viewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setCustomView(R.layout.app_bar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(R.color.green)))

        val layoutManager = LinearLayoutManager(this)
        binding.historyList.layoutManager = layoutManager

        viewModel.historyList.observe(this){
            setHistoryData(it)
        }
    }

    private fun setHistoryData(consumer:List<HistoryEntity>){
        val adapter = HistoryAdapter()
        adapter.submitList(consumer)
        binding.historyList.adapter = adapter
    }
}