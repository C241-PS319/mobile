package com.c241ps319.patera.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.c241ps319.patera.data.model.History
import com.c241ps319.patera.databinding.ItemHistoryBinding
import com.c241ps319.patera.utils.DateUtils

class HistoryAdapter(private val histories: List<History?>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    class HistoryViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return histories.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val history = histories[position]

        holder.binding.apply {
            tvDate.text = DateUtils.formatDate(history?.createdAt!!)
            tvTime.text = DateUtils.formatTime(history.createdAt)
            Glide.with(ivItemHistory.context).load(history.picture).into(ivItemHistory)
        }
    }
}