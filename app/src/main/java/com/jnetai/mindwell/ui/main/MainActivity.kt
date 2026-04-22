package com.jnetai.mindwell.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jnetai.mindwell.databinding.ActivityMainBinding
import com.jnetai.mindwell.ui.mood.MoodActivity
import com.jnetai.mindwell.ui.journal.JournalActivity
import com.jnetai.mindwell.ui.breathing.BreathingActivity
import com.jnetai.mindwell.ui.coping.CopingActivity
import com.jnetai.mindwell.ui.crisis.CrisisActivity
import com.jnetai.mindwell.ui.tips.TipsActivity
import com.jnetai.mindwell.ui.chart.ChartActivity
import com.jnetai.mindwell.ui.about.AboutActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "MindWell"

        binding.cardMood.setOnClickListener { startActivity(Intent(this, MoodActivity::class.java)) }
        binding.cardJournal.setOnClickListener { startActivity(Intent(this, JournalActivity::class.java)) }
        binding.cardBreathing.setOnClickListener { startActivity(Intent(this, BreathingActivity::class.java)) }
        binding.cardCoping.setOnClickListener { startActivity(Intent(this, CopingActivity::class.java)) }
        binding.cardCrisis.setOnClickListener { startActivity(Intent(this, CrisisActivity::class.java)) }
        binding.cardTips.setOnClickListener { startActivity(Intent(this, TipsActivity::class.java)) }
        binding.cardChart.setOnClickListener { startActivity(Intent(this, ChartActivity::class.java)) }
        binding.cardAbout.setOnClickListener { startActivity(Intent(this, AboutActivity::class.java)) }
    }
}