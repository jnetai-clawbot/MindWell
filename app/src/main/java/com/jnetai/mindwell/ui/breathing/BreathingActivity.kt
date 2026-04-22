package com.jnetai.mindwell.ui.breathing

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jnetai.mindwell.databinding.ActivityBreathingBinding
import java.util.Timer
import java.util.TimerTask

class BreathingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBreathingBinding
    private var isRunning = false
    private var timer: Timer? = null
    private var phaseSecondsLeft = 0
    private var totalSecondsLeft = 0
    private var currentPhaseIndex = 0
    private var currentCycle = 0
    private val handler = Handler(Looper.getMainLooper())

    // 4-7-8 pattern
    private val pattern478 = listOf(
        BreathingPhase("Breathe In", 4),
        BreathingPhase("Hold", 7),
        BreathingPhase("Breathe Out", 8)
    )

    // Box breathing: 4-4-4-4
    private val patternBox = listOf(
        BreathingPhase("Breathe In", 4),
        BreathingPhase("Hold", 4),
        BreathingPhase("Breathe Out", 4),
        BreathingPhase("Hold", 4)
    )

    private var currentPattern = pattern478
    private val maxCycles = 4

    data class BreathingPhase(val name: String, val duration: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreathingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Breathing Exercises"

        binding.btnPattern478.setOnClickListener { selectPattern(pattern478, binding.btnPattern478) }
        binding.btnPatternBox.setOnClickListener { selectPattern(patternBox, binding.btnPatternBox) }
        binding.btnStartStop.setOnClickListener { toggleBreathing() }

        selectPattern(pattern478, binding.btnPattern478)
        updateDisplay()
    }

    override fun onSupportNavigateUp(): Boolean {
        stopBreathing()
        finish()
        return true
    }

    private fun selectPattern(pattern: List<BreathingPhase>, selectedBtn: Button) {
        if (isRunning) return
        currentPattern = pattern
        binding.btnPattern478.alpha = 0.5f
        binding.btnPatternBox.alpha = 0.5f
        selectedBtn.alpha = 1.0f
        resetState()
        updateDisplay()
    }

    private fun resetState() {
        currentPhaseIndex = 0
        currentCycle = 0
        phaseSecondsLeft = currentPattern[0].duration
        totalSecondsLeft = currentPattern.sumOf { it.duration } * maxCycles
    }

    private fun toggleBreathing() {
        if (isRunning) {
            stopBreathing()
        } else {
            startBreathing()
        }
    }

    private fun startBreathing() {
        isRunning = true
        binding.btnStartStop.text = "Stop"
        resetState()
        animateCircle()

        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    tick()
                }
            }
        }, 1000, 1000)
    }

    private fun stopBreathing() {
        isRunning = false
        binding.btnStartStop.text = "Start"
        timer?.cancel()
        timer = null
        binding.tvPhase.text = "Ready"
        binding.tvTimer.text = ""
        binding.tvCycle.text = ""
        binding.breathingCircle.animate().scaleX(1f).scaleY(1f).setDuration(300).start()
    }

    private fun tick() {
        if (!isRunning) return

        totalSecondsLeft--
        phaseSecondsLeft--

        if (phaseSecondsLeft <= 0) {
            currentPhaseIndex++
            if (currentPhaseIndex >= currentPattern.size) {
                currentPhaseIndex = 0
                currentCycle++
                if (currentCycle >= maxCycles) {
                    stopBreathing()
                    binding.tvPhase.text = "Complete! 🎉"
                    return
                }
            }
            phaseSecondsLeft = currentPattern[currentPhaseIndex].duration
        }

        updateDisplay()
        animateCircle()
    }

    private fun updateDisplay() {
        val phase = currentPattern.getOrElse(currentPhaseIndex) { currentPattern[0] }
        binding.tvPhase.text = phase.name
        binding.tvTimer.text = phaseSecondsLeft.toString()
        binding.tvCycle.text = "Cycle ${currentCycle + 1} of $maxCycles"
    }

    private fun animateCircle() {
        val phase = currentPattern.getOrElse(currentPhaseIndex) { currentPattern[0] }
        val scale = when (phase.name) {
            "Breathe In" -> 1.4f
            "Hold" -> 1.2f
            "Breathe Out" -> 0.8f
            else -> 1.0f
        }

        binding.breathingCircle.animate()
            .scaleX(scale)
            .scaleY(scale)
            .setDuration(800)
            .start()
    }
}