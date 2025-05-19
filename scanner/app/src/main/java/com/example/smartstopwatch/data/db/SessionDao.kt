package com.example.smartstopwatch.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.smartstopwatch.data.models.Session
import com.example.smartstopwatch.data.models.SessionWithLaps

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: Session): Long

    @Query("SELECT * FROM Session ORDER BY startTime DESC")
    fun getAllSessions(): LiveData<List<Session>>

    @Transaction
    @Query("SELECT * FROM Session WHERE id = :sessionId")
    suspend fun getSessionWithLaps(sessionId: Long): SessionWithLaps

    @Transaction
    @Query("SELECT * FROM Session ORDER BY startTime DESC")
    fun getAllSessionsWithLaps(): LiveData<List<SessionWithLaps>>

    @Query("DELETE FROM Session WHERE id = :sessionId")
    suspend fun deleteSession(sessionId: Long)
}
