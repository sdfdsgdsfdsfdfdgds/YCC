package com.example.data

data class VocabularyWord(
    val word: String,
    val definition: String,
    val partOfSpeech: String,
    val example: String,
    val category: String
)

data class GKQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String,
    val category: String
)

object CoachingData {
    val vocabularyWords = listOf(
        VocabularyWord("Acumen", "The ability to make good judgments and quick decisions.", "Noun", "Her business acumen helped the coaching center grow.", "Competitive"),
        VocabularyWord("Benevolent", "Well meaning and kindly; generous.", "Adjective", "The benevolent teacher offered free evening tutoring classes.", "General"),
        VocabularyWord("Candid", "Truthful and straightforward; frank.", "Adjective", "The coach gave a candid assessment of the student's preparation.", "General"),
        VocabularyWord("Diligent", "Having or showing care and conscientiousness in one's work.", "Adjective", "A diligent student is bound to clear competitive exams.", "Competitive"),
        VocabularyWord("Eloquence", "Fluent or persuasive speaking or writing.", "Noun", "The English teacher's eloquence made the lectures spellbinding.", "Competitive"),
        VocabularyWord("Fortitude", "Courage in pain or adversity.", "Noun", "Studying for 10 hours straight requires great mental fortitude.", "General"),
        VocabularyWord("Gregarious", "Fond of company; highly sociable.", "Adjective", "He is a gregarious classmate who helps everyone prepare.", "General"),
        VocabularyWord("Impeccable", "In accordance with the highest standards; faultless.", "Adjective", "His answers in the vocabulary test are impeccable.", "Competitive"),
        VocabularyWord("Jubilant", "Feeling or expressing great happiness and triumph.", "Adjective", "The class was jubilant after the exam results were announced.", "General"),
        VocabularyWord("Meticulous", "Showing great attention to detail; very careful and precise.", "Adjective", "She kept meticulous notes for general knowledge quizzes.", "Competitive"),
        VocabularyWord("Nostalgia", "A sentimental longing or wistful affection for the past.", "Noun", "Reviewing old school notes filled him with nostalgia.", "General"),
        VocabularyWord("Pragmatic", "Dealing with things sensibly and realistically based on practical context.", "Adjective", "Solving past papers is a pragmatic way to study.", "Competitive"),
        VocabularyWord("Resilient", "Able to withstand or recover quickly from difficult conditions.", "Adjective", "Resilient students learn from their quiz mistakes and improve.", "General"),
        VocabularyWord("Scrupulous", "Thorough, extremely attentive to details, or concerned with avoiding wrong.", "Adjective", "The teacher prepared the quiz answer key with scrupulous care.", "Competitive"),
        VocabularyWord("Unanimous", "Fully in agreement; shared by everyone in a group.", "Adjective", "The student batch was unanimous in praise of the class app.", "General"),
        VocabularyWord("Venerable", "Accorded a great deal of respect, especially because of wisdom or character.", "Adjective", "The venerable educator distributed gold medals to top rankers.", "General"),
        VocabularyWord("Wary", "Feeling or showing caution about possible dangers or problems.", "Adjective", "Be wary of negative marking in the practice exam segment.", "General"),
        VocabularyWord("Zeal", "Great energy or enthusiasm in pursuit of a cause or objective.", "Noun", "Her teaching zeal inspired the entire coaching batch.", "Competitive"),
        VocabularyWord("Pinnacle", "The most successful point; the culmination of achievement.", "Noun", "Securing a top rank is the pinnacle of academic success.", "Competitive"),
        VocabularyWord("Tenacious", "Tending to keep a firm hold of something; cohesive and persistent.", "Adjective", "A tenacious approach to solving complex mock equations yields results.", "Competitive")
    )

    val gkQuestions = listOf(
        GKQuestion(
            id = 1,
            question = "Which planet in our solar system is known as the 'Red Planet'?",
            options = listOf("Venus", "Mars", "Jupiter", "Saturn"),
            correctAnswerIndex = 1,
            explanation = "Mars is known as the Red Planet because iron minerals in its soil oxidize (rust), causing the soil and atmosphere to look red.",
            category = "Science"
        ),
        GKQuestion(
            id = 2,
            question = "Who is widely revered as the 'Father of the Indian Constitution'?",
            options = listOf("Mahatma Gandhi", "Dr. B.R. Ambedkar", "Jawaharlal Nehru", "Dr. Rajendra Prasad"),
            correctAnswerIndex = 1,
            explanation = "Dr. B.R. Ambedkar was the Chairman of the Drafting Committee, responsible for framing the Constitution of India.",
            category = "History"
        ),
        GKQuestion(
            id = 3,
            question = "The power of a lens is measured in which of the following units?",
            options = listOf("Dioptres", "Lumen", "Watt", "Candela"),
            correctAnswerIndex = 0,
            explanation = "The power of a optical lens is measured in Dioptres (represented by D), which is the reciprocal of the focal length in meters.",
            category = "Science"
        ),
        GKQuestion(
            id = 4,
            question = "Which of the following is acknowledged as the longest river in the world?",
            options = listOf("Amazon River", "Nile River", "Yangtze River", "Mississippi River"),
            correctAnswerIndex = 1,
            explanation = "The Nile River is generally accepted as the longest river in the world, stretching over 6,650 kilometers.",
            category = "Geography"
        ),
        GKQuestion(
            id = 5,
            question = "Who penned India's national anthem, 'Jana Gana Mana'?",
            options = listOf("Bankim Chandra Chatterjee", "Rabindranath Tagore", "Subhash Chandra Bose", "Sarojini Naidu"),
            correctAnswerIndex = 1,
            explanation = "Rabindranath Tagore composed the National Anthem 'Jana Gana Mana' originally in Bengali, adopted in its Hindi version in 1950.",
            category = "History"
        ),
        GKQuestion(
            id = 6,
            question = "Which classical dance style originates from the state of Tamil Nadu?",
            options = listOf("Kathak", "Bharatanatyam", "Kathakali", "Kuchipudi"),
            correctAnswerIndex = 1,
            explanation = "Bharatanatyam is a major genre of Indian classical dance that originated historically in the temples of Tamil Nadu.",
            category = "General Awareness"
        ),
        GKQuestion(
            id = 7,
            question = "Which human body organ secretes the hormone insulin?",
            options = listOf("Liver", "Pancreas", "Gallbladder", "Spleen"),
            correctAnswerIndex = 1,
            explanation = "The pancreas is an organ in the abdomen that produces chemical secretions, most notably insulin, to regulate blood glucose levels.",
            category = "Science"
        ),
        GKQuestion(
            id = 8,
            question = "Which gas is the most abundant in Earth's atmosphere?",
            options = listOf("Oxygen", "Nitrogen", "Carbon Dioxide", "Argon"),
            correctAnswerIndex = 1,
            explanation = "Nitrogen makes up approximately 78% of the Earth's atmosphere, followed by Oxygen at about 21%.",
            category = "Geography"
        ),
        GKQuestion(
            id = 9,
            question = "Who was chosen as the first President of Independent India?",
            options = listOf("Dr. Sarvepalli Radhakrishnan", "Dr. Rajendra Prasad", "Lal Bahadur Shastri", "Sardar Vallabhbhai Patel"),
            correctAnswerIndex = 1,
            explanation = "Dr. Raj राजेंद्र Prasad served as the first President of the Republic of India from 1950 to 1962.",
            category = "History"
        ),
        GKQuestion(
            id = 10,
            question = "Which famous temple in Odisha is traditionally called the 'Black Pagoda'?",
            options = listOf("Jagannath Temple", "Konark Sun Temple", "Lingaraj Temple", "Mukteshwar Temple"),
            correctAnswerIndex = 1,
            explanation = "The Konark Sun Temple was called the Black Pagoda by European sailors because of its dark visual mass and magnetic properties.",
            category = "General Awareness"
        ),
        GKQuestion(
            id = 11,
            question = "Which state is widely known as the 'Spice Garden of India'?",
            options = listOf("Karnataka", "Kerala", "Tamil Nadu", "Andhra Pradesh"),
            correctAnswerIndex = 1,
            explanation = "Kerala is known as the Spice Garden of India due to its extensive variety, history, and cultivation of hot and exotic spices.",
            category = "Geography"
        ),
        GKQuestion(
            id = 12,
            question = "What is the name of the currency of Japan?",
            options = listOf("Yuan", "Yen", "Won", "Baht"),
            correctAnswerIndex = 1,
            explanation = "The Japanese currency is called the Yen (¥), which is the third most traded currency in foreign exchange markets.",
            category = "General Awareness"
        )
    )
}
