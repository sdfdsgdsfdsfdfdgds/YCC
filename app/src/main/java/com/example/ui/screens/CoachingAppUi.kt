package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import com.example.ui.CoachingTab
import com.example.ui.CoachingViewModel
import com.example.ui.QuizSessionState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachingAppMainScreen(
    viewModel: CoachingViewModel,
    onSpeak: (String) -> Unit
) {
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val quizState by viewModel.quizSessionState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (quizState.isQuizActive) {
            // High-fidelity Full Screen Quiz overlay
            ActiveQuizScreen(
                state = quizState,
                onSelectOption = { viewModel.selectQuizOption(it) },
                onConfirm = { viewModel.confirmQuizAnswer() },
                onNext = { viewModel.nextQuizQuestion() },
                onFinish = { viewModel.finishQuiz() },
                onCancel = { viewModel.cancelQuiz() }
            )
        } else {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Natural Green School Emblem
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF4A6741)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = "School Logo",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Yadav Coaching",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = Color(0xFF1D1B16)
                                    )
                                    Text(
                                        text = "Welcome back, Rahul",
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.sp,
                                        color = Color(0xFF4A6741)
                                    )
                                }
                                // Profile circle
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE7E2D3)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = "Student Account",
                                        tint = Color(0xFF49454F),
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFFFBF9F4)
                        )
                    )
                },
                bottomBar = {
                    NavigationBar(
                        containerColor = Color(0xFFF3F4ED),
                        windowInsets = WindowInsets.navigationBars
                    ) {
                        NavigationBarItem(
                            selected = currentTab == CoachingTab.TABLES,
                            onClick = { viewModel.selectTab(CoachingTab.TABLES) },
                            icon = { Icon(Icons.Default.TableChart, contentDescription = "Tables Tab") },
                            label = { Text("Tables") },
                            modifier = Modifier.testTag("nav_tab_tables")
                        )
                        NavigationBarItem(
                            selected = currentTab == CoachingTab.VOCABULARY,
                            onClick = { viewModel.selectTab(CoachingTab.VOCABULARY) },
                            icon = { Icon(Icons.Default.MenuBook, contentDescription = "Vocab Tab") },
                            label = { Text("Vocabulary") },
                            modifier = Modifier.testTag("nav_tab_vocab")
                        )
                        NavigationBarItem(
                            selected = currentTab == CoachingTab.QUIZ,
                            onClick = { viewModel.selectTab(CoachingTab.QUIZ) },
                            icon = { Icon(Icons.Default.EmojiObjects, contentDescription = "Quiz Tab") },
                            label = { Text("GK Quizzes") },
                            modifier = Modifier.testTag("nav_tab_quiz")
                        )
                        NavigationBarItem(
                            selected = currentTab == CoachingTab.DASHBOARD,
                            onClick = { viewModel.selectTab(CoachingTab.DASHBOARD) },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard Tab") },
                            label = { Text("Progress") },
                            modifier = Modifier.testTag("nav_tab_dashboard")
                        )
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    AnimatedContent(
                        targetState = currentTab,
                        label = "MainTabTransition",
                        transitionSpec = {
                            fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
                        }
                    ) { targetTab ->
                        when (targetTab) {
                            CoachingTab.TABLES -> TablesScreen(viewModel, onSpeak)
                            CoachingTab.VOCABULARY -> VocabularyScreen(viewModel)
                            CoachingTab.QUIZ -> QuizSelectScreen(viewModel)
                            CoachingTab.DASHBOARD -> DashboardScreen(viewModel)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 1. MATH TABLES SCREEN
// ==========================================
@Composable
fun TablesScreen(
    viewModel: CoachingViewModel,
    onSpeak: (String) -> Unit
) {
    val selectedTable by viewModel.selectedTable.collectAsStateWithLifecycle()
    val tableProgress by viewModel.tableProgressFlow.collectAsStateWithLifecycle()

    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    if (isTablet) {
        // Dual-pane layout for tablets/foldables (adaptive canvas)
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
            ) {
                TableGridPane(
                    selectedTable = selectedTable,
                    tableProgress = tableProgress,
                    onSelectTable = { viewModel.selectTable(it) }
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Card(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                TableDetailPane(
                    tableNumber = selectedTable,
                    isMastered = tableProgress.find { it.tableNumber == selectedTable }?.isMastered ?: false,
                    attempts = tableProgress.find { it.tableNumber == selectedTable }?.practiceAttempts ?: 0,
                    onToggleMastered = { viewModel.toggleTableMastered(selectedTable) },
                    onSpeak = onSpeak,
                    onStartPractice = { viewModel.startTableQuiz(selectedTable) }
                )
            }
        }
    } else {
        // Mobile scrollable container tab layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.42f),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Select multiplication table:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    TableGridPane(
                        selectedTable = selectedTable,
                        tableProgress = tableProgress,
                        onSelectTable = { viewModel.selectTable(it) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.58f),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
                )
            ) {
                TableDetailPane(
                    tableNumber = selectedTable,
                    isMastered = tableProgress.find { it.tableNumber == selectedTable }?.isMastered ?: false,
                    attempts = tableProgress.find { it.tableNumber == selectedTable }?.practiceAttempts ?: 0,
                    onToggleMastered = { viewModel.toggleTableMastered(selectedTable) },
                    onSpeak = onSpeak,
                    onStartPractice = { viewModel.startTableQuiz(selectedTable) }
                )
            }
        }
    }
}

@Composable
fun TableGridPane(
    selectedTable: Int,
    tableProgress: List<TableProgress>,
    onSelectTable: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 52.dp),
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items((1..30).toList()) { num ->
            val progress = tableProgress.find { it.tableNumber == num }
            val isMastered = progress?.isMastered ?: false
            val isSelected = selectedTable == num

            val backgroundColor = when {
                isSelected -> MaterialTheme.colorScheme.primary
                isMastered -> Color(0xFFE9EDD9) // Natural Tones Moss Green
                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            }

            val textColor = when {
                isSelected -> MaterialTheme.colorScheme.onPrimary
                isMastered -> Color(0xFF4A6741) // Natural Tones Forest Green
                else -> MaterialTheme.colorScheme.onSurface
            }

            val borderModifier = if (isMastered && !isSelected) {
                Modifier.border(1.dp, Color(0xFF4A6741), RoundedCornerShape(8.dp))
            } else if (isSelected) {
                Modifier
            } else {
                Modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(backgroundColor)
                    .then(borderModifier)
                    .clickable { onSelectTable(num) }
                    .testTag("table_select_$num")
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$num",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    if (isMastered) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Mastered",
                            modifier = Modifier.size(10.dp),
                            tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else Color(0xFF34A853)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TableDetailPane(
    tableNumber: Int,
    isMastered: Boolean,
    attempts: Int,
    onToggleMastered: () -> Unit,
    onSpeak: (String) -> Unit,
    onStartPractice: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Table Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Table of $tableNumber",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Tested $attempts times",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = {
                        val buildText = StringBuilder()
                        buildText.append("Table of $tableNumber. ")
                        for (i in 1..10) {
                            buildText.append("$tableNumber times $i is ${tableNumber * i}. ")
                        }
                        onSpeak(buildText.toString())
                    },
                    modifier = Modifier.testTag("btn_speak_table")
                ) {
                    Text("🔊", fontSize = 22.sp)
                }

                Spacer(modifier = Modifier.width(6.dp))

                Button(
                    onClick = onStartPractice,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                    modifier = Modifier.testTag("btn_practice_table")
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Test", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Checkbox row
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable { onToggleMastered() },
            color = if (isMastered) Color(0xFFE9EDD9) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isMastered) Icons.Default.CheckCircle else Icons.Default.Check,
                        contentDescription = null,
                        tint = if (isMastered) Color(0xFF4A6741) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isMastered) "Mastered & Memorized!" else "Mark as Mastered",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isMastered) Color(0xFF4A6741) else MaterialTheme.colorScheme.onSurface
                    )
                }
                Checkbox(
                    checked = isMastered,
                    onCheckedChange = { onToggleMastered() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF4A6741),
                        checkmarkColor = Color.White
                    ),
                    modifier = Modifier.testTag("chk_mastered_$tableNumber")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Scrollable table multipliers list
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items((1..10).toList()) { multiplier ->
                val result = tableNumber * multiplier
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$tableNumber",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "×",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$multiplier",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "=",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "$result",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.testTag("eq_${tableNumber}_$multiplier")
                        )
                    }
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            }
        }
    }
}

// ==========================================
// 2. VOCABULARY SCREEN
// ==========================================
@Composable
fun VocabularyScreen(viewModel: CoachingViewModel) {
    val searchQuery by viewModel.vocabSearchQuery.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.vocabFilterType.collectAsStateWithLifecycle()
    val wordProgressList by viewModel.wordProgressFlow.collectAsStateWithLifecycle()

    val filteredList = remember(searchQuery, selectedFilter, wordProgressList) {
        CoachingData.vocabularyWords.filter { word ->
            // Search match
            val matchesSearch = word.word.contains(searchQuery, ignoreCase = true) ||
                    word.definition.contains(searchQuery, ignoreCase = true)

            // Category/State match
            val dbProg = wordProgressList.find { it.word == word.word }
            val matchesFilter = when (selectedFilter) {
                "All" -> true
                "Learned" -> dbProg?.isLearned == true
                "Not Learned" -> dbProg?.isLearned != true
                "Favorites" -> dbProg?.isFavorite == true
                "Competitive" -> word.category == "Competitive"
                else -> true
            }

            matchesSearch && matchesFilter
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateVocabQuery(it) },
                placeholder = { Text("Search words or definitions...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateVocabQuery("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("vocab_search_field"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Filter Row Scrollable
            val filterOptions = listOf("All", "Learned", "Favorites", "Competitive")
            ScrollableTabRow(
                selectedTabIndex = filterOptions.indexOf(selectedFilter).coerceAtLeast(0),
                edgePadding = 0.dp,
                divider = {},
                containerColor = Color.Transparent,
                indicator = {}
            ) {
                filterOptions.forEach { filter ->
                    val isSelected = selectedFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.updateVocabFilter(filter) },
                        label = { Text(filter) },
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .testTag("vocab_filter_$filter"),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Word Count
            Text(
                text = "Showcasing ${filteredList.size} English coaching words",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
            )

            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outlineVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No vocab words match your criteria.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredList) { wordItem ->
                        val dbProg = wordProgressList.find { it.word == wordItem.word }
                        val isLearned = dbProg?.isLearned == true
                        val isFavorite = dbProg?.isFavorite == true

                        VocabWordCard(
                            wordItem = wordItem,
                            isLearned = isLearned,
                            isFavorite = isFavorite,
                            onToggleLearned = { viewModel.toggleWordLearned(wordItem.word) },
                            onToggleFavorite = { viewModel.toggleWordFavorite(wordItem.word) }
                        )
                    }
                }
            }
        }

        // Floating Action Button to launch vocabulary game
        FloatingActionButton(
            onClick = { viewModel.startVocabQuiz() },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .testTag("fab_vocab_drill"),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Practise Game", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun VocabWordCard(
    wordItem: VocabularyWord,
    isLearned: Boolean,
    isFavorite: Boolean,
    onToggleLearned: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isLearned) Color(0xFF4A6741).copy(alpha = 0.6f) else MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(14.dp)
            )
            .testTag("vocab_card_${wordItem.word}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isLearned) Color(0xFFE9EDD9).copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Title + Actions row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = wordItem.word,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = wordItem.partOfSpeech,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    if (wordItem.category == "Competitive") {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Exam High-Yield",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Row {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite word",
                            tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = onToggleLearned,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("learn_btn_${wordItem.word}")
                    ) {
                        Icon(
                            imageVector = if (isLearned) Icons.Default.CheckCircle else Icons.Default.Check,
                            contentDescription = "Mark learned",
                            tint = if (isLearned) Color(0xFF34A853) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Definition Text
            Text(
                text = wordItem.definition,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Example Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                    .padding(8.dp)
            ) {
                Column {
                    Text(
                        text = "Real sentence Example:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "\"${wordItem.example}\"",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                        style = androidx.compose.ui.text.TextStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                    )
                }
            }
        }
    }
}

// ==========================================
// 3. GENERAL KNOWLEDGE QUIZZES SELECTION LIST Screen
// ==========================================
@Composable
fun QuizSelectScreen(viewModel: CoachingViewModel) {
    val categories = listOf(
        Pair("Science & Cosmos", "Explore Physics, Chemical elements, Human Biology, and Planetary details with rich coaching questions."),
        Pair("Indian & Global History", "Revisit historical milestone records, Indian Freedom struggles, and ancient emperors."),
        Pair("World Geography", "Study rivers, global atmosphere layers, spice capitals and planetary scales."),
        Pair("General Awareness", "Test your knowledge about famous symbols, world currencies, capitals, and classic items.")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "General Knowledge Quizzes",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Choose a topic below to begin. Each practice quiz contains 10 key multiple-choice questions to boost your prep.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        items(categories) { cat ->
            val themeColor = when (cat.first) {
                "Science & Cosmos" -> Triple(Color(0xFFE8F0FE), Color(0xFF1E3A8A), "🔬")
                "Indian & Global History" -> Triple(Color(0xFFFEF3C7), Color(0xFFD97706), "📜")
                "World Geography" -> Triple(Color(0xFFE6F4EA), Color(0xFF137333), "🌍")
                else -> Triple(Color(0xFFF3E8FF), Color(0xFF7E22CE), "💡")
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val mappedTag = when (cat.first) {
                            "Science & Cosmos" -> "Science"
                            "Indian & Global History" -> "History"
                            "World Geography" -> "Geography"
                            else -> "General Awareness"
                        }
                        viewModel.startGkQuiz(mappedTag)
                    }
                    .testTag("quiz_card_${cat.first}"),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(themeColor.first),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = themeColor.third,
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = cat.first,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = cat.second,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start quiz",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

// ==========================================
// 4. PROGRESS DASHBOARD SCREEN
// ==========================================
@Composable
fun DashboardScreen(viewModel: CoachingViewModel) {
    val tableProgress by viewModel.tableProgressFlow.collectAsStateWithLifecycle()
    val wordProgress by viewModel.wordProgressFlow.collectAsStateWithLifecycle()
    val quizHistory by viewModel.quizHistoryFlow.collectAsStateWithLifecycle()

    val tablesMasteredCount = remember(tableProgress) { tableProgress.count { it.isMastered } }
    val wordsLearnedCount = remember(wordProgress) { wordProgress.count { it.isLearned } }

    val overallAverageScore = remember(quizHistory) {
        if (quizHistory.isEmpty()) 0
        else {
            val totalScore = quizHistory.sumOf { it.score }
            val totalQuestions = quizHistory.sumOf { it.totalQuestions }
            if (totalQuestions > 0) (totalScore.toFloat() / totalQuestions * 100).toInt() else 0
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Progress Dashboard card (Natural Tones Moss Theme)
        item {
            val progressPercentage = if (quizHistory.isEmpty()) 84 else overallAverageScore
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.selectTab(CoachingTab.QUIZ) },
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE9EDD9)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "STUDENT PROGRESS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                            color = Color(0xFF1D1B16)
                        )
                        Text(
                            text = "${progressPercentage}%",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF4A6741)
                        )
                        Text(
                            text = "Weekly goal: ${tablesMasteredCount + wordsLearnedCount}/20 tasks completed",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF5C6051)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .padding(2.dp)
                    ) {
                        CircularProgressRing(
                            progress = progressPercentage.toFloat() / 100f,
                            strokeWidth = 6.dp,
                            color = Color(0xFF4A6741)
                        )
                    }
                }
            }
        }

        // 2. Launcher Grid (Tables, Vocabulary, GK, Rank)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Tables card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.selectTab(CoachingTab.TABLES) },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3EFE0)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFD4A373)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Calculate,
                                    contentDescription = "Tables",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Tables 1-30",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF45413B)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Table mastery: $tablesMasteredCount/30 finished.",
                                fontSize = 11.sp,
                                color = Color(0xFF7A756D),
                                lineHeight = 14.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.White.copy(alpha = 0.5f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("12", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.White.copy(alpha = 0.5f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("13", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color.White.copy(alpha = 0.5f))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text("...", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // Vocabulary card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.selectTab(CoachingTab.VOCABULARY) },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE7E2D3)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFF4A6741)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = "Vocabulary",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Vocabulary",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF45413B)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Word of the day:\nEloquent",
                                fontSize = 11.sp,
                                color = Color(0xFF7A756D),
                                lineHeight = 14.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.4f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                val remainingWords = 20 - wordsLearnedCount
                                Text("$remainingWords New Words", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // GK Quiz Card (horizontal span-2 styled card)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectTab(CoachingTab.QUIZ) },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF7FF)),
                    border = BorderStroke(1.dp, Color(0xFFE9E1E9)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF6750A4)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Quiz,
                                contentDescription = "Quiz",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "General Knowledge",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color(0xFF1D1B20)
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFEADDFF))
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "LIVE",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF21005D)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Current Affairs, History & Geo Quizzes",
                                fontSize = 12.sp,
                                color = Color(0xFF49454F)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Forward arrow indicator",
                            tint = Color(0xFF49454F)
                        )
                    }
                }

                // Class Rank Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F3EB)),
                    border = BorderStroke(1.dp, Color(0xFFE1E3D3)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Leaderboard,
                            contentDescription = "Leaderboard",
                            tint = Color(0xFF4A6741),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "CLASS RANK",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF5C6051),
                                letterSpacing = 1.1.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "#4 out of 42 Students",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1D1B16)
                            )
                        }
                        Text(
                            text = "+2 Pos",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4A6741)
                        )
                    }
                }
            }
        }

        // Academic Badges Panel
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Earned Prep Badges",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val badgesList = listOf(
                        QuadrupleBadge("Yadav Scholar", "Overall quiz score >80%", overallAverageScore >= 80 && quizHistory.isNotEmpty(), "🎓", Color(0xFF1E3A8A)),
                        QuadrupleBadge("Table Champ", "Mastered at least 5 tables", tablesMasteredCount >= 5, "🧮", Color(0xFF137333)),
                        QuadrupleBadge("Vocab Guru", "Marked 8+ words as learned", wordsLearnedCount >= 8, "📚", Color(0xFF7E22CE)),
                        QuadrupleBadge("Mock Master", "Completed 3+ dynamic quizzes", quizHistory.size >= 3, "🥇", Color(0xFFD97706))
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        badgesList.forEach { badge ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (badge.isUnlocked) badge.color.copy(alpha = 0.08f)
                                        else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)
                                    )
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = badge.emoji,
                                    fontSize = 24.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = badge.title,
                                        fontWeight = FontWeight.Bold,
                                        color = if (badge.isUnlocked) badge.color else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = badge.requirement,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            if (badge.isUnlocked) badge.color else Color.Gray.copy(alpha = 0.3f)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (badge.isUnlocked) "UNLOCKED" else "LOCKED",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Quiz History Logs
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Recent Test Scores",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        if (quizHistory.isNotEmpty()) {
                            TextButton(
                                onClick = { viewModel.clearAllHistory() },
                                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Clear All", fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (quizHistory.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No tests completed yet. Start learning!",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    else {
                        val dateFormat = remember { SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()) }
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            quizHistory.take(6).forEach { history ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = history.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = dateFormat.format(Date(history.dateMillis)),
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .background(
                                                if (history.score >= history.totalQuestions * 0.8) Color(0xFFE6F4EA)
                                                else MaterialTheme.colorScheme.secondaryContainer
                                            )
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "${history.score} / ${history.totalQuestions}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (history.score >= history.totalQuestions * 0.8) Color(0xFF137333)
                                            else MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class QuadrupleBadge(
    val title: String,
    val requirement: String,
    val isUnlocked: Boolean,
    val emoji: String,
    val color: Color
)

@Composable
fun CircularProgressRing(
    progress: Float,
    strokeWidth: androidx.compose.ui.unit.Dp,
    color: Color
) {
    val animatedProgress = animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "DashboardProgress"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val sizeMin = size.minDimension
        val radius = (sizeMin - strokeWidth.toPx()) / 2f

        // Background circle
        drawCircle(
            color = color.copy(alpha = 0.15f),
            radius = radius,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )

        // Progress arc
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = animatedProgress.value * 360f,
            useCenter = false,
            style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        )
    }
}

// ==========================================
// FULL-SCREEN ACTIVE PRACTICE QUIZ SCREEN
// ==========================================
@Composable
fun ActiveQuizScreen(
    state: QuizSessionState,
    onSelectOption: (Int) -> Unit,
    onConfirm: () -> Unit,
    onNext: () -> Unit,
    onFinish: () -> Unit,
    onCancel: () -> Unit
) {
    val totalCount = state.questions.size
    val currentIndex = state.currentQuestionIndex

    Scaffold(
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onCancel,
                        modifier = Modifier.testTag("btn_close_quiz")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Exit Quiz")
                    }

                    Text(
                        text = state.quizTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Score: ${state.scoreCount}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 14.sp
                    )
                }

                // Smooth linear progress indicator
                if (currentIndex < totalCount) {
                    val progressRatio = (currentIndex + 1).toFloat() / totalCount.toFloat()
                    LinearProgressIndicator(
                        progress = { progressRatio },
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (currentIndex >= totalCount) {
                // QUIZ RESULT CONGRATULATORY PANEL
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val ratio = state.scoreCount.toFloat() / totalCount.toFloat()
                    val congratMessage = when {
                        ratio >= 0.9f -> "Excellent Performance! Impeccable!"
                        ratio >= 0.7f -> "Great Work Yadav Student!"
                        ratio >= 0.5f -> "Good Effort! Keep practicing!"
                        else -> "Keep Learning, Try Again!"
                    }

                    val badgeEmoji = when {
                        ratio >= 0.9f -> "👑"
                        ratio >= 0.7f -> "🌟"
                        ratio >= 0.5f -> "👍"
                        else -> "📚"
                    }

                    Text(
                        text = badgeEmoji,
                        fontSize = 72.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Text(
                        text = congratMessage,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Quiz session completed.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Large Score Banner
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 36.dp, vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Your Metric",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "${state.scoreCount} / $totalCount",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "Correct Actions",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(36.dp))

                    Button(
                        onClick = onFinish,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("btn_complete_quiz_dismiss"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Save & Go Back", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            } else {
                // ACTIVE QUESTION LAYOUT
                val activeQuestion = state.questions[currentIndex]

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Question Counter Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Question ${currentIndex + 1} of $totalCount",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Type: ${state.quizType}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Question Card text
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = activeQuestion.question,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Options List
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        activeQuestion.options.forEachIndexed { optIndex, optionText ->
                            val isSelected = state.selectedOptionIndex == optIndex
                            val isCorrectAnswer = activeQuestion.correctAnswerIndex == optIndex

                            val containerColor = when {
                                state.isAnswerConfirmed -> {
                                    when {
                                        isCorrectAnswer -> Color(0xFFE6F4EA) // correct option always highlights green
                                        isSelected && !isCorrectAnswer -> Color(0xFFFCE8E6) // selected incorrect highlights red
                                        else -> MaterialTheme.colorScheme.surface
                                    }
                                }
                                isSelected -> MaterialTheme.colorScheme.primaryContainer
                                else -> MaterialTheme.colorScheme.surface
                            }

                            val contentColor = when {
                                state.isAnswerConfirmed -> {
                                    when {
                                        isCorrectAnswer -> Color(0xFF137333) // solid green text
                                        isSelected && !isCorrectAnswer -> Color(0xFFC5221F) // solid red text
                                        else -> MaterialTheme.colorScheme.onSurface
                                    }
                                }
                                isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
                                else -> MaterialTheme.colorScheme.onSurface
                            }

                            val borderModifier = when {
                                state.isAnswerConfirmed -> {
                                    when {
                                        isCorrectAnswer -> Modifier.border(2.dp, Color(0xFF34A853), RoundedCornerShape(12.dp))
                                        isSelected && !isCorrectAnswer -> Modifier.border(2.dp, Color(0xFFEA4335), RoundedCornerShape(12.dp))
                                        else -> Modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                                    }
                                }
                                isSelected -> Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                                else -> Modifier.border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .then(borderModifier)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable(!state.isAnswerConfirmed) {
                                        onSelectOption(optIndex)
                                    }
                                    .testTag("quiz_option_$optIndex"),
                                colors = CardDefaults.cardColors(containerColor = containerColor)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Option Indicator (A, B, C, D)
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isSelected) MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.surfaceVariant
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${('A' + optIndex)}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = optionText,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = contentColor,
                                        modifier = Modifier.weight(1f)
                                    )

                                    if (state.isAnswerConfirmed) {
                                        if (isCorrectAnswer) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Correct Answer",
                                                tint = Color(0xFF34A853)
                                            )
                                        } else if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Incorrect Answer",
                                                tint = Color(0xFFEA4335)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Explanation Detail Section
                        if (state.isAnswerConfirmed) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Class Review & Discussion:",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        letterSpacing = 0.5.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = activeQuestion.explanation,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        lineHeight = 18.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Sticky Bottom Navigation Actions
                    if (!state.isAnswerConfirmed) {
                        Button(
                            onClick = onConfirm,
                            enabled = state.selectedOptionIndex != -1,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("btn_confirm_answer"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Confirm choice", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    } else {
                        Button(
                            onClick = onNext,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("btn_next_question"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            val btnLabel = if (currentIndex == totalCount - 1) "Complete Quiz" else "Next Question"
                            Text(btnLabel, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}
