package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class CoachingTab {
    TABLES, VOCABULARY, QUIZ, DASHBOARD
}

data class QuizSessionState(
    val isQuizActive: Boolean = false,
    val quizType: String = "", // "GK", "VOCAB", "TABLE"
    val quizTitle: String = "",
    val questions: List<QuizQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedOptionIndex: Int = -1,
    val isAnswerConfirmed: Boolean = false,
    val scoreCount: Int = 0
)

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)

class CoachingViewModel(private val repository: CoachingRepository) : ViewModel() {

    // Current Active Tab
    private val _currentTab = MutableStateFlow(CoachingTab.TABLES)
    val currentTab: StateFlow<CoachingTab> = _currentTab.asStateFlow()

    // Database flows wrapped in StateFlows with standard 5s stop timeout for efficiency/lifecycle
    val tableProgressFlow: StateFlow<List<TableProgress>> = repository.allTableProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wordProgressFlow: StateFlow<List<WordProgress>> = repository.allWordProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quizHistoryFlow: StateFlow<List<QuizHistory>> = repository.allQuizHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Table Selector
    private val _selectedTable = MutableStateFlow(1)
    val selectedTable: StateFlow<Int> = _selectedTable.asStateFlow()

    // Vocabulary Filters
    private val _vocabSearchQuery = MutableStateFlow("")
    val vocabSearchQuery: StateFlow<String> = _vocabSearchQuery.asStateFlow()

    private val _vocabFilterType = MutableStateFlow("All") // "All", "Learned", "Unlearned", "Favorites", "Competitive"
    val vocabFilterType: StateFlow<String> = _vocabFilterType.asStateFlow()

    // Quiz Session
    private val _quizSessionState = MutableStateFlow(QuizSessionState())
    val quizSessionState: StateFlow<QuizSessionState> = _quizSessionState.asStateFlow()

    fun selectTab(tab: CoachingTab) {
        _currentTab.value = tab
    }

    fun selectTable(number: Int) {
        if (number in 1..30) {
            _selectedTable.value = number
        }
    }

    fun updateVocabQuery(query: String) {
        _vocabSearchQuery.value = query
    }

    fun updateVocabFilter(pType: String) {
        _vocabFilterType.value = pType
    }

    // Database Interactions
    fun toggleTableMastered(tableNumber: Int) {
        viewModelScope.launch {
            val progressList = tableProgressFlow.value
            val currentProgress = progressList.find { it.tableNumber == tableNumber }
            val newProgress = currentProgress?.copy(isMastered = !currentProgress.isMastered)
                ?: TableProgress(tableNumber = tableNumber, isMastered = true)
            repository.saveTableProgress(newProgress)
        }
    }

    fun toggleWordLearned(word: String) {
        viewModelScope.launch {
            val progressList = wordProgressFlow.value
            val currentProgress = progressList.find { it.word == word }
            val newProgress = currentProgress?.copy(isLearned = !currentProgress.isLearned)
                ?: WordProgress(word = word, isLearned = true)
            repository.saveWordProgress(newProgress)
        }
    }

    fun toggleWordFavorite(word: String) {
        viewModelScope.launch {
            val progressList = wordProgressFlow.value
            val currentProgress = progressList.find { it.word == word }
            val newProgress = currentProgress?.copy(isFavorite = !currentProgress.isFavorite)
                ?: WordProgress(word = word, isFavorite = true)
            repository.saveWordProgress(newProgress)
        }
    }

    // START Table quiz (from Selected Table or mixed)
    fun startTableQuiz(tableNumber: Int) {
        val questions = mutableListOf<QuizQuestion>()
        val startMultiplier = if (tableNumber == 0) 1 else tableNumber
        val isMixed = (tableNumber == 0)

        // Generate 10 math questions
        for (i in 1..10) {
            val n = if (isMixed) Random.nextInt(1, 31) else startMultiplier
            val multiplier = Random.nextInt(1, 11)
            val correctAns = n * multiplier

            // Generate wrong options
            val options = mutableSetOf(correctAns)
            while (options.size < 4) {
                val offset = Random.nextInt(-10, 11)
                val fakeOpt = correctAns + offset
                if (fakeOpt > 0 && fakeOpt != correctAns) {
                    options.add(fakeOpt)
                }
            }
            val ShuffledList = options.toList().shuffled().map { it.toString() }
            val correctIndex = ShuffledList.indexOf(correctAns.toString())

            questions.add(
                QuizQuestion(
                    question = "What is $n × $multiplier ?",
                    options = ShuffledList,
                    correctAnswerIndex = correctIndex,
                    explanation = "Mathematics Table rule: Multiplication is repeated addition. Hence, $n times $multiplier equals $correctAns."
                )
            )
        }

        _quizSessionState.value = QuizSessionState(
            isQuizActive = true,
            quizType = "TABLE",
            quizTitle = if (isMixed) "Mixed Tables Drill" else "Table of $tableNumber Speed Test",
            questions = questions,
            currentQuestionIndex = 0,
            selectedOptionIndex = -1,
            isAnswerConfirmed = false,
            scoreCount = 0
        )
    }

    // START Vocabulary quiz
    fun startVocabQuiz() {
        val words = CoachingData.vocabularyWords.shuffled()
        val quizSize = minOf(words.size, 10)
        val questions = mutableListOf<QuizQuestion>()

        for (i in 0 until quizSize) {
            val correctWord = words[i]
            val correctAns = correctWord.definition

            // Choose 3 wrong definitions from other words
            val incorrectOptions = CoachingData.vocabularyWords
                .filter { it.word != correctWord.word }
                .shuffled()
                .take(3)
                .map { it.definition }

            val optionsPool = (incorrectOptions + correctAns).shuffled()
            val correctIndex = optionsPool.indexOf(correctAns)

            questions.add(
                QuizQuestion(
                    question = "What is the definition of the word: '${correctWord.word}' (${correctWord.partOfSpeech})?",
                    options = optionsPool,
                    correctAnswerIndex = correctIndex,
                    explanation = "Definition: ${correctWord.definition}\n\nExample usage: \"${correctWord.example}\""
                )
            )
        }

        _quizSessionState.value = QuizSessionState(
            isQuizActive = true,
            quizType = "VOCAB",
            quizTitle = "English Vocabulary Drill",
            questions = questions,
            currentQuestionIndex = 0,
            selectedOptionIndex = -1,
            isAnswerConfirmed = false,
            scoreCount = 0
        )
    }

    // START GK quiz
    fun startGkQuiz(category: String) {
        val originalQuestions = if (category == "All") {
            CoachingData.gkQuestions
        } else {
            CoachingData.gkQuestions.filter { it.category == category }
        }

        val shuffledQuestions = originalQuestions.shuffled().take(10)
        val quizQuestions = shuffledQuestions.map { gk ->
            QuizQuestion(
                question = gk.question,
                options = gk.options,
                correctAnswerIndex = gk.correctAnswerIndex,
                explanation = gk.explanation
            )
        }

        _quizSessionState.value = QuizSessionState(
            isQuizActive = true,
            quizType = "GK",
            quizTitle = "GK: $category Practice",
            questions = quizQuestions,
            currentQuestionIndex = 0,
            selectedOptionIndex = -1,
            isAnswerConfirmed = false,
            scoreCount = 0
        )
    }

    fun selectQuizOption(index: Int) {
        if (!_quizSessionState.value.isAnswerConfirmed) {
            _quizSessionState.value = _quizSessionState.value.copy(selectedOptionIndex = index)
        }
    }

    fun confirmQuizAnswer() {
        val state = _quizSessionState.value
        if (state.selectedOptionIndex == -1 || state.isAnswerConfirmed) return

        val currentQuestion = state.questions[state.currentQuestionIndex]
        val isCorrect = state.selectedOptionIndex == currentQuestion.correctAnswerIndex
        val newScore = if (isCorrect) state.scoreCount + 1 else state.scoreCount

        _quizSessionState.value = state.copy(
            isAnswerConfirmed = true,
            scoreCount = newScore
        )
    }

    fun nextQuizQuestion() {
        val state = _quizSessionState.value
        val nextIndex = state.currentQuestionIndex + 1

        if (nextIndex < state.questions.size) {
            _quizSessionState.value = state.copy(
                currentQuestionIndex = nextIndex,
                selectedOptionIndex = -1,
                isAnswerConfirmed = false
            )
        } else {
            // End of quiz - Save to Room database
            viewModelScope.launch {
                val finalHistory = QuizHistory(
                    quizType = state.quizType,
                    title = state.quizTitle,
                    score = state.scoreCount,
                    totalQuestions = state.questions.size
                )
                repository.insertQuizHistory(finalHistory)

                // If Table quiz and scored perfectly (e.g. 10/10), consider auto-mastering that table if it was a single table test!
                if (state.quizType == "TABLE" && state.scoreCount == state.questions.size && state.quizTitle.startsWith("Table of")) {
                    try {
                        val numStr = state.quizTitle.replace("Table of ", "").replace(" Speed Test", "").trim()
                        val tblNum = numStr.toIntOrNull()
                        if (tblNum != null && tblNum in 1..30) {
                            val currentProgress = tableProgressFlow.value.find { it.tableNumber == tblNum }
                            val newProgress = currentProgress?.copy(
                                isMastered = true,
                                practiceAttempts = currentProgress.practiceAttempts + 1
                            ) ?: TableProgress(tableNumber = tblNum, isMastered = true, practiceAttempts = 1)
                            repository.saveTableProgress(newProgress)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else if (state.quizType == "TABLE") {
                    // Just count as an attempt
                    try {
                        val numStr = state.quizTitle.replace("Table of ", "").replace(" Speed Test", "").trim()
                        val tblNum = numStr.toIntOrNull()
                        if (tblNum != null && tblNum in 1..30) {
                            val currentProgress = tableProgressFlow.value.find { it.tableNumber == tblNum }
                            val newProgress = currentProgress?.copy(
                                practiceAttempts = currentProgress.practiceAttempts + 1
                            ) ?: TableProgress(tableNumber = tblNum, isMastered = false, practiceAttempts = 1)
                            repository.saveTableProgress(newProgress)
                        }
                    } catch (e: Exception) {
                        // ignore
                    }
                }
            }

            // Keep status but mark as complete for displaying result overlay
            _quizSessionState.value = state.copy(
                currentQuestionIndex = nextIndex // becomes equals to questions.size, triggers finish view
            )
        }
    }

    fun finishQuiz() {
        _quizSessionState.value = QuizSessionState() // Reset
    }

    fun cancelQuiz() {
        _quizSessionState.value = QuizSessionState() // Reset and exit
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}

class CoachingViewModelFactory(private val repository: CoachingRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoachingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CoachingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
