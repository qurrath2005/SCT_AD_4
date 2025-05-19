package com.example.smartstopwatch.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object TimeFormatter {
    
    fun formatElapsedTime(timeInMillis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(timeInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis) % 60
        val millis = timeInMillis % 1000

        return String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d.%03d",
            hours,
            minutes,
            seconds,
            millis
        )
    }

    fun formatElapsedTimeShort(timeInMillis: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis) % 60
        val millis = timeInMillis % 1000

        return String.format(
            Locale.getDefault(),
            "%02d:%02d.%03d",
            minutes,
            seconds,
            millis
        )
    }

    fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.getDefault())
        return dateFormat.format(date)
    }
}
