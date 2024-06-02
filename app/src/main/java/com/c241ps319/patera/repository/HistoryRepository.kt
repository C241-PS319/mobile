package com.c241ps319.patera.repository

import android.app.Application
import com.c241ps319.patera.data.local.HistoryEntity
import com.c241ps319.patera.data.local.PateraDatabase

class HistoryRepository(application: Application) {
    private val historyDao = PateraDatabase.getDatabase(application).historyDao()

    suspend fun addHistory(historyEntity: HistoryEntity) {
        historyDao.addHistory(historyEntity)
    }

    fun getHistory(): List<HistoryEntity> = historyDao.getHistory()
}