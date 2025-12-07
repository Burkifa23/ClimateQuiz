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
        String cleanCorrect = correctAnswer.trim();

        // 1. Direct match (e.g., user types "Carbon Dioxide")
        if (cleanUser.equalsIgnoreCase(cleanCorrect)) {
            return true;
        }

        // 2. Letter match (e.g., user types "A" or "B")
        if (cleanUser.length() == 1) {
            char userChar = Character.toUpperCase(cleanUser.charAt(0));

            // Ensure input is between A and D (or however many options there are)
            if (userChar >= 'A' && userChar < 'A' + options.length) {
                int index = userChar - 'A';
                String selectedOption = options[index]; // Get the text at that index

                // Compare the text of the selected option with the correct answer
                return selectedOption.trim().equalsIgnoreCase(cleanCorrect);
            }
        }

        return false;
    }
    // Added for GUI support
    public String[] getOptions() {
        return options;
    }
}
