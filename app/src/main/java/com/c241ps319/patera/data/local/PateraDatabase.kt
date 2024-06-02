package com.c241ps319.patera.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 1)
abstract class PateraDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    companion object{
        @Volatile
        private var INSTANCE: PateraDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context):PateraDatabase{
            if (INSTANCE == null){
                synchronized(PateraDatabase::class.java){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        PateraDatabase::class.java, "patera_database")
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE as PateraDatabase
        }
    }
}