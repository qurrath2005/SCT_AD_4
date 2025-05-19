package com.example.smartstopwatch.utils

import android.content.Context
import android.os.Environment
import com.example.smartstopwatch.data.models.SessionWithLaps
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Locale

object CsvExporter {

    fun exportSessionToCsv(context: Context, sessionWithLaps: SessionWithLaps): File? {
        try {
            val session = sessionWithLaps.session
            val laps = sessionWithLaps.laps
            
            val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            val fileName = "Stopwatch_Session_${dateFormat.format(session.startTime)}.csv"
            
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (!downloadsDir.exists()) {
                downloadsDir.mkdirs()
            }
            
            val file = File(downloadsDir, fileName)
            val writer = CSVWriter(FileWriter(file))
            
            // Write session info
            writer.writeNext(arrayOf("Session Information"))
            writer.writeNext(arrayOf("Start Time", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(session.startTime)))
            writer.writeNext(arrayOf("End Time", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(session.endTime)))
            writer.writeNext(arrayOf("Total Duration", TimeFormatter.formatElapsedTime(session.duration)))
            writer.writeNext(arrayOf("Total Laps", laps.size.toString()))
            writer.writeNext(arrayOf()) // Empty line
            
            // Write lap info
            writer.writeNext(arrayOf("Lap Number", "Lap Time", "Total Time"))
            laps.forEach { lap ->
                writer.writeNext(arrayOf(
                    lap.lapNumber.toString(),
                    TimeFormatter.formatElapsedTime(lap.lapTime),
                    TimeFormatter.formatElapsedTime(lap.totalTime)
                ))
            }
            
            writer.close()
            return file
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
