package com.jnetai.mindwell.ui.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MoodBarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    data class MoodDay(val dayLabel: String, val mood: Int?, val date: LocalDate)

    var moodData: List<MoodDay> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val emptyPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val moodColors = mapOf(
        1 to Color.parseColor("#EF5350"),  // Red
        2 to Color.parseColor("#FF9800"),  // Orange
        3 to Color.parseColor("#FFC107"),  // Yellow
        4 to Color.parseColor("#66BB6A"),  // Green
        5 to Color.parseColor("#42A5F5")   // Blue
    )

    private val moodEmojis = mapOf(
        1 to "😢",
        2 to "😟",
        3 to "😐",
        4 to "😊",
        5 to "😄"
    )

    init {
        emptyPaint.color = Color.parseColor("#333333")
        emptyPaint.style = Paint.Style.STROKE
        emptyPaint.strokeWidth = 2f

        textPaint.color = Color.WHITE
        textPaint.textSize = 36f
        textPaint.textAlign = Paint.Align.CENTER

        gridPaint.color = Color.parseColor("#444444")
        gridPaint.strokeWidth = 1f

        labelPaint.color = Color.parseColor("#AAAAAA")
        labelPaint.textSize = 28f
        labelPaint.textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val padding = 60f
        val bottomPadding = 80f
        val topPadding = 60f

        val chartWidth = width - padding * 2
        val chartHeight = height - topPadding - bottomPadding

        // Draw horizontal grid lines (mood levels 1-5)
        for (i in 1..5) {
            val y = topPadding + chartHeight - (i.toFloat() / 5f) * chartHeight
            canvas.drawLine(padding, y, width - padding, y, gridPaint)
            canvas.drawText(i.toString(), padding / 2, y + 10f, labelPaint)
        }

        if (moodData.isEmpty()) return

        val barWidth = chartWidth / moodData.size * 0.6f
        val spacing = chartWidth / moodData.size

        moodData.forEachIndexed { index, moodDay ->
            val x = padding + spacing * index + spacing / 2f - barWidth / 2f

            // Day label at bottom
            canvas.drawText(moodDay.dayLabel, x + barWidth / 2f, height - bottomPadding / 2f, labelPaint)

            if (moodDay.mood != null) {
                val barHeight = (moodDay.mood.toFloat() / 5f) * chartHeight
                val top = topPadding + chartHeight - barHeight

                barPaint.color = moodColors[moodDay.mood] ?: Color.GRAY
                barPaint.style = Paint.Style.FILL

                val rect = RectF(x, top, x + barWidth, topPadding + chartHeight)
                canvas.drawRoundRect(rect, 8f, 8f, barPaint)

                // Mood emoji on top of bar
                val emojiY = top - 20f
                canvas.drawText(moodEmojis[moodDay.mood] ?: "", x + barWidth / 2f, emojiY, textPaint)
            } else {
                // Empty bar outline
                val rect = RectF(x, topPadding, x + barWidth, topPadding + chartHeight)
                canvas.drawRoundRect(rect, 8f, 8f, emptyPaint)
            }
        }
    }
}