import org.junit.jupiter.api.*;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for QuizManager.
 *
 */
class QuizManagerTest {

    private static final String TEST_DB_FILE = "test_quiz_manager.db";
    private QuizManager quizManager;

    @BeforeEach
    void setUp() {
        quizManager = new QuizManager(null);
        quizManager.loadQuestions();
    }

    // QUESTION LOADING TESTS

    @Test
    @DisplayName("loadQuestions should populate question bank")
    void testLoadQuestions() {
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

        // Second quiz
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
        QuizManager newManager = new QuizManager(null);
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


    // SCORE RECORDING TESTS (DISABLED)

    @Test
    @DisplayName("recordScore should be safe to call even without Database")
    void testRecordScoreSafety() {
        quizManager.startQuiz("Alice", Difficulty.EASY);
        // Should not throw exception even if DB is disabled (logic inside QuizManager handles it)
        assertDoesNotThrow(() -> quizManager.recordScore());
    }

    /* * DISABLED UNTIL Vera's MERGES PERSISTENCE MANAGER
     * @Test
    @DisplayName("recordScore should save correct score")
    void testRecordScoreCorrectValue() {
        quizManager.startQuiz("Bob", Difficulty.EASY);
        for (int i = 0; i < 3; i++) {
            quizManager.answerQuestion(i, quizManager.getQuestion(i).getCorrectAnswer());
        }
        quizManager.recordScore();
        List<UserScoreRecord> scores = quizManager.getLeaderboard();
        assertEquals(3, scores.get(0).getScore());
    }
    */

    //  GETTER TESTS

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

    // POLYMORPHISM TESTS

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
}