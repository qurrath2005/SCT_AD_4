package com.example.smartstopwatch.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.smartstopwatch.data.db.StopwatchDatabase
import com.example.smartstopwatch.data.models.SessionWithLaps
import com.example.smartstopwatch.data.repository.StopwatchRepository
import kotlinx.coroutines.launch

class SessionHistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StopwatchRepository
    val allSessionsWithLaps: LiveData<List<SessionWithLaps>>

    init {
        val database = StopwatchDatabase.getDatabase(application)
        repository = StopwatchRepository(database.sessionDao(), database.lapDao())
        allSessionsWithLaps = repository.allSessionsWithLaps
    }

    suspend fun getSessionWithLaps(sessionId: Long): SessionWithLaps {
        return repository.getSessionWithLaps(sessionId)
    }

    fun deleteSession(sessionId: Long) {
        viewModelScope.launch {
            repository.deleteSession(sessionId)
        }
    }
}
