import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Enumeration;

public class QuizApp {

    private JFrame frame;
    private CardLayout cardLayout;

    // 3 screens
    private JPanel startPanel;
    private JPanel questionPanel;
    private JPanel resultPanel;

    // THE BRAIN (Your Manager)
    private QuizManager quizManager;

    // CONSTRUCTOR
    public QuizApp() {
        // Initialize the Manager
        quizManager = new QuizManager();
        // Load data from Questions bank
        quizManager.loadQuestions();

        frame = new JFrame("Climate Change Quiz");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        frame.setLayout(cardLayout);

        startPanel = new JPanel();
        questionPanel = new JPanel();
        resultPanel = new JPanel();

        setupStartScreen();
        setupResultScreen();

        frame.add(startPanel, "Start");
        frame.add(questionPanel, "Question");
        frame.add(resultPanel, "Result");

        cardLayout.show(frame.getContentPane(), "Start");
        frame.setVisible(true);
    }


    // UI COMPONENTS


    class CircleButton extends JToggleButton {
        private Color normalColor = new Color(240, 240, 240);
        private Color selectedColor = new Color(0, 150, 136); // Teal
        private String letter;

        public CircleButton(String letter) {
            this.letter = letter;
            setPreferredSize(new Dimension(32, 32));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isSelected() ? selectedColor : normalColor);
            g2.fillOval(0, 0, getWidth(), getHeight());
            g2.setColor(isSelected() ? Color.WHITE : Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(letter)) / 2;
            int y = (getHeight() + fm.getAscent()) / 2 - 3;
            g2.drawString(letter, x, y);
            g2.dispose();
        }
    }

    class QuestionCard extends JPanel {
        private int questionIndex;
        private ButtonGroup group;

        public QuestionCard(int index, String questionText, String[] options) {
            this.questionIndex = index;
            this.group = new ButtonGroup();

            setOpaque(false);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

            // Background Card
            JPanel bg = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(Color.WHITE);
                    g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                }
            };
            bg.setOpaque(false);
            bg.setLayout(new BoxLayout(bg, BoxLayout.Y_AXIS));
            bg.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            // Question Text
            JTextArea qLabel = new JTextArea((index + 1) + ". " + questionText);
            qLabel.setFont(new Font("Arial", Font.BOLD, 18));
            qLabel.setLineWrap(true);
            qLabel.setWrapStyleWord(true);
            qLabel.setEditable(false);
            qLabel.setOpaque(false);
            bg.add(qLabel);
            bg.add(Box.createVerticalStrut(15));

            // Options
            String[] letters = {"A", "B", "C", "D"};
            for (int i = 0; i < options.length; i++) {
                final String answerText = options[i];
                String letter = (i < 4) ? letters[i] : "-";

                JPanel row = new JPanel();
                row.setOpaque(false);
                row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
                row.setAlignmentX(Component.LEFT_ALIGNMENT);

                CircleButton btn = new CircleButton(letter);
                btn.setActionCommand(answerText); // Store the answer text in the button
                group.add(btn);

                JLabel optLabel = new JLabel(answerText);
                optLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                optLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

                row.add(btn);
                row.add(optLabel);

                // Allow clicking the text to select
                row.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        btn.setSelected(true);
                    }
                });

                bg.add(row);
                bg.add(Box.createVerticalStrut(5));
            }

            add(bg);
            add(Box.createVerticalStrut(15)); // Spacing between cards
        }

        // Helper to get the selected answer from this card
        public String getSelectedAnswer() {
            ButtonModel model = group.getSelection();
            return (model != null) ? model.getActionCommand() : null;
        }
    }


    // SCREENS


    private void setupStartScreen() {
        startPanel.setLayout(new GridBagLayout());
        startPanel.setBackground(new Color(20, 126, 21)); // Forest Green

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Climate Change Quiz");
        title.setFont(new Font("Arial", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username Input
        JTextField nameField = new JTextField(15);
        nameField.setMaximumSize(new Dimension(200, 30));
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));

        // Difficulty Selector
        String[] levels = {"EASY", "HARD"};
        JComboBox<String> difficultyBox = new JComboBox<>(levels);
        difficultyBox.setMaximumSize(new Dimension(200, 30));

        JButton startBtn = new JButton("Start Quiz");
        styleButton(startBtn);

        startBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String diffStr = (String) difficultyBox.getSelectedItem();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter your name!");
                return;
            }

            Difficulty diff = diffStr.equals("EASY") ? Difficulty.EASY : Difficulty.HARD;

            // INTEGRATION: Call your manager!
            try {
                quizManager.startQuiz(name, diff);
                setupQuestionScreen(); // Build the questions based on selection
                cardLayout.show(frame.getContentPane(), "Question");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        content.add(title);
        content.add(Box.createVerticalStrut(40));
        content.add(new JLabel("Enter Name:")).setForeground(Color.WHITE);
        content.add(nameField);
        content.add(Box.createVerticalStrut(20));
        content.add(difficultyBox);
        content.add(Box.createVerticalStrut(40));
        content.add(startBtn);

        startPanel.add(content);
    }

    private void setupQuestionScreen() {
        questionPanel.removeAll(); // Clear old questions
        questionPanel.setLayout(new BorderLayout());

        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBackground(new Color(230, 240, 230)); // Light background

        // INTEGRATION: Get questions from quiz manager
        List<Questions> questions = quizManager.getCurrentQuizQuestions();

        // Keep track of card references so we can get answers later
        java.util.List<QuestionCard> cards = new java.util.ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            Questions q = questions.get(i);

            // Handle Polymorphism for options
            String[] options;
            if (q instanceof MultipleChoiceQuestion) {
                options = ((MultipleChoiceQuestion) q).getOptions();
            } else {
                // True/False
                options = new String[]{"True", "False"};
            }

            QuestionCard card = new QuestionCard(i, q.getText(), options);
            cards.add(card);
            listContainer.add(card);
        }

        JButton submitBtn = new JButton("Submit Quiz");
        styleButton(submitBtn);

        submitBtn.addActionListener(e -> {
            // INTEGRATION: Process all answers
            int answeredCount = 0;
            for (QuestionCard card : cards) {
                String answer = card.getSelectedAnswer();
                if (answer != null) {
                    // Tell manager the answer
                    quizManager.answerQuestion(card.questionIndex, answer);
                    answeredCount++;
                }
            }

            if (answeredCount < questions.size()) {
                int choice = JOptionPane.showConfirmDialog(frame,
                        "You haven't answered all questions. Submit anyway?",
                        "Confirm Submit", JOptionPane.YES_NO_OPTION);
                if (choice != JOptionPane.YES_OPTION) return;
            }

            // Save and Show Results
            quizManager.recordScore();
            showResults();
        });

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(230, 240, 230));
        btnPanel.add(submitBtn);

        JScrollPane scroll = new JScrollPane(listContainer);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        questionPanel.add(scroll, BorderLayout.CENTER);
        questionPanel.add(btnPanel, BorderLayout.SOUTH);
    }

    private void setupResultScreen() {
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(Color.WHITE);
        // Content added dynamically in showResults()
    }

    private void showResults() {
        resultPanel.removeAll();

        JLabel scoreLabel = new JLabel("Your Score: " + quizManager.getCurrentScore() + " / " + quizManager.getTotalQuestions());
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 30));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel diffLabel = new JLabel("Difficulty: " + quizManager.getQuestion(0).getDifficultyLevel());
        diffLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        diffLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // LEADERBOARD
        JPanel leaderboardPanel = new JPanel();
        leaderboardPanel.setLayout(new BoxLayout(leaderboardPanel, BoxLayout.Y_AXIS));
        leaderboardPanel.setBorder(BorderFactory.createTitledBorder("Leaderboard"));

        List<UserScoreRecord> scores = quizManager.getLeaderboard();
        for(UserScoreRecord r : scores) {
            JLabel row = new JLabel(String.format("%s: %d (%s)", r.getUserName(), r.getScore(), r.getDifficulty()));
            leaderboardPanel.add(row);
        }

        JButton restartBtn = new JButton("Play Again");
        styleButton(restartBtn);
        restartBtn.addActionListener(e -> cardLayout.show(frame.getContentPane(), "Start"));

        resultPanel.add(Box.createVerticalStrut(50));
        resultPanel.add(scoreLabel);
        resultPanel.add(diffLabel);
        resultPanel.add(Box.createVerticalStrut(30));
        resultPanel.add(leaderboardPanel);
        resultPanel.add(Box.createVerticalStrut(30));
        resultPanel.add(restartBtn);

        cardLayout.show(frame.getContentPane(), "Result");
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBackground(new Color(0, 150, 136));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public static void main(String[] args) {
        new QuizApp();
    }
}