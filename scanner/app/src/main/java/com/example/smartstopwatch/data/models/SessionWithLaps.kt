package com.example.smartstopwatch.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class SessionWithLaps(
    @Embedded val session: Session,
    @Relation(
        parentColumn = "id",
        entityColumn = "sessionId"
    )
    val laps: List<Lap>
)
