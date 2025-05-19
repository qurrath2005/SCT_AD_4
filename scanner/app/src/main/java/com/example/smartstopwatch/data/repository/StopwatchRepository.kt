package com.example.smartstopwatch.data.repository

import androidx.lifecycle.LiveData
import com.example.smartstopwatch.data.db.LapDao
import com.example.smartstopwatch.data.db.SessionDao
import com.example.smartstopwatch.data.models.Lap
import com.example.smartstopwatch.data.models.Session
import com.example.smartstopwatch.data.models.SessionWithLaps
import java.util.Date

class StopwatchRepository(
    private val sessionDao: SessionDao,
    private val lapDao: LapDao
) {
    val allSessions: LiveData<List<Session>> = sessionDao.getAllSessions()
    val allSessionsWithLaps: LiveData<List<SessionWithLaps>> = sessionDao.getAllSessionsWithLaps()

    suspend fun insertSession(startTime: Date, endTime: Date, duration: Long): Long {
        val session = Session(
            startTime = startTime,
            endTime = endTime,
            duration = duration
        )
        return sessionDao.insertSession(session)
    }

    suspend fun insertLap(sessionId: Long, lapNumber: Int, lapTime: Long, totalTime: Long): Long {
        val lap = Lap(
            sessionId = sessionId,
            lapNumber = lapNumber,
            lapTime = lapTime,
            totalTime = totalTime
        )
        return lapDao.insertLap(lap)
    }

    suspend fun insertLaps(laps: List<Lap>) {
        lapDao.insertLaps(laps)
    }

    fun getLapsForSession(sessionId: Long): LiveData<List<Lap>> {
        return lapDao.getLapsForSession(sessionId)
    }

    suspend fun getSessionWithLaps(sessionId: Long): SessionWithLaps {
        return sessionDao.getSessionWithLaps(sessionId)
    }

    suspend fun deleteSession(sessionId: Long) {
        sessionDao.deleteSession(sessionId)
    }
}
