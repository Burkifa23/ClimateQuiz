import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
public class QuestionBank {

    private static final List<MultipleChoiceQuestion> multipleChoiceQuestions = new ArrayList<>();
    private static final List<TrueFalseQuestion> trueFalseQuestions = new ArrayList<>();


    private static void loadQuestions() {
        String[] mcQ1Options = {"Oxygen", "Nitrogen", "Carbon", "Dioxide"};
        MultipleChoiceQuestion mcQ1 = new MultipleChoiceQuestion(
                "What is climate change?",
                mcQ1Options,
                "Long-term shifts in temperature/weather patterns",
                Difficulty.EASY);
        multipleChoiceQuestions.add(mcQ1);

        String[] mcQ2Options = {"Oxygen", "Nitrogen", "Carbon", "Dioxide"};
        MultipleChoiceQuestion mcQ2 = new MultipleChoiceQuestion(
                "Which gas is a major greenhouse gas?",
                mcQ2Options,
                "Carbon",
                Difficulty.EASY);
        multipleChoiceQuestions.add(mcQ2);

        String[] mcQ3Options = {" Decreases CO2", "Increases CO2", "Stabilizes climate", "Cools Earth."};
        MultipleChoiceQuestion mcQ3 = new MultipleChoiceQuestion(
                "What does deforestation do?",
                mcQ2Options,
                "Increases CO2",
                Difficulty.EASY);
        multipleChoiceQuestions.add(mcQ3);


        String[] mcQ4Options = {"Methane", "Nitrous oxide", "Water vapor", "Carbon Monoxide"};
        MultipleChoiceQuestion mcQ4 = new MultipleChoiceQuestion(
                "Which of these is NOT a major greenhouse gas?",
                mcQ2Options,
                "Carbon Monoxide",
                Difficulty.EASY);
        multipleChoiceQuestions.add(mcQ4);


        String[] mcQ5Options = {"Earth Day", "World Environment", "Every day", "World Cleanup Day"};
        MultipleChoiceQuestion mcQ5 = new MultipleChoiceQuestion(
                "Which is the most important day young people can take action on climate?",
                mcQ5Options,
                "Every day",
                Difficulty.EASY);
        multipleChoiceQuestions.add(mcQ5);

        String[] mcQ6Options = {"Eat less meat", "Plant a tree", "Avoid cutting down of trees", "All of the above"};
        MultipleChoiceQuestion mcQ6 = new MultipleChoiceQuestion(
                "Which one of the activities below is an action you can take to reduce your carbon footprint",
                mcQ5Options,
                "All of the above",
                Difficulty.EASY);
        multipleChoiceQuestions.add(mcQ6);

        String[] mcQ7Options = {"Humidity", "Precipitation", "Greenhouse gases", "Nitrates"};
        MultipleChoiceQuestion mcQ7 = new MultipleChoiceQuestion(
                "What are the gases called that become trapped in the earth`s atmosphere and are heating the planet",
                mcQ5Options,
                "Greenhouse gases",
                Difficulty.EASY);
        multipleChoiceQuestions.add(mcQ7);

        String[] mcQ8Options = {"Transportation", "Fashion", "Farming ", "They all do"};
        MultipleChoiceQuestion mcQ8 = new MultipleChoiceQuestion(
                "Which of the following sectors oes not contribute to greenhouse gas emissions",
                mcQ5Options,
                "They all do",
                Difficulty.EASY);
        multipleChoiceQuestions.add(mcQ8);

        String[] mcQ9Options = {"Biosphere", "Atmosphere", "Barometer", "Climate"};
        MultipleChoiceQuestion mcQ9 = new MultipleChoiceQuestion(
                "All of the gases on the planet make up the earth`s ....",
                mcQ9Options,
                "Atmosphere",
                Difficulty.EASY);
        multipleChoiceQuestions.add(mcQ9);

        String[] mcQ10Options = {"weather is a subtle change while climate is drastic", "weather is short-term while climate is long-term", "weather can be forecasted but climate cannot", "They are the same thing"};
        MultipleChoiceQuestion mcQ10 = new MultipleChoiceQuestion(
                "How is weather different form climate",
                mcQ5Options,
                "Weather is short term while climate is long-term",
                Difficulty.EASY);
        multipleChoiceQuestions.add(mcQ10);

        String[] mcQ11Options = {"CFCs", "excess CO2", "ozone", "excess CO"};
        MultipleChoiceQuestion mcQ11 = new MultipleChoiceQuestion(
                "Mainly, Ozonosphere is depleted by",
                mcQ5Options,
                "CFCs",
                Difficulty.HARD);
        multipleChoiceQuestions.add(mcQ11);

        String[] mcQ12Options = {"lightning", "deforestation", "burning of fossil fuels", "photosynthesis"};
        MultipleChoiceQuestion mcQ12 = new MultipleChoiceQuestion(
                "This process is functional in removing carbon dioxide from the atmosphere",
                mcQ5Options,
                "photosynthesis",
                Difficulty.HARD);
        multipleChoiceQuestions.add(mcQ12);

        String[] mcQ13Options = {"water vapour", "ozone", "methane", "carbon dioxide"};
        MultipleChoiceQuestion mcQ13 = new MultipleChoiceQuestion(
                "Which of the following is not a major greenhouse gas?",
                mcQ5Options,
                "ozone",
                Difficulty.HARD);
        multipleChoiceQuestions.add(mcQ13);

        String[] mcQ14Options = {"carbon monoxide", "ozone", "fluorides", "sulphur dioxide"};
        MultipleChoiceQuestion mcQ14 = new MultipleChoiceQuestion(
                "Ultraviolet radiations from sunlight causes a reaction producing",
                mcQ5Options,
                "ozone",
                Difficulty.HARD);
        multipleChoiceQuestions.add(mcQ14);

        String[] mcQ15Options = {"Short waves", "Medium waves", "Long Waves", "Tidal Waves"};
        MultipleChoiceQuestion mcQ15 = new MultipleChoiceQuestion(
                "Which type of light wave has the greatest impact on the Greenhouse Effect?",
                mcQ5Options,
                "Long Waves",
                Difficulty.HARD);
        multipleChoiceQuestions.add(mcQ15);

        String[] mcQ16Options = {"Sea level rising", "Loss of Arctic Sea Ice", "Less water vapour in the air", "More extreme weather events"};
        MultipleChoiceQuestion mcQ16 = new MultipleChoiceQuestion(
                "Which of the following is not evidence of global warming ",
                mcQ5Options,
                "Less water vapour in the atmosphere",
                Difficulty.HARD);
        multipleChoiceQuestions.add(mcQ16);

        String[] mcQ17Options = {"Adequate rainfall", "pure air", "deficiency of freshwater", "less soil pollution"};
        MultipleChoiceQuestion mcQ17 = new MultipleChoiceQuestion(
                "Which of the following is a result of climate change? ",
                mcQ5Options,
                "deficiency in fresh water",
                Difficulty.HARD);
        multipleChoiceQuestions.add(mcQ17);

        String[] mcQ18Options = {"surface temperature ", "precipitation", "atmospheric conditions", "changes in seasonal variation"};
        MultipleChoiceQuestion mcQ18 = new MultipleChoiceQuestion(
                "Which of the following is used to determine global warming but not climate change? ",
                mcQ5Options,
                "surface temperatures",
                Difficulty.HARD);
        multipleChoiceQuestions.add(mcQ18);

        String[] mcQ19Options = {"more polar bears", "release of trapped methane and CO2", "increased ice cream sales", "faster glacier formation"};
        MultipleChoiceQuestion mcQ19 = new MultipleChoiceQuestion(
                "What is a major consequence of Arctic permafrost thawing due to warming",
                mcQ5Options,
                "release of trapped methane and CO2 ",
                Difficulty.HARD);
        multipleChoiceQuestions.add(mcQ19);

        String[] mcQ20Options = {"agriculture", "transport", "forestry", "energy supply"};
        MultipleChoiceQuestion mcQ20 = new MultipleChoiceQuestion(
                "What is the following activities contribute the most to carbon emission globally?",
                mcQ5Options,
                "Energy supply",
                Difficulty.HARD);
        multipleChoiceQuestions.add(mcQ20);

        TrueFalseQuestion tfQ1 = new TrueFalseQuestion("Climate change is directly associated with the concentration of greenhouse gases (GHGs), such as carbon dioxide and methane, ",
                "True", Difficulty.EASY);
        trueFalseQuestions.add(tfQ1);

        TrueFalseQuestion tfQ2 = new TrueFalseQuestion("Only the industry should be concerned about climate change. ",
                "False", Difficulty.EASY);
        trueFalseQuestions.add(tfQ2);

        TrueFalseQuestion tfQ3 = new TrueFalseQuestion("Changes in climate affect public health and the global economy",
                "True", Difficulty.EASY);
        trueFalseQuestions.add(tfQ3);

        TrueFalseQuestion tfQ4 = new TrueFalseQuestion("Developing countries are more resilient to climate impacts",
                "False", Difficulty.EASY);
        trueFalseQuestions.add(tfQ4);

        TrueFalseQuestion tfQ5 = new TrueFalseQuestion("Energy transition is one of the pillars for containing climate change.",
                "True", Difficulty.EASY);
        trueFalseQuestions.add(tfQ5);

        TrueFalseQuestion tfQ6 = new TrueFalseQuestion(
                "Individual actions make a difference in solving climate change issues",
                "True", Difficulty.EASY);
        trueFalseQuestions.add(tfQ6);

        TrueFalseQuestion tfQ7 = new TrueFalseQuestion(
                "Burning fossil fuels is an example of a human activity that emits greenhouse gases in the atmosphere",
                "True", Difficulty.EASY);
        trueFalseQuestions.add(tfQ7);

        TrueFalseQuestion tfQ8 = new TrueFalseQuestion(
                "More greenhouse gases = lower temperatures",
                "False", Difficulty.EASY);
        trueFalseQuestions.add(tfQ8);

        TrueFalseQuestion tfQ9 = new TrueFalseQuestion(
                "Deforestation contributes to global warming",
                "True", Difficulty.EASY);
        trueFalseQuestions.add(tfQ9);

        TrueFalseQuestion tfQ10 = new TrueFalseQuestion(
                "Global warming leads to more extreme weather conditions",
                "True", Difficulty.EASY);
        trueFalseQuestions.add(tfQ10);

        TrueFalseQuestion tfQ11 = new TrueFalseQuestion(
                "The oceans absorb about 50% of the excess heat in the climate system.",
                "False", Difficulty.HARD);
        trueFalseQuestions.add(tfQ11);

        TrueFalseQuestion tfQ12 = new TrueFalseQuestion(
                "Climate change is easier to predict than weather change",
                "True", Difficulty.HARD);
        trueFalseQuestions.add(tfQ12);

        TrueFalseQuestion tfQ13 = new TrueFalseQuestion(
                "The National Adaptation Plans can only be funded by national governments and international funds such as the Green Climate Fund and the Adaptation Fund",
                "False", Difficulty.HARD);
        trueFalseQuestions.add(tfQ13);

        TrueFalseQuestion tfQ14 = new TrueFalseQuestion(
                "Ozone layer depletion is mainly caused by Chlorofluorocarbons",
                "True", Difficulty.HARD);
        trueFalseQuestions.add(tfQ14);

        TrueFalseQuestion tfQ15 = new TrueFalseQuestion(
                "Global warming is mainly caused by natural variations in earth`s orbit",
                "True", Difficulty.HARD);
        trueFalseQuestions.add(tfQ15);

        TrueFalseQuestion tfQ16 = new TrueFalseQuestion(
                "Increase in green house gases can result in an increase in pH level and water",
                "True", Difficulty.HARD);
        trueFalseQuestions.add(tfQ16);

        TrueFalseQuestion tfQ17 = new TrueFalseQuestion(
                "Transportation is the largest contributors to global greenhouse gas",
                "False", Difficulty.HARD);
        trueFalseQuestions.add(tfQ17);

        TrueFalseQuestion tfQ18 = new TrueFalseQuestion(
                "Climate change can increase the burden of vector-borne and water-borne diseases by lengthening their transmission season and altering their geographic range.",
                "True", Difficulty.HARD);
        trueFalseQuestions.add(tfQ18);

        TrueFalseQuestion tfQ19 = new TrueFalseQuestion(
                "El Nino and La Nina are natural climate phenomena which may be influenced by human factors",
                "True", Difficulty.HARD);
        trueFalseQuestions.add(tfQ19);

        TrueFalseQuestion tfQ20 = new TrueFalseQuestion(
                "Actions on adaptation to climate change receive more investment than actions on mitigation.",
                "True", Difficulty.HARD);
        trueFalseQuestions.add(tfQ20);
    }

    public static List<MultipleChoiceQuestion> getMultipleChoiceQuestions(){
        return multipleChoiceQuestions;
    }
    public static List<TrueFalseQuestion> getTrueFalseQuestions() {
        return trueFalseQuestions;
    }




}
