package com.example.smartstopwatch.ui.viewmodels

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smartstopwatch.data.db.StopwatchDatabase
import com.example.smartstopwatch.data.models.Lap
import com.example.smartstopwatch.data.models.SessionWithLaps
import com.example.smartstopwatch.data.repository.StopwatchRepository
import kotlinx.coroutines.launch
import java.util.Date

class StopwatchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StopwatchRepository
    private val handler = Handler(Looper.getMainLooper())
    private var startTime = 0L
    private var elapsedTime = 0L
    private var lastLapTime = 0L
    private var isRunning = false
    private var lapCount = 0

    private val _currentTimeMillis = MutableLiveData<Long>()
    val currentTimeMillis: LiveData<Long> = _currentTimeMillis

    private val _laps = MutableLiveData<List<Lap>>()
    val laps: LiveData<List<Lap>> = _laps

    private val _currentSessionId = MutableLiveData<Long?>()
    val currentSessionId: LiveData<Long?> = _currentSessionId

    private val _progress = MutableLiveData<Float>()
    val progress: LiveData<Float> = _progress

    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                val now = SystemClock.elapsedRealtime()
                elapsedTime = now - startTime
                _currentTimeMillis.value = elapsedTime
                
                // Update progress (cycles every minute)
                val progressValue = (elapsedTime % 60000) / 60000f
                _progress.value = progressValue
                
                handler.postDelayed(this, 10) // Update every 10ms for millisecond precision
            }
        }
    }

    init {
        val database = StopwatchDatabase.getDatabase(application)
        repository = StopwatchRepository(database.sessionDao(), database.lapDao())
        _laps.value = emptyList()
        _progress.value = 0f
    }

    fun startStopwatch() {
        if (!isRunning) {
            if (elapsedTime == 0L) {
                // New session
                startTime = SystemClock.elapsedRealtime()
                lastLapTime = startTime
                lapCount = 0
                viewModelScope.launch {
                    val sessionId = repository.insertSession(
                        startTime = Date(),
                        endTime = Date(), // Will update when stopped
                        duration = 0 // Will update when stopped
                    )
                    _currentSessionId.value = sessionId
                }
            } else {
                // Resume
                startTime = SystemClock.elapsedRealtime() - elapsedTime
            }
            isRunning = true
            handler.post(updateTimeRunnable)
        }
    }

    fun pauseStopwatch() {
        if (isRunning) {
            isRunning = false
            handler.removeCallbacks(updateTimeRunnable)
        }
    }

    fun resetStopwatch() {
        isRunning = false
        handler.removeCallbacks(updateTimeRunnable)
        elapsedTime = 0L
        _currentTimeMillis.value = 0L
        _progress.value = 0f
        lapCount = 0
        _laps.value = emptyList()
        _currentSessionId.value = null
    }

    fun recordLap() {
        if (isRunning) {
            val currentTime = SystemClock.elapsedRealtime()
            val lapTime = currentTime - lastLapTime
            lastLapTime = currentTime
            lapCount++

            val lap = Lap(
                sessionId = _currentSessionId.value ?: 0,
                lapNumber = lapCount,
                lapTime = lapTime,
                totalTime = elapsedTime
            )

            val currentLaps = _laps.value?.toMutableList() ?: mutableListOf()
            currentLaps.add(0, lap) // Add to the beginning of the list
            _laps.value = currentLaps

            viewModelScope.launch {
                repository.insertLap(
                    sessionId = _currentSessionId.value ?: 0,
                    lapNumber = lapCount,
                    lapTime = lapTime,
                    totalTime = elapsedTime
                )
            }
        }
    }

    fun saveSession() {
        _currentSessionId.value?.let { sessionId ->
            viewModelScope.launch {
                repository.insertSession(
                    startTime = Date(System.currentTimeMillis() - elapsedTime),
                    endTime = Date(),
                    duration = elapsedTime
                )
            }
        }
    }

    suspend fun getSessionWithLaps(sessionId: Long): SessionWithLaps {
        return repository.getSessionWithLaps(sessionId)
    }
}
