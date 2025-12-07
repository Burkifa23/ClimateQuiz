import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QuizManager {

    // ATTRIBUTES
    private List<Questions> questionBank;
    private int currentScore;
    private String userName;
    private Difficulty selectedDifficulty;
    private PersistenceManager persistenceManager;
    private List<Questions> currentQuizQuestions;

    // CONSTRUCTOR
    public QuizManager() {
        this.questionBank = new ArrayList<>();
        this.currentScore = 0;
        this.currentQuizQuestions = new ArrayList<>();

        // Initialize Database connection
        this.persistenceManager = new PersistenceManager();
        try {
            this.persistenceManager.initializeDatabase();
        } catch (Exception e) {
            System.err.println("Warning: Database init failed: " + e.getMessage());
        }
    }

    // Constructor for testing (allows mocking PersistenceManager)
    public QuizManager(PersistenceManager pm) {
        this.questionBank = new ArrayList<>();
        this.currentScore = 0;
        this.persistenceManager = pm;
        this.currentQuizQuestions = new ArrayList<>();
    }

    // LOGIC: LOADING QUESTIONS
    public void loadQuestions() {
        questionBank = QuestionBank.getAllQuestions();
        if (questionBank == null || questionBank.isEmpty()) {
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

    // LOGIC: PERSISTENCE

    public void recordScore() {
        if (userName == null) throw new IllegalStateException("No quiz played");

        UserScoreRecord record = new UserScoreRecord(
                userName,
                currentScore,
                selectedDifficulty,
                System.currentTimeMillis()
        );

        try {
            persistenceManager.saveScore(record);
            System.out.println("Score recorded successfully for " + userName);
        } catch (Exception e) {
            System.err.println("Failed to record score: " + e.getMessage());
        }
    }

    public List<UserScoreRecord> getLeaderboard() {
        try {
            return persistenceManager.loadAllScores();
        } catch (Exception e) {
            System.err.println("Failed to load leaderboard: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // GETTERS
    public int getCurrentScore() { return currentScore; }
    public String getUserName() { return userName; }
    public List<Questions> getCurrentQuizQuestions() { return new ArrayList<>(currentQuizQuestions); }
    public Questions getQuestion(int index) { return currentQuizQuestions.get(index); }
    public int getTotalQuestions() { return currentQuizQuestions == null ? 0 : currentQuizQuestions.size(); }
}