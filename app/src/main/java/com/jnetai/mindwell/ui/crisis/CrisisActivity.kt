package com.jnetai.mindwell.ui.crisis

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jnetai.mindwell.databinding.ActivityCrisisBinding

class CrisisActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrisisBinding

    data class CrisisResource(val name: String, val number: String, val description: String, val url: String? = null)

    private val resources = listOf(
        CrisisResource("988 Suicide & Crisis Lifeline", "988", "24/7 support for people in distress. Call or text 988.", "tel:988"),
        CrisisResource("Crisis Text Line", "741741", "Text HOME to 741741 for free crisis counseling.", "sms:741741"),
        CrisisResource("SAMHSA Helpline", "1-800-662-4357", "Free referral and information for substance abuse and mental health.", "tel:18006624357"),
        CrisisResource("National Domestic Violence", "1-800-799-7233", "24/7 confidential support for domestic violence.", "tel:18007997233"),
        CrisisResource("Trevor Project (LGBTQ+)", "1-866-488-7386", "Crisis intervention and suicide prevention for LGBTQ+ youth.", "tel:18664887386"),
        CrisisResource("Veterans Crisis Line", "988 (Press 1)", "Support for veterans in crisis. Call 988 then press 1, or text 838255.", "tel:988"),
        CrisisResource("NAMI Helpline", "1-800-950-6264", "Information, support, and referrals for mental health conditions.", "tel:18009506264"),
        CrisisResource("Childhelp National Abuse Hotline", "1-800-422-4453", "For child abuse prevention and treatment.", "tel:18004224453")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrisisBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Crisis Resources"

        populateResources()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            this.getResources().displayMetrics
        ).toInt()
    }

    private fun populateResources() {
        for (resource in resources) {
            val card = com.google.android.material.card.MaterialCardView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 0, 0, dpToPx(16)) }
                radius = dpToPx(12).toFloat()
                setCardBackgroundColor(getColor(com.jnetai.mindwell.R.color.crisis_card_bg))
                cardElevation = dpToPx(2).toFloat()
                strokeColor = getColor(com.jnetai.mindwell.R.color.crisis_stroke)
                strokeWidth = dpToPx(1)
                isClickable = true
                isFocusable = true
                setOnClickListener {
                    resource.url?.let {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                    }
                }
            }

            val innerLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                val pad = dpToPx(16)
                setPadding(pad, pad, pad, pad)
            }

            val nameView = TextView(this).apply {
                text = resource.name
                setTextColor(getColor(com.jnetai.mindwell.R.color.crisis_name))
                textSize = 16f
                setTypeface(typeface, android.graphics.Typeface.BOLD)
            }

            val numberView = TextView(this).apply {
                text = "📞 ${resource.number}"
                setTextColor(getColor(com.jnetai.mindwell.R.color.crisis_number))
                textSize = 18f
                setTypeface(typeface, android.graphics.Typeface.BOLD)
            }

            val descView = TextView(this).apply {
                text = resource.description
                setTextColor(getColor(com.jnetai.mindwell.R.color.crisis_desc))
                textSize = 14f
            }

            innerLayout.addView(nameView)
            innerLayout.addView(numberView)
            innerLayout.addView(descView)
            card.addView(innerLayout)
            binding.resourcesContainer.addView(card)
        }
    }
}