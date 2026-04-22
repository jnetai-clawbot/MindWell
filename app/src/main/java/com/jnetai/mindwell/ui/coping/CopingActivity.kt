package com.jnetai.mindwell.ui.coping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jnetai.mindwell.databinding.ActivityCopingBinding

class CopingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCopingBinding

    data class CopingStrategy(val title: String, val description: String, val category: String)

    private val strategies = listOf(
        // Breathing
        CopingStrategy("4-7-8 Breathing", "Inhale for 4 seconds, hold for 7, exhale for 8. This activates your parasympathetic nervous system.", "breathing"),
        CopingStrategy("Box Breathing", "Inhale 4s, hold 4s, exhale 4s, hold 4s. Used by Navy SEALs for calm under pressure.", "breathing"),
        CopingStrategy("Diaphragmatic Breathing", "Place one hand on chest, one on belly. Breathe so only the belly hand moves. 5-10 minutes.", "breathing"),
        CopingStrategy("Alternate Nostril Breathing", "Close right nostril, inhale left. Close left, exhale right. Alternate for 5 minutes.", "breathing"),

        // Mindfulness
        CopingStrategy("5-4-3-2-1 Grounding", "Name 5 things you see, 4 you touch, 3 you hear, 2 you smell, 1 you taste. Brings you to the present.", "mindfulness"),
        CopingStrategy("Body Scan", "Starting from toes, slowly notice each body part. Tense and release each area. Takes 10-15 minutes.", "mindfulness"),
        CopingStrategy("Mindful Observation", "Pick an object nearby. Observe it as if seeing it for the first time — color, texture, shape, shadows.", "mindfulness"),
        CopingStrategy("Thought Clouds", "Imagine your thoughts as clouds passing across the sky. Watch them drift without holding on to any.", "mindfulness"),

        // Physical
        CopingStrategy("Progressive Muscle Relaxation", "Tense each muscle group for 5s then release. Start from feet and work up. 15-20 minutes.", "physical"),
        CopingStrategy("Quick Walk", "Even 10 minutes of walking reduces anxiety. Focus on the sensation of your feet on the ground.", "physical"),
        CopingStrategy("Cold Water Reset", "Splash cold water on your face or hold an ice cube. Triggers the dive reflex, slowing heart rate.", "physical"),
        CopingStrategy("Stretching", "Gentle neck rolls, shoulder shrugs, and back stretches release stored tension. 5 minutes helps.", "physical"),

        // Social
        CopingStrategy("Call Someone", "Reach out to a friend or family member. You don't have to talk about what's bothering you.", "social"),
        CopingStrategy("Support Group", "Online or in-person groups connect you with others who understand. You're not alone.", "social"),
        CopingStrategy("Write a Letter", "Write to someone you trust — even if you don't send it. The act of expressing helps process.", "social"),
        CopingStrategy("Volunteer", "Helping others shifts focus outward and boosts mood. Even small acts count.", "social")
    )

    private var currentCategory = "all"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCopingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Coping Strategies"

        setupCategoryButtons()
        setupRecyclerView()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setupCategoryButtons() {
        binding.btnAll.setOnClickListener { filterCategory("all") }
        binding.btnBreathing.setOnClickListener { filterCategory("breathing") }
        binding.btnMindfulness.setOnClickListener { filterCategory("mindfulness") }
        binding.btnPhysical.setOnClickListener { filterCategory("physical") }
        binding.btnSocial.setOnClickListener { filterCategory("social") }
        filterCategory("all")
    }

    private fun filterCategory(category: String) {
        currentCategory = category
        val buttons = listOf(
            "all" to binding.btnAll,
            "breathing" to binding.btnBreathing,
            "mindfulness" to binding.btnMindfulness,
            "physical" to binding.btnPhysical,
            "social" to binding.btnSocial
        )
        buttons.forEach { (cat, btn) ->
            btn.alpha = if (cat == category) 1.0f else 0.5f
        }
        updateList()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        updateList()
    }

    private fun updateList() {
        val filtered = if (currentCategory == "all") strategies else strategies.filter { it.category == currentCategory }
        binding.recyclerView.adapter = CopingAdapter(filtered)
    }

    inner class CopingAdapter(private val items: List<CopingStrategy>) : RecyclerView.Adapter<CopingAdapter.VH>() {
        inner class VH(view: View) : RecyclerView.ViewHolder(view) {
            val title: TextView = view.findViewById(android.R.id.text1)
            val desc: TextView = view.findViewById(android.R.id.text2)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val view = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_2, parent, false)
            return VH(view)
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            val item = items[position]
            holder.title.text = item.title
            holder.desc.text = item.description
        }

        override fun getItemCount() = items.size
    }
}