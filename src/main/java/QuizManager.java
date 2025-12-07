
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Central manager class that orchestrates the quiz application.
 * - Manages the quiz flow (Start, Answer, End).
 * - Delegates data storage to QuestionBank.
 * - Delegates validation to the Questions class.
 */
public class QuizManager {

    // ATTRIBUTES
    private List<Questions> questionBank;
    private int currentScore;
    private String userName;
    private Difficulty selectedDifficulty;
    // private PersistenceManager persistenceManager;
    private List<Questions> currentQuizQuestions;

    //  CONSTRUCTOR
    public QuizManager() {
        this.questionBank = new ArrayList<>();
        this.currentScore = 0;
        this.currentQuizQuestions = new ArrayList<>();

        // Waiting for Vera
        // this.persistenceManager = new PersistenceManager();
        // this.persistenceManager.initializeDatabase();
    }

    // Constructor for testing (allows mocking PersistenceManager)
    /*
    public QuizManager(PersistenceManager pm) {
        this.questionBank = new ArrayList<>();
        this.currentScore = 0;
        this.persistenceManager = pm;
        this.currentQuizQuestions = new ArrayList<>();
    }
    */
    // LOGIC: LOADING QUESTIONS

    /**
     * Loads questions from the static QuestionBank utility class.
     * This keeps the Manager clean and focused on logic, not data storage.
     */
    public void loadQuestions() {
        // Retrieve the single, unified list of all questions from the dedicated bank
        questionBank = QuestionBank.getAllQuestions();

        if (questionBank.isEmpty()) {
            System.err.println("FATAL: Question bank failed to load! Check QuestionBank.java.");
        } else {
            System.out.println("Loaded " + questionBank.size() + " questions from QuestionBank.");
        }
    }

    // LOGIC: QUIZ FLOW

    public void startQuiz(String userName, Difficulty difficulty) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty");
        }
        if (difficulty == null) {
            throw new IllegalArgumentException("Difficulty cannot be null");
        }
        if (questionBank == null || questionBank.isEmpty()) {
            throw new IllegalStateException("Question bank is empty. Call loadQuestions() first.");
        }

        this.userName = userName.trim();
        this.selectedDifficulty = difficulty;
        this.currentScore = 0;

        // Filter using getDifficultyLevel()"
        this.currentQuizQuestions = questionBank.stream()
                .filter(q -> q.getDifficultyLevel() == difficulty)
                .collect(Collectors.toList());

        if (currentQuizQuestions.isEmpty()) {
            throw new IllegalStateException("No questions available for difficulty: " + difficulty);
        }
    }

    public boolean answerQuestion(int questionIndex, String userAnswer) {
        if (currentQuizQuestions == null || currentQuizQuestions.isEmpty()) {
            throw new IllegalStateException("No quiz in progress. Call startQuiz() first.");
        }
        if (questionIndex < 0 || questionIndex >= currentQuizQuestions.size()) {
            throw new IndexOutOfBoundsException("Invalid question index: " + questionIndex);
        }

        Questions question = currentQuizQuestions.get(questionIndex);


        boolean isCorrect = question.checkAnswer(userAnswer);

        if (isCorrect) {
            currentScore++;
        }
        return isCorrect;
    }

    // LOGIC: PERSISTENCE (Wrappers)

    public void recordScore() {
        if (userName == null) throw new IllegalStateException("No quiz played");

        // When Vera is Ready
        /*
        UserScoreRecord record = new UserScoreRecord(
            userName,
            currentScore,
            selectedDifficulty,
            System.currentTimeMillis()
        );
        persistenceManager.saveScore(record);
        */
    }

    /*
    public List<UserScoreRecord> getLeaderboard() {
        // UNCOMMENT THIS WHEN DEV 2 IS READY
        // return persistenceManager.loadAllScores();
        return new ArrayList<>(); // Temporary placeholder
    }

     */

    // GETTERS

    public int getCurrentScore() {
        return currentScore;
    }

    public String getUserName() {
        return userName;
    }

    public List<Questions> getCurrentQuizQuestions() {
        return new ArrayList<>(currentQuizQuestions);
    }

    public Questions getQuestion(int index) {
        return currentQuizQuestions.get(index);
    }

    public int getTotalQuestions() {
        return currentQuizQuestions == null ? 0 : currentQuizQuestions.size();
    }
}