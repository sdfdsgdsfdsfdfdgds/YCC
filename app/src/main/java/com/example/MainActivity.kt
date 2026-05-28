package com.example

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.data.AppDatabase
import com.example.data.CoachingRepository
import com.example.ui.CoachingViewModel
import com.example.ui.CoachingViewModelFactory
import com.example.ui.screens.CoachingAppMainScreen
import com.example.ui.theme.MyApplicationTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    private var textToSpeech: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize TextToSpeech engine
        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.US
            }
        }

        // Setup Room Database & Repository
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = CoachingRepository(database.coachingDao())
        val viewModelFactory = CoachingViewModelFactory(repository)

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: CoachingViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = viewModelFactory
                )
                CoachingAppMainScreen(
                    viewModel = viewModel,
                    onSpeak = { text ->
                        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        super.onDestroy()
    }
}
