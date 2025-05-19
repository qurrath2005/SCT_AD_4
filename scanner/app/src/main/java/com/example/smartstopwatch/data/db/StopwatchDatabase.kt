package com.example.smartstopwatch.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.smartstopwatch.data.models.Lap
import com.example.smartstopwatch.data.models.Session

@Database(entities = [Session::class, Lap::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class StopwatchDatabase : RoomDatabase() {

    abstract fun sessionDao(): SessionDao
    abstract fun lapDao(): LapDao

    companion object {
        @Volatile
        private var INSTANCE: StopwatchDatabase? = null

        fun getDatabase(context: Context): StopwatchDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StopwatchDatabase::class.java,
                    "stopwatch_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
