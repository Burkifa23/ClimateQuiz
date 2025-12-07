import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for QuizManager.
 * Covers core logic (filtering, scoring) AND database integration.
 */
class QuizManagerTest {

    private static final String TEST_DB_FILE = "test_quiz_manager.db";
    private QuizManager quizManager;
    private PersistenceManager testPersistenceManager;

    @BeforeEach
    void setUp() {
        // Clean up old test DB to ensure fresh start
        File dbFile = new File(TEST_DB_FILE);
        if (dbFile.exists()) {
            dbFile.delete();
        }

        // Initialize REAL PersistenceManager for testing
        testPersistenceManager = new PersistenceManager(TEST_DB_FILE);
        testPersistenceManager.initializeDatabase();

        // Pass it to the Manager
        quizManager = new QuizManager(testPersistenceManager);
        quizManager.loadQuestions();
    }

    @AfterEach
    void tearDown() {
        // Clean up the database file after tests run
        File dbFile = new File(TEST_DB_FILE);
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    // ========== QUESTION LOADING TESTS ==========

    @Test
    @DisplayName("loadQuestions should populate question bank")
    void testLoadQuestions() {
        // We verify loading by ensuring startQuiz doesn't throw an "Empty Bank" exception
        assertDoesNotThrow(() -> quizManager.startQuiz("Tester", Difficulty.EASY));
        assertDoesNotThrow(() -> quizManager.startQuiz("Tester", Difficulty.HARD));
    }

    // START QUIZ TESTS

    @Test
    @DisplayName("startQuiz should initialize quiz session")
    void testStartQuiz() {
        assertDoesNotThrow(() -> quizManager.startQuiz("Alice", Difficulty.EASY));

        assertEquals("Alice", quizManager.getUserName());
        assertFalse(quizManager.getCurrentQuizQuestions().isEmpty());
        assertEquals(0, quizManager.getCurrentScore());
    }

    @Test
    @DisplayName("startQuiz should filter questions by difficulty")
    void testStartQuizFiltersQuestions() {
        quizManager.startQuiz("Bob", Difficulty.EASY);

        List<Questions> quizQuestions = quizManager.getCurrentQuizQuestions();

        assertFalse(quizQuestions.isEmpty());

        for (Questions q : quizQuestions) {
            assertEquals(Difficulty.EASY, q.getDifficultyLevel());
        }
    }

    @Test
    @DisplayName("startQuiz should reject null username")
    void testStartQuizNullUsername() {
        assertThrows(IllegalArgumentException.class, () ->
                quizManager.startQuiz(null, Difficulty.EASY));
    }

    @Test
    @DisplayName("startQuiz should reject empty username")
    void testStartQuizEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () ->
                quizManager.startQuiz("   ", Difficulty.EASY));
    }

    @Test
    @DisplayName("startQuiz should reject null difficulty")
    void testStartQuizNullDifficulty() {
        assertThrows(IllegalArgumentException.class, () ->
                quizManager.startQuiz("Alice", null));
    }

    @Test
    @DisplayName("startQuiz should trim username whitespace")
    void testStartQuizTrimsUsername() {
        quizManager.startQuiz("  Bob  ", Difficulty.EASY);
        assertEquals("Bob", quizManager.getUserName());
    }

    @Test
    @DisplayName("startQuiz should reset score for new quiz")
    void testStartQuizResetsScore() {
        quizManager.startQuiz("Alice", Difficulty.EASY);
        String correctAns = quizManager.getQuestion(0).getCorrectAnswer();
        quizManager.answerQuestion(0, correctAns);
        assertTrue(quizManager.getCurrentScore() > 0);

        // Second quiz should start at 0
        quizManager.startQuiz("Bob", Difficulty.HARD);
        assertEquals(0, quizManager.getCurrentScore());
    }

    // ANSWER QUESTION TESTS

    @Test
    @DisplayName("answerQuestion should update score for correct answer")
    void testAnswerQuestionCorrect() {
        quizManager.startQuiz("Alice", Difficulty.EASY);

        Questions firstQuestion = quizManager.getQuestion(0);
        String correctAnswer = firstQuestion.getCorrectAnswer();

        boolean result = quizManager.answerQuestion(0, correctAnswer);

        assertTrue(result);
        assertEquals(1, quizManager.getCurrentScore());
    }

    @Test
    @DisplayName("answerQuestion should not update score for incorrect answer")
    void testAnswerQuestionIncorrect() {
        quizManager.startQuiz("Alice", Difficulty.EASY);

        boolean result = quizManager.answerQuestion(0, "Definitely Wrong Answer");

        assertFalse(result);
        assertEquals(0, quizManager.getCurrentScore());
    }

    @Test
    @DisplayName("answerQuestion should handle multiple correct answers")
    void testAnswerMultipleQuestions() {
        quizManager.startQuiz("Alice", Difficulty.EASY);

        int correctAnswers = 0;
        List<Questions> questions = quizManager.getCurrentQuizQuestions();

        for (int i = 0; i < Math.min(3, questions.size()); i++) {
            Questions q = questions.get(i);
            boolean isCorrect = quizManager.answerQuestion(i, q.getCorrectAnswer());
            if (isCorrect) correctAnswers++;
        }

        assertEquals(correctAnswers, quizManager.getCurrentScore());
    }

    @Test
    @DisplayName("answerQuestion should throw exception if no quiz started")
    void testAnswerQuestionNoQuiz() {
        // Create a separate instance for this test to avoid setUp() state
        QuizManager newManager = new QuizManager(testPersistenceManager);
        newManager.loadQuestions();

        assertThrows(IllegalStateException.class, () ->
                newManager.answerQuestion(0, "Any answer"));
    }

    @Test
    @DisplayName("answerQuestion should throw exception for invalid index")
    void testAnswerQuestionInvalidIndex() {
        quizManager.startQuiz("Alice", Difficulty.EASY);

        assertThrows(IndexOutOfBoundsException.class, () ->
                quizManager.answerQuestion(-1, "Answer"));

        assertThrows(IndexOutOfBoundsException.class, () ->
                quizManager.answerQuestion(1000, "Answer"));
    }

    // GETTER TESTS

    @Test
    @DisplayName("getTotalQuestions should return correct count")
    void testGetTotalQuestions() {
        quizManager.startQuiz("Alice", Difficulty.EASY);

        int total = quizManager.getTotalQuestions();
        assertTrue(total > 0);
        assertEquals(quizManager.getCurrentQuizQuestions().size(), total);
    }

    @Test
    @DisplayName("getQuestion should return correct question")
    void testGetQuestion() {
        quizManager.startQuiz("Alice", Difficulty.EASY);

        Questions first = quizManager.getQuestion(0);
        assertNotNull(first);
        assertEquals(Difficulty.EASY, first.getDifficultyLevel());
    }

    @Test
    @DisplayName("getCurrentQuizQuestions should return defensive copy")
    void testGetCurrentQuizQuestionsDefensiveCopy() {
        quizManager.startQuiz("Alice", Difficulty.EASY);

        List<Questions> questions = quizManager.getCurrentQuizQuestions();
        int originalSize = questions.size();

        // Try to modify returned list
        questions.clear();

        // Original should be unchanged in the manager
        assertEquals(originalSize, quizManager.getCurrentQuizQuestions().size());
    }

    //  POLYMORPHISM TESTS

    @Test
    @DisplayName("Polymorphism should work for different question types")
    void testPolymorphism() {
        quizManager.startQuiz("Alice", Difficulty.EASY);

        List<Questions> questions = quizManager.getCurrentQuizQuestions();

        for (Questions q : questions) {
            boolean result = q.checkAnswer(q.getCorrectAnswer());
            assertTrue(result, "CheckAnswer should return true for correct answer");
        }
    }

    // DATABASE INTEGRATION TESTS

    @Test
    @DisplayName("recordScore should save to actual database")
    void testRecordScoreIntegration() {
        quizManager.startQuiz("IntegrationTester", Difficulty.EASY);

        // Answer one question correctly
        Questions q = quizManager.getQuestion(0);
        quizManager.answerQuestion(0, q.getCorrectAnswer());

        // Save
        assertDoesNotThrow(() -> quizManager.recordScore());

        // Verify via PersistenceManager directly to ensure it hit the DB
        List<UserScoreRecord> scores = testPersistenceManager.loadAllScores();
        assertFalse(scores.isEmpty());
        assertEquals("IntegrationTester", scores.get(0).getUserName());
        assertEquals(1, scores.get(0).getScore());
    }

    @Test
    @DisplayName("getLeaderboard should return sorted scores from DB")
    void testGetLeaderboardIntegration() {


        // 1. Low Score
        quizManager.startQuiz("LowScorer", Difficulty.EASY);
        quizManager.recordScore(); // Score 0

        // 2. High Score
        quizManager.startQuiz("HighScorer", Difficulty.EASY);
        Questions q = quizManager.getQuestion(0);
        quizManager.answerQuestion(0, q.getCorrectAnswer()); // Score 1
        quizManager.recordScore();

        // Retrieve via QuizManager
        List<UserScoreRecord> leaderboard = quizManager.getLeaderboard();

        assertEquals(2, leaderboard.size());
        assertEquals("HighScorer", leaderboard.get(0).getUserName()); // High score first
        assertEquals("LowScorer", leaderboard.get(1).getUserName());  // Low score second
    }
}