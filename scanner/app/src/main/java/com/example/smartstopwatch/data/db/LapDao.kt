package com.example.smartstopwatch.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.smartstopwatch.data.models.Lap

@Dao
interface LapDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLap(lap: Lap): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLaps(laps: List<Lap>)

    @Query("SELECT * FROM Lap WHERE sessionId = :sessionId ORDER BY lapNumber")
    fun getLapsForSession(sessionId: Long): LiveData<List<Lap>>

    @Query("SELECT * FROM Lap WHERE sessionId = :sessionId ORDER BY lapNumber")
    suspend fun getLapsForSessionSync(sessionId: Long): List<Lap>

    @Query("DELETE FROM Lap WHERE sessionId = :sessionId")
    suspend fun deleteLapsForSession(sessionId: Long)
}
