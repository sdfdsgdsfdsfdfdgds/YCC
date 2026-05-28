package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_progress")
data class TableProgress(
    @PrimaryKey val tableNumber: Int,
    val isMastered: Boolean = false,
    val practiceAttempts: Int = 0,
    val bestTimeSeconds: Int = 999
)

@Entity(tableName = "word_progress")
data class WordProgress(
    @PrimaryKey val word: String,
    val isLearned: Boolean = false,
    val isFavorite: Boolean = false
)

@Entity(tableName = "quiz_history")
data class QuizHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val quizType: String, // "GK", "VOCAB", "TABLE"
    val title: String,
    val score: Int,
    val totalQuestions: Int,
    val dateMillis: Long = System.currentTimeMillis()
)
