package com.jnetai.mindwell.ui.journal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jnetai.mindwell.MindWellApp
import com.jnetai.mindwell.data.entity.JournalEntry
import com.jnetai.mindwell.databinding.ActivityJournalBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JournalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJournalBinding
    private lateinit var adapter: JournalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJournalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Journal"

        adapter = JournalAdapter { entry ->
            val intent = Intent(this, JournalEditActivity::class.java)
            intent.putExtra("ENTRY_ID", entry.id)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.fabAddEntry.setOnClickListener {
            startActivity(Intent(this, JournalEditActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadEntries()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun loadEntries() {
        lifecycleScope.launch {
            val entries = withContext(Dispatchers.IO) {
                (application as MindWellApp).database.journalDao().getAll()
            }
            adapter.submitList(entries)
            binding.tvEmptyState.visibility = if (entries.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        }
    }
}