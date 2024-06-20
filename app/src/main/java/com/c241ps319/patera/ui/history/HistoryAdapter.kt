package com.c241ps319.patera.ui.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.c241ps319.patera.data.model.History
import com.c241ps319.patera.databinding.ItemHistoryBinding
import com.c241ps319.patera.ui.history.detail.DetailHistoryActivity
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

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailHistoryActivity::class.java)
            intent.putExtra(DetailHistoryActivity.EXTRA_RESULT, history?.recommendation?.name)
            intent.putExtra(DetailHistoryActivity.EXTRA_CAUSE, history?.recommendation?.cause)
            intent.putExtra(DetailHistoryActivity.EXTRA_HEALING, history?.recommendation?.healing)
            intent.putExtra(
                DetailHistoryActivity.EXTRA_PREVENTION,
                history?.recommendation?.prevention
            )
            intent.putExtra(DetailHistoryActivity.EXTRA_COST, history?.recommendation?.cost)
            intent.putExtra(DetailHistoryActivity.EXTRA_PICTURE, history?.picture)

            holder.itemView.context.startActivity(intent)
        }
    }
}