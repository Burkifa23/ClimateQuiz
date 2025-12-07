import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionBank {


    private static final List<Questions> ALL_QUESTIONS = new ArrayList<>();


    static {
        loadAllQuestions();
    }

    private static void loadAllQuestions() {
        // EASY QUESTIONS
        ALL_QUESTIONS.add(new MultipleChoiceQuestion(
                "What is climate change?",
                new String[]{"Oxygen levels", "Nitrogen shifts", "Long-term weather shifts", "Dioxide levels"},
                "Long-term weather shifts",
                Difficulty.EASY));

        ALL_QUESTIONS.add(new MultipleChoiceQuestion(
                "Which gas is a major greenhouse gas?",
                new String[]{"Oxygen", "Nitrogen", "Carbon Dioxide", "Argon"},
                "Carbon Dioxide",
                Difficulty.EASY));

        ALL_QUESTIONS.add(new MultipleChoiceQuestion(
                "What does deforestation do?",
                new String[]{"Decreases CO2", "Increases CO2", "Stabilizes climate", "Cools Earth"},
                "Increases CO2",
                Difficulty.EASY));

        ALL_QUESTIONS.add(new MultipleChoiceQuestion(
                "Which is the most important day young people can take action on climate?",
                new String[]{"Earth Day", "World Environment Day", "Every day", "Cleanup Day"},
                "Every day",
                Difficulty.EASY));

        ALL_QUESTIONS.add(new TrueFalseQuestion(
                "Climate change is directly associated with greenhouse gases.",
                "True",
                Difficulty.EASY));

        ALL_QUESTIONS.add(new TrueFalseQuestion(
                "Only industry should be concerned about climate change.",
                "False",
                Difficulty.EASY));

        ALL_QUESTIONS.add(new TrueFalseQuestion(
                "Deforestation contributes to global warming.",
                "True",
                Difficulty.EASY));

        ALL_QUESTIONS.add(new TrueFalseQuestion(
                "More greenhouse gases = lower temperatures.",
                "False",
                Difficulty.EASY));

        // HARD QUESTIONS
        ALL_QUESTIONS.add(new MultipleChoiceQuestion(
                "Mainly, Ozonosphere is depleted by:",
                new String[]{"CFCs", "Excess CO2", "Ozone", "Excess CO"},
                "CFCs",
                Difficulty.HARD));

        ALL_QUESTIONS.add(new MultipleChoiceQuestion(
                "This process removes carbon dioxide from the atmosphere:",
                new String[]{"Lightning", "Deforestation", "Burning fossil fuels", "Photosynthesis"},
                "Photosynthesis",
                Difficulty.HARD));

        ALL_QUESTIONS.add(new MultipleChoiceQuestion(
                "Which is NOT a major greenhouse gas?",
                new String[]{"Water vapour", "Nitrogen", "Methane", "Carbon dioxide"},
                "Nitrogen",
                Difficulty.HARD));

        ALL_QUESTIONS.add(new MultipleChoiceQuestion(
                "Which sector contributes most to global emissions?",
                new String[]{"Agriculture", "Transport", "Forestry", "Energy supply"},
                "Energy supply",
                Difficulty.HARD));

        ALL_QUESTIONS.add(new TrueFalseQuestion(
                "The oceans absorb about 50% of the excess heat in the climate system.",
                "False", // Corrected fact
                Difficulty.HARD));

        ALL_QUESTIONS.add(new TrueFalseQuestion(
                "Transportation is the largest contributor to global greenhouse gas.",
                "False",
                Difficulty.HARD));

        ALL_QUESTIONS.add(new TrueFalseQuestion(
                "Ozone layer depletion is mainly caused by Chlorofluorocarbons.",
                "True",
                Difficulty.HARD));

        ALL_QUESTIONS.add(new TrueFalseQuestion(
                "Climate change can increase the burden of vector-borne and water-borne diseases by lengthening their transmission season and altering their geographic range.",
                "True",
                Difficulty.HARD));
    }

    /**
     * Provides an unmodifiable list of all loaded questions.
     * @return A list of all questions.
     */
    public static List<Questions> getAllQuestions() {
        // Return a copy to prevent external modification of the bank
        return new ArrayList<>(ALL_QUESTIONS);
    }
}