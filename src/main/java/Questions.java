public abstract class Questions {
    protected String question;
    protected String correctAnswer;
    protected Difficulty difficultyLevel;


    public Questions(String question, String correctAnswer,Difficulty difficultyLevel) {
        if (question == null || question.trim().isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be null or empty");
        }

        if (correctAnswer == null || correctAnswer.trim().isEmpty()) {
            throw new IllegalArgumentException("Correct answer cannot be null or empty");
        }

        if (difficultyLevel == null) {
            throw new IllegalArgumentException("Difficulty cannot be null");
        }

        this.question = question.trim();
        this.difficultyLevel = difficultyLevel;
        this.correctAnswer = correctAnswer.trim();

    }
    public String getText(){
        return  question;
    }
    public Difficulty getDifficultyLevel(){
        return difficultyLevel;
    }
    public String getCorrectAnswer() {
        return correctAnswer;
    }
    public abstract String displayQuestion();


    public abstract boolean checkAnswer(String userAnswer);

    public void setQuestionText(String questionText) {
        if (questionText == null || questionText.trim().isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be null or empty");
        }
        this.question = questionText.trim();
    }

    public void setCorrectAnswer(String correctAnswer) {
        if (correctAnswer == null || correctAnswer.trim().isEmpty()) {
            throw new IllegalArgumentException("Correct answer cannot be null or empty");
        }
        this.correctAnswer = correctAnswer;
    }

    public void setDifficulty(Difficulty difficultyLevel) {
        if (difficultyLevel == null) {
            throw new IllegalArgumentException("Difficulty cannot be null");
        }
        this.difficultyLevel = difficultyLevel;
    }



}
