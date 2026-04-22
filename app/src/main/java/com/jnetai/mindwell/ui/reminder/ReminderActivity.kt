package com.jnetai.mindwell.ui.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jnetai.mindwell.databinding.ActivityReminderBinding
import com.jnetai.mindwell.reminder.ReminderReceiver
import java.util.Calendar

class ReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReminderBinding
    private var selectedHour = 21
    private var selectedMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Daily Reminders"

        binding.timePicker.setIs24HourView(false)
        binding.timePicker.hour = selectedHour
        binding.timePicker.minute = selectedMinute

        loadReminderSettings()

        binding.btnSaveReminder.setOnClickListener { saveReminder() }
        binding.btnDisableReminder.setOnClickListener { doDisableReminder() }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun loadReminderSettings() {
        val prefs = getSharedPreferences("mindwell_prefs", Context.MODE_PRIVATE)
        val enabled = prefs.getBoolean("reminder_enabled", false)
        val hour = prefs.getInt("reminder_hour", 21)
        val minute = prefs.getInt("reminder_minute", 0)

        binding.timePicker.hour = hour
        binding.timePicker.minute = minute
        binding.switchReminder.isChecked = enabled
    }

    private fun saveReminder() {
        selectedHour = binding.timePicker.hour
        selectedMinute = binding.timePicker.minute
        val enabled = binding.switchReminder.isChecked

        val prefs = getSharedPreferences("mindwell_prefs", Context.MODE_PRIVATE)
        prefs.edit()
            .putBoolean("reminder_enabled", enabled)
            .putInt("reminder_hour", selectedHour)
            .putInt("reminder_minute", selectedMinute)
            .apply()

        if (enabled) {
            setReminder(selectedHour, selectedMinute)
            Toast.makeText(this, "Reminder set for ${formatTime(selectedHour, selectedMinute)}", Toast.LENGTH_SHORT).show()
        } else {
            cancelReminder()
            Toast.makeText(this, "Reminder disabled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setReminder(hour: Int, minute: Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun cancelReminder() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent?.let { alarmManager.cancel(it) }
    }

    private fun doDisableReminder() {
        cancelReminder()
        val prefs = getSharedPreferences("mindwell_prefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("reminder_enabled", false).apply()
        binding.switchReminder.isChecked = false
        Toast.makeText(this, "Reminder disabled", Toast.LENGTH_SHORT).show()
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val ampm = if (hour >= 12) "PM" else "AM"
        val h = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
        return "$h:${String.format("%02d", minute)} $ampm"
    }
}