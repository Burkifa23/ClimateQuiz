import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizApp {

    private JFrame frame;
    private CardLayout cardLayout;

    // 3 screens
    private JPanel startPanel;
    private JPanel questionPanel;
    private JPanel resultPanel;

    // Button difficulty
    private Difficulty selectedDifficulty;

    //Username
    private String userName;

    //Quiz Manager
    private QuizManager quizManager;

    //Database
    private PersistenceManager dbase = new PersistenceManager();
    private UserScoreRecord userScoreRecord;

    //Track every card created
    private List<QuestionCard> questionCards = new ArrayList<>();

    // CONSTRUCTOR
    public QuizApp() {

        frame = new JFrame("Climate Change Quiz");
        frame.setSize(600, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        dbase.initializeDatabase();

        quizManager = new QuizManager();
        quizManager.loadQuestions();
        System.out.println("Questions loaded: " + quizManager.getTotalQuestions());

        cardLayout = new CardLayout();
        frame.setLayout(cardLayout);

        startPanel = stylePanel();
        questionPanel = stylePanel();
        resultPanel = stylePanel();

        StartScreen();

        frame.add(startPanel, "Start");
        frame.add(questionPanel, "Question");
        frame.add(resultPanel, "Result");

        cardLayout.show(frame.getContentPane(), "Start");
        frame.setVisible(true);
    }

    private JPanel stylePanel() {
        JPanel stylePanel = new JPanel();
        stylePanel.setBackground(ForestTheme.PRIMARY_BG);
        return stylePanel;
    }

    // CIRCLE BUTTON
    class CircleButton extends JToggleButton {

        private Color normalColor = ForestTheme.TEXT_COLOR;
        private Color selectedColor = ForestTheme.BUTTON_COLOR;
        private String letter;

        public CircleButton(String letter) {
            this.letter = letter;
            setPreferredSize(new Dimension(40, 40));
            setMinimumSize(new Dimension(40, 40));
            setMaximumSize(new Dimension(40, 40));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            // smooth edges
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // fill circle
            g2.setColor(isSelected() ? selectedColor : normalColor);
            g2.fillOval(0, 0, getWidth(), getHeight());

            // draw letter
            g2.setColor(isSelected() ? Color.WHITE : Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 22));

            //Center
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(letter)) / 2;
            int y = (getHeight() + fm.getAscent()) / 2 - 3;

            g2.drawString(letter, x, y);
            g2.dispose();
        }
    }

    // QUESTION CARD
    class QuestionCard extends JPanel {

        private String selectedAnswer = null;
        private Questions questionObject;

        public QuestionCard(String question, String[] options) {

            setOpaque(false);

            // Dynamic height
            int height = (options.length == 2) ? 220 : 320;
            setPreferredSize(new Dimension(600, height));
            setMaximumSize(new Dimension(600, height));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            // White rounded container
            JPanel background = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setColor(ForestTheme.CARD_BG);
                    g.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                }
            };
            background.setOpaque(false);
            background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
            background.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));


            // QUESTION TITLE
            JTextArea qLabel = new JTextArea(question);
            qLabel.setFont(ForestTheme.QUESTION_FONT);
            qLabel.setLineWrap(true);
            qLabel.setWrapStyleWord(true);
            qLabel.setEditable(false);
            qLabel.setOpaque(false);
            qLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            background.add(qLabel);
            background.add(Box.createVerticalStrut(20));

            // ANSWER OPTIONS
            String[] letters = {"A", "B", "C", "D"};
            ButtonGroup group = new ButtonGroup();
            int optionSpacing = (options.length == 2) ? 4 : 8;  // LESS space for T/F

            for (int i = 0; i < options.length; i++) {
                JPanel row = new JPanel();
                row.setOpaque(false);
                row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
                row.setCursor(new Cursor(Cursor.HAND_CURSOR));

                CircleButton btn = new CircleButton(letters[i]);
                group.add(btn);

                JLabel optLabel = new JLabel(options[i]);
                optLabel.setFont(ForestTheme.OPTION_FONT);
                optLabel.setForeground(ForestTheme.TEXT_DARK);
                optLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

                // Clicking the row selects the button and stores the answer
                row.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        btn.doClick();
                        selectedAnswer = optLabel.getText();
                    }
                });

                btn.addActionListener(e->{
                    selectedAnswer = optLabel.getText();
                });

                row.add(btn);
                row.add(optLabel);
                row.add(Box.createHorizontalGlue());

                background.add(row);
                background.add(Box.createVerticalStrut(optionSpacing));
            }
            add(background);
        }

        public String getSelectedAnswer() {
            return selectedAnswer;
        }
        public Questions getQuestionObject() {
            return questionObject;
        }
        public void setQuestionObject(Questions q) {
            this.questionObject = q;
        }
    }

    // START SCREEN
    public void StartScreen() {

        startPanel.removeAll();
        startPanel.setLayout(new BorderLayout());
        startPanel.setBackground(ForestTheme.PRIMARY_BG);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Title
        JLabel titleLabel = new JLabel("Climate Change Quiz");
        titleLabel.setFont(ForestTheme.TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Icon
        JLabel iconLabel = new JLabel("🌿");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        iconLabel.setForeground(Color.BLACK);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        // Subtitle
        JLabel subtitleLabel = new JLabel("Test your knowledge & learn something new!");
        subtitleLabel.setFont(ForestTheme.SUBTITLE_FONT);
        subtitleLabel.setForeground(ForestTheme.TEXT_COLOR);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Username field and label
        JLabel usernameLabel = new JLabel("Enter username:");
        usernameLabel.setFont(ForestTheme.BUTTON_FONT);
        usernameLabel.setForeground(ForestTheme.TEXT_COLOR);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        usernameField.setMaximumSize(new Dimension(300,40));
        usernameField.setHorizontalAlignment(JTextField.CENTER);
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Start button
        JButton startButton = new JButton("Start Quiz");
        startButton.setPreferredSize(new Dimension(200, 50));
        startButton.setMaximumSize(new Dimension(200, 50));
        styleButton(startButton);
        addHoverEffect(startButton);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);


        // Difficulty label
        JLabel difficultyLabel = new JLabel("Select Difficulty:");
        difficultyLabel.setFont(ForestTheme.BUTTON_FONT);
        difficultyLabel.setForeground(ForestTheme.TEXT_COLOR);
        difficultyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Difficulty dropdown
        String[] levels = {"EASY", "HARD"};
        JComboBox<String> difficulty = new JComboBox<>(levels);
        difficulty.setFont(ForestTheme.BUTTON_FONT);
        difficulty.setBackground(ForestTheme.ACCENT_COLOR);
        difficulty.setForeground(ForestTheme.TEXT_COLOR);
        difficulty.setMaximumSize(new Dimension(200, 40));
        difficulty.setAlignmentX(Component.CENTER_ALIGNMENT);


        // Add spacing + components
        content.add(Box.createVerticalStrut(60));
        content.add(iconLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(titleLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(subtitleLabel);
        content.add(Box.createVerticalStrut(40));
        content.add(usernameLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(usernameField);
        content.add(Box.createVerticalStrut(20));
        content.add(difficultyLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(difficulty);
        content.add(Box.createVerticalStrut(40));
        content.add(startButton);

        startPanel.add(content);

        // START BUTTON ACTION
        startButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            if(username.isEmpty()){
                JOptionPane.showMessageDialog(frame,
                        "Please enter your username!",
                        "Missing Name",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String difficultyValue = difficulty.getSelectedItem().toString();
            selectedDifficulty = difficultyValue.equals("EASY") ? Difficulty.EASY : Difficulty.HARD;

            quizManager.startQuiz(username,selectedDifficulty);
            userName=username;

            QuestionScreen();
            cardLayout.show(frame.getContentPane(), "Question");
        });

        startPanel.revalidate();
        startPanel.repaint();
    }


    // QUESTION SCREEN
    public void QuestionScreen() {
        questionPanel.removeAll();
        questionPanel.setLayout(new BorderLayout());

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setBackground(ForestTheme.PRIMARY_BG);

        JLabel questionTitle = new JLabel("Question Time");
        questionTitle.setFont(ForestTheme.TITLE_FONT);
        questionTitle.setForeground(ForestTheme.TEXT_COLOR);
        questionTitle.setOpaque(false);
        questionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        container.add(Box.createVerticalStrut(40));
        container.add(questionTitle);
        container.add(Box.createVerticalStrut(40));  // Gap before first card


        // Get questions from QuizManager
        List<Questions> quizQuestions = quizManager.getCurrentQuizQuestions();
        questionCards.clear();

        int numbering = 1;

        for (Questions q : quizQuestions) {

            String questionText = numbering + ". " + q.getText();
            String[] options;

            if (q instanceof MultipleChoiceQuestion) {
                options = ((MultipleChoiceQuestion) q).getOptions();
            } else if (q instanceof TrueFalseQuestion) {
                options = ((TrueFalseQuestion) q).getTAndFOptions();
            } else {
                continue;
            }

            QuestionCard card = new QuestionCard(questionText, options);
            card.setQuestionObject(q);
            questionCards.add(card);
            container.add(Box.createVerticalStrut(20));
            container.add(card);
            numbering++;
        }


        JButton submitButton = new JButton("Submit Quiz");
        submitButton.setPreferredSize(new Dimension(250, 60));
        submitButton.setMaximumSize(new Dimension(250, 60));
        styleButton(submitButton);
        addHoverEffect(submitButton);
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        container.add(Box.createVerticalStrut(50));
        container.add(submitButton);
        container.add(Box.createVerticalStrut(50));

        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getViewport().setBackground(ForestTheme.PRIMARY_BG);
        scroll.getVerticalScrollBar().setUnitIncrement(20);

        questionPanel.add(scroll, BorderLayout.CENTER);

        submitButton.addActionListener(e -> {
            for (int i = 0; i < questionCards.size(); i++) {
                QuestionCard card = questionCards.get(i);
                String userAnswer = card.getSelectedAnswer();
                quizManager.answerQuestion(i, userAnswer);
            }

            // Save score to database
            quizManager.recordScore();

            int finalScore = quizManager.getCurrentScore();
            ResultsScreen(finalScore);
            cardLayout.show(frame.getContentPane(), "Result");
        });
        questionPanel.revalidate();
        questionPanel.repaint();
    }

    //Results screen
    public void ResultsScreen(int score){

        resultPanel.removeAll();
        resultPanel.setLayout(new GridLayout());

        // Center container
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel resultTitle = new JLabel("Quiz Complete!");
        resultTitle.setFont(ForestTheme.TITLE_FONT);
        resultTitle.setForeground(ForestTheme.TEXT_COLOR);
        resultTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLabel;
        // Username message
        if(score>=7){
            userLabel = new JLabel("Well done, " + userName + "!");
        }

        else if(score>=5){
            userLabel = new JLabel("Not bad, " + userName + "!");
        }
        else{
            userLabel = new JLabel("Terrible, " + userName + ". Try again!");
        }

        userLabel.setFont(ForestTheme.SUBTITLE_FONT);
        userLabel.setForeground(ForestTheme.TEXT_COLOR);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Score message
        JLabel scoreLabel = new JLabel(score + "/" +quizManager.getTotalQuestions());
        scoreLabel.setFont(ForestTheme.TITLE_FONT);
        scoreLabel.setForeground(ForestTheme.ACCENT_COLOR);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //LEADERBOARD
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
        leaderboardPanel.setBorder(BorderFactory.createTitledBorder("Leaderboard"));
        leaderboardPanel.setOpaque(false);

        List<UserScoreRecord> scores = quizManager.getLeaderboard();
        scores.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        // LIMIT results (Top 5)
        scores = scores.stream().limit(5).toList();
        int rank = 1;

        for(UserScoreRecord r : scores) {
            JLabel row = new JLabel(String.format("%d. %s - %d (%s)", rank, r.getUserName().toUpperCase(), r.getScore(), r.getDifficulty()));
            row.setFont(new Font("Segoe UI", Font.BOLD, 20));
            row.setForeground(ForestTheme.TEXT_COLOR);
            row.setAlignmentX(Component.CENTER_ALIGNMENT);

            leaderboardPanel.add(row);
            leaderboardPanel.add(Box.createVerticalStrut(10));
            rank++;
        }
        leaderboardPanel.setOpaque(false);
        leaderboardPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );


        // Back button
        JButton backButton = new JButton("Play Again");
        styleButton(backButton);
        backButton.setPreferredSize(new Dimension(200, 50));
        backButton.setMaximumSize(new Dimension(200, 50));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        backButton.addActionListener(e -> {
            questionCards.clear();
            questionPanel.removeAll();
            StartScreen();
            cardLayout.show(frame.getContentPane(), "Start");
        });

        // Add components
        content.add(Box.createVerticalStrut(60));
        content.add(resultTitle);
        content.add(Box.createVerticalStrut(30));
        content.add(userLabel);
        content.add(Box.createVerticalStrut(30));
        content.add(scoreLabel);
        content.add(Box.createVerticalStrut(60));
        content.add(leaderboardPanel);
        content.add(Box.createVerticalStrut(40));
        content.add(backButton);

        resultPanel.add(content);
        resultPanel.revalidate();
        resultPanel.repaint();


    }

    // STYLE FLAT BUTTON
    private void styleButton(JButton button) {
        button.setFont(ForestTheme.BUTTON_FONT);
        button.setBackground(ForestTheme.BUTTON_COLOR);
        button.setForeground(ForestTheme.TEXT_COLOR);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void addHoverEffect(JButton button) {
        Color normal = ForestTheme.BUTTON_COLOR;
        Color hover = ForestTheme.BUTTON_HOVER;

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(hover); }
            public void mouseExited(MouseEvent e) { button.setBackground(normal); }
        });
    }


    // Main
    public static void main(String[] args) {
        new QuizApp();
    }
}



