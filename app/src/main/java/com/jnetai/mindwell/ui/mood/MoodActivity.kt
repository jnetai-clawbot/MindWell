package com.jnetai.mindwell.ui.mood

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jnetai.mindwell.MindWellApp
import com.jnetai.mindwell.data.entity.MoodEntry
import com.jnetai.mindwell.databinding.ActivityMoodBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class MoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoodBinding
    private var selectedMood = 0

    private val moodEmojis = mapOf(
        1 to "😢",
        2 to "😟",
        3 to "😐",
        4 to "😊",
        5 to "😄"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Daily Mood"

        setupMoodButtons()
        loadTodayMood()

        binding.btnSaveMood.setOnClickListener { saveMood() }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupMoodButtons() {
        val buttons = listOf(
            binding.btnMood1 to 1,
            binding.btnMood2 to 2,
            binding.btnMood3 to 3,
            binding.btnMood4 to 4,
            binding.btnMood5 to 5
        )

        buttons.forEach { (btn, level) ->
            btn.text = moodEmojis[level]
            btn.setOnClickListener {
                selectedMood = level
                updateMoodSelection()
            }
        }
    }

    private fun updateMoodSelection() {
        val buttons = listOf(
            binding.btnMood1 to 1,
            binding.btnMood2 to 2,
            binding.btnMood3 to 3,
            binding.btnMood4 to 4,
            binding.btnMood5 to 5
        )

        buttons.forEach { (btn, level) ->
            val scale = if (level == selectedMood) 1.3f else 1.0f
            btn.animate().scaleX(scale).scaleY(scale).setDuration(150).start()
            btn.alpha = if (selectedMood == 0 || level == selectedMood) 1.0f else 0.4f
        }

        binding.tvMoodLabel.text = when (selectedMood) {
            1 -> "Very Low"
            2 -> "Low"
            3 -> "Okay"
            4 -> "Good"
            5 -> "Great"
            else -> "How are you feeling?"
        }
    }

    private fun loadTodayMood() {
        val today = LocalDate.now()
        lifecycleScope.launch {
            val entry = withContext(Dispatchers.IO) {
                (application as MindWellApp).database.moodDao().getByDate(today)
            }
            entry?.let {
                selectedMood = it.mood
                binding.etMoodNote.setText(it.note)
                updateMoodSelection()
            }
        }
    }

    private fun saveMood() {
        if (selectedMood == 0) {
            Toast.makeText(this, "Please select a mood level", Toast.LENGTH_SHORT).show()
            return
        }

        val today = LocalDate.now()
        val note = binding.etMoodNote.text.toString()

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                val existing = (application as MindWellApp).database.moodDao().getByDate(today)
                val entry = MoodEntry(
                    id = existing?.id ?: 0,
                    date = today,
                    mood = selectedMood,
                    note = note
                )
                (application as MindWellApp).database.moodDao().insert(entry)
            }
            Toast.makeText(this@MoodActivity, "Mood saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}