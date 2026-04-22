package com.jnetai.mindwell.ui.tips

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jnetai.mindwell.R
import com.jnetai.mindwell.databinding.ActivityTipsBinding
import kotlin.random.Random

class TipsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTipsBinding

    private val tips = listOf(
        "Take a 5-minute walk. Even short movement can shift your mood.",
        "Write down 3 things you're grateful for today.",
        "Drink a glass of water. Dehydration affects mood more than you think.",
        "Practice the 5-4-3-2-1 grounding technique when feeling anxious.",
        "Set one small, achievable goal for today. Celebrate when you complete it.",
        "Reach out to someone you trust. Connection is a basic human need.",
        "Try a 4-7-8 breathing exercise before bed for better sleep.",
        "Limit social media scrolling. Set a timer if needed.",
        "Step outside for natural light. It helps regulate your circadian rhythm.",
        "Do one thing that brings you joy today, no matter how small.",
        "It's okay to say no. Boundaries protect your energy.",
        "Notice your self-talk. Would you say those things to a friend?",
        "Take breaks. Your brain needs rest to function well.",
        "Eat something nourishing. Your mood and food are connected.",
        "Progress isn't always visible. Small steps still count.",
        "Try progressive muscle relaxation when tension builds up.",
        "Keep a regular sleep schedule, even on weekends.",
        "Forgive yourself for bad days. They happen to everyone.",
        "Music can shift your mood. Create a feel-good playlist.",
        "Unclutter one small space. Outer order can create inner calm.",
        "A cold splash of water on your face can reset your nervous system.",
        "Focus on what you can control, let go of what you can't.",
        "Remember: feelings are temporary. This too shall pass.",
        "Do something kind for someone else — it boosts your mood too.",
        "Spend time in nature. Even a park bench for 10 minutes helps.",
        "Your worth isn't defined by your productivity.",
        "Laugh intentionally. Watch something funny — it really does help.",
        "Deep breaths. You've survived every hard day so far."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Daily Wellness Tips"

        showRandomTip()
        binding.btnNewTip.setOnClickListener { showRandomTip() }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun showRandomTip() {
        val tip = tips[Random.nextInt(tips.size)]
        binding.tvTipText.text = tip
        binding.tvTipText.animate().alpha(0f).setDuration(0).withEndAction {
            binding.tvTipText.animate().alpha(1f).setDuration(400).start()
        }.start()
    }
}