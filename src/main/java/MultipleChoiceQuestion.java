import java.util.ArrayList;
import java.util.List;
public class MultipleChoiceQuestion extends Questions {
    private String[] options;

    public MultipleChoiceQuestion(String question, String[] options, String correctAnswer, Difficulty difficultyLevel) {
        super(question, correctAnswer, difficultyLevel);
        this.options = options;
    }

    @Override
    public String displayQuestion() {
        StringBuilder builder = new StringBuilder();
        builder.append(question).append("\n");

        char letter = 'A';
        for (String option : options) {
            builder.append(letter).append(". ").append(option).append("\n");
            letter++;
        }
        return builder.toString();
    }

    @Override
    public boolean checkAnswer(String userAnswer) {
        if (userAnswer == null) {
            return false;
        }
        String cleanUser = userAnswer.trim();
        return true;

    }
}
