package com.jnetai.mindwell.ui.journal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jnetai.mindwell.data.entity.JournalEntry
import com.jnetai.mindwell.databinding.ItemJournalBinding
import java.time.format.DateTimeFormatter

class JournalAdapter(
    private val onClick: (JournalEntry) -> Unit
) : RecyclerView.Adapter<JournalAdapter.ViewHolder>() {

    private var entries: List<JournalEntry> = emptyList()

    fun submitList(list: List<JournalEntry>) {
        entries = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemJournalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(entries[position])
    }

    override fun getItemCount() = entries.size

    inner class ViewHolder(private val binding: ItemJournalBinding) : RecyclerView.ViewHolder(binding.root) {
        private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

        fun bind(entry: JournalEntry) {
            binding.tvTitle.text = entry.title
            binding.tvDate.text = entry.date.format(dateFormatter)
            binding.tvPreview.text = entry.content.take(100) + if (entry.content.length > 100) "…" else ""
            binding.tvTags.text = entry.tags.joinToString(", ") { "#$it" }
            binding.root.setOnClickListener { onClick(entry) }
        }
    }
}