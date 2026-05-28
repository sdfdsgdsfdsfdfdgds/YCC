package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CoachingDao {
    // Tables Progress
    @Query("SELECT * FROM table_progress ORDER BY tableNumber ASC")
    fun getAllTableProgress(): Flow<List<TableProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTableProgress(progress: TableProgress)

    // Word Progress
    @Query("SELECT * FROM word_progress")
    fun getAllWordProgress(): Flow<List<WordProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWordProgress(progress: WordProgress)

    // Quiz History
    @Query("SELECT * FROM quiz_history ORDER BY dateMillis DESC")
    fun getAllQuizHistory(): Flow<List<QuizHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizHistory(history: QuizHistory)

    @Query("DELETE FROM quiz_history")
    suspend fun clearQuizHistory()
}
