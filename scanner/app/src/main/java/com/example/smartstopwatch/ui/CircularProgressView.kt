package com.example.smartstopwatch.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.smartstopwatch.R

class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    private val rect = RectF()
    private var progress = 0f // 0 to 1

    init {
        progressPaint.color = ContextCompat.getColor(context, R.color.timer_progress)
        backgroundPaint.color = ContextCompat.getColor(context, R.color.timer_background)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val strokeWidth = width * 0.05f
        progressPaint.strokeWidth = strokeWidth
        backgroundPaint.strokeWidth = strokeWidth

        // Adjust the rectangle to account for the stroke width
        val padding = strokeWidth / 2
        rect.set(padding, padding, width - padding, height - padding)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw background circle
        canvas.drawArc(rect, 0f, 360f, false, backgroundPaint)

        // Draw progress arc
        val sweepAngle = progress * 360f
        canvas.drawArc(rect, -90f, sweepAngle, false, progressPaint)
    }

    fun setProgress(value: Float) {
        progress = value.coerceIn(0f, 1f)
        invalidate()
    }
}
