package com.jnetai.mindwell.ui.journal

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jnetai.mindwell.MindWellApp
import com.jnetai.mindwell.data.entity.JournalEntry
import com.jnetai.mindwell.databinding.ActivityJournalEditBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class JournalEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJournalEditBinding
    private var editEntryId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJournalEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Journal Entry"

        editEntryId = intent.getLongExtra("ENTRY_ID", -1L)

        if (editEntryId > 0) {
            loadEntry()
        }

        binding.btnSave.setOnClickListener { saveEntry() }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun loadEntry() {
        lifecycleScope.launch {
            val entry = withContext(Dispatchers.IO) {
                (application as MindWellApp).database.journalDao().getById(editEntryId)
            }
            entry?.let {
                binding.etTitle.setText(it.title)
                binding.etContent.setText(it.content)
                binding.etTags.setText(it.tags.joinToString(", "))
            }
        }
    }

    private fun saveEntry() {
        val title = binding.etTitle.text.toString().trim()
        val content = binding.etContent.text.toString().trim()
        if (title.isBlank()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
            return
        }
        if (content.isBlank()) {
            Toast.makeText(this, "Please write something", Toast.LENGTH_SHORT).show()
            return
        }

        val tags = binding.etTags.text.toString()
            .split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val entry = JournalEntry(
                    id = if (editEntryId > 0) editEntryId else 0,
                    title = title,
                    content = content,
                    date = LocalDate.now(),
                    tags = tags
                )
                (application as MindWellApp).database.journalDao().insert(entry)
            }
            Toast.makeText(this@JournalEditActivity, "Entry saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}