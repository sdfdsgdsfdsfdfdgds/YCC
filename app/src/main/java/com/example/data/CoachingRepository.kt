package com.example.data

import kotlinx.coroutines.flow.Flow

class CoachingRepository(private val coachingDao: CoachingDao) {
    val allTableProgress: Flow<List<TableProgress>> = coachingDao.getAllTableProgress()
    val allWordProgress: Flow<List<WordProgress>> = coachingDao.getAllWordProgress()
    val allQuizHistory: Flow<List<QuizHistory>> = coachingDao.getAllQuizHistory()

    suspend fun saveTableProgress(progress: TableProgress) {
        coachingDao.insertTableProgress(progress)
    }

    suspend fun saveWordProgress(progress: WordProgress) {
        coachingDao.insertWordProgress(progress)
    }

    suspend fun insertQuizHistory(history: QuizHistory) {
        coachingDao.insertQuizHistory(history)
    }

    suspend fun clearHistory() {
        coachingDao.clearQuizHistory()
    }
}
