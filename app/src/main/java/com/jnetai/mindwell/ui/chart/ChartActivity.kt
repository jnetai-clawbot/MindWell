package com.jnetai.mindwell.ui.chart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jnetai.mindwell.MindWellApp
import com.jnetai.mindwell.data.entity.MoodEntry
import com.jnetai.mindwell.databinding.ActivityChartBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class ChartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChartBinding
    private var currentWeekStart: LocalDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Weekly Mood Chart"

        binding.btnPrevWeek.setOnClickListener {
            currentWeekStart = currentWeekStart.minusWeeks(1)
            loadWeekData()
        }
        binding.btnNextWeek.setOnClickListener {
            currentWeekStart = currentWeekStart.plusWeeks(1)
            loadWeekData()
        }

        loadWeekData()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun loadWeekData() {
        val weekEnd = currentWeekStart.plusDays(6)
        val formatter = DateTimeFormatter.ofPattern("MMM dd")
        binding.tvWeekRange.text = "${currentWeekStart.format(formatter)} — ${weekEnd.format(formatter)}"

        lifecycleScope.launch {
            val entries: Map<LocalDate, MoodEntry> = withContext(Dispatchers.IO) {
                (application as MindWellApp).database.moodDao()
                    .getByDateRange(currentWeekStart, weekEnd)
                    .associateBy { it.date }
            }

            val dayLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            val data = (0..6).map { i ->
                val date = currentWeekStart.plusDays(i.toLong())
                val entry = entries[date]
                MoodBarChartView.MoodDay(
                    dayLabel = dayLabels[i],
                    mood = entry?.mood,
                    date = date
                )
            }

            binding.moodChart.moodData = data

            // Compute average
            val moodsWithData = data.mapNotNull { it.mood }
            if (moodsWithData.isNotEmpty()) {
                val avg = moodsWithData.average()
                binding.tvWeekAverage.text = "Week Average: ${"%.1f".format(avg)} / 5.0"
            } else {
                binding.tvWeekAverage.text = "No data this week"
            }
        }
    }
}