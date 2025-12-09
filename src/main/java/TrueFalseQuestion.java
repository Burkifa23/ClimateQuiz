public class TrueFalseQuestion extends Questions {

    public TrueFalseQuestion(String question, String correctAnswer, Difficulty difficultyLevel) {
        super(question,correctAnswer, difficultyLevel);

    }

    public String[] getTAndFOptions() {
        return new String[] {"True", "False"};
    }

    @Override
    public String displayQuestion() {
        return  "True or False?";
    }

    @Override
    public boolean checkAnswer(String userAnswer) {
        if (userAnswer == null) {
            return false;
        }

        String user = userAnswer.trim();
        String correct = correctAnswer.trim();

        // 1. Check full text: "True" or "False"
        if (user.equalsIgnoreCase(correct)) {
            return true;
        }

        // 2. Check letter: "T" or "F"
        if (user.length() == 1) {
            char Char = Character.toUpperCase(user.charAt(0));
            char correctChar = Character.toUpperCase(correct.charAt(0));

            return Char == correctChar;
        }

        return false;
    }
}
