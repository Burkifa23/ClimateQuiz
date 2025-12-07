import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class QuizApp {

    private JFrame frame;
    private CardLayout cardLayout;

    // 3 screens
    private JPanel startPanel;
    private JPanel questionPanel;
    private JPanel resultPanel;

    // THE BRAIN
    private QuizManager quizManager;

    // Track every card created to retrieve answers later
    private List<QuestionCard> activeCards = new ArrayList<>();

    // CONSTRUCTOR
    public QuizApp() {
        // Initialize the Manager
        quizManager = new QuizManager();
        quizManager.loadQuestions(); // Load data from your bank

        frame = new JFrame("Climate Change Quiz");
        frame.setSize(650, 700); // Increased height to fit Leaderboard
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        frame.setLayout(cardLayout);

        startPanel = new JPanel();
        questionPanel = new JPanel();
        resultPanel = new JPanel();

        setupStartScreen();
        // Other screens are setup dynamically

        frame.add(startPanel, "Start");
        frame.add(questionPanel, "Question");
        frame.add(resultPanel, "Result");

        cardLayout.show(frame.getContentPane(), "Start");
        frame.setVisible(true);
    }



    // UI COMPONENTS


    class CircleButton extends JToggleButton {
        private Color normalColor = new Color(240, 240, 240);
        private Color selectedColor = new Color(0, 150, 136);
        private String letter;

        public CircleButton(String letter) {
            this.letter = letter;
            setPreferredSize(new Dimension(32, 32));
            setMinimumSize(new Dimension(32, 32));
            setMaximumSize(new Dimension(32, 32));
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
            g2.setFont(new Font("Arial", Font.BOLD, 22));
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(letter)) / 2;
            int y = (getHeight() + fm.getAscent()) / 2 - 3;
            g2.drawString(letter, x, y);
            g2.dispose();
        }
    }

    class QuestionCard extends JPanel {
        private String selectedAnswer = null;
        private int questionIndex;

        public QuestionCard(int index, String questionText, String[] options) {
            this.questionIndex = index;

            setOpaque(false);
            int height = (options.length == 2) ? 200 : 280;
            setPreferredSize(new Dimension(520, height));
            setMaximumSize(new Dimension(520, height));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JPanel background = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(Color.WHITE);
                    g.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                }
            };
            background.setOpaque(false);
            background.setLayout(new BoxLayout(background, BoxLayout.Y_AXIS));
            background.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

            JTextArea qLabel = new JTextArea((index + 1) + ". " + questionText);
            qLabel.setFont(new Font("Arial", Font.BOLD, 18));
            qLabel.setLineWrap(true);
            qLabel.setWrapStyleWord(true);
            qLabel.setEditable(false);
            qLabel.setOpaque(false);
            qLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            background.add(qLabel);
            background.add(Box.createVerticalStrut(10));

            String[] letters = {"A", "B", "C", "D"};
            ButtonGroup group = new ButtonGroup();
            int optionSpacing = (options.length == 2) ? 4 : 8;

            for (int i = 0; i < options.length; i++) {
                final String answerText = options[i];
                String letter = (i < 4) ? letters[i] : "-";

                JPanel row = new JPanel();
                row.setOpaque(false);
                row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));

                CircleButton btn = new CircleButton(letter);
                group.add(btn);

                JLabel optLabel = new JLabel(answerText);
                optLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                optLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

                ActionListener selectionAction = e -> {
                    btn.setSelected(true);
                    selectedAnswer = answerText;
                };

                btn.addActionListener(selectionAction);
                row.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        selectionAction.actionPerformed(null);
                    }
                });

                row.add(btn);
                row.add(optLabel);
                row.add(Box.createHorizontalGlue());

                background.add(row);
                background.add(Box.createVerticalStrut(1));
                background.add(Box.createVerticalStrut(optionSpacing));
            }
            add(background);
        }

        public String getSelectedAnswer() {
            return selectedAnswer;
        }

        public int getQuestionIndex() {
            return questionIndex;
        }
    }


    // SCREENS


    public void setupStartScreen() {
        startPanel.removeAll();
        startPanel.setLayout(new BorderLayout());
        startPanel.setBackground(new Color(20, 126, 21));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titleLabel = new JLabel("Climate Change Quiz");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Test your knowledge & learn something new!");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usernameLabel = new JLabel("Enter username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Arial", Font.PLAIN, 20));
        usernameField.setMaximumSize(new Dimension(250,35));
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel difficultyLabel = new JLabel("Select Difficulty:");
        difficultyLabel.setFont(new Font("Arial", Font.BOLD, 22));
        difficultyLabel.setForeground(Color.WHITE);
        difficultyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] levels = {"EASY", "HARD"};
        JComboBox<String> difficultyBox = new JComboBox<>(levels);
        difficultyBox.setFont(new Font("Arial", Font.BOLD, 18));
        difficultyBox.setMaximumSize(new Dimension(250, 40));
        difficultyBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton startButton = new JButton("Start Quiz");
        styleButton(startButton);
        addHoverEffect(startButton);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(titleLabel);
        content.add(Box.createVerticalStrut(15));
        content.add(subtitleLabel);
        content.add(Box.createVerticalStrut(30));
        content.add(usernameLabel);
        content.add(usernameField);
        content.add(Box.createVerticalStrut(20));
        content.add(difficultyLabel);
        content.add(difficultyBox);
        content.add(Box.createVerticalStrut(40));
        content.add(startButton);

        startPanel.add(content, BorderLayout.CENTER);

        startButton.addActionListener(e -> {
            String username = usernameField.getText().trim();

            if(username.isEmpty()){
                JOptionPane.showMessageDialog(frame, "Please enter your username!", "Missing Name", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String difficultyValue = difficultyBox.getSelectedItem().toString();
            Difficulty selectedDifficulty = difficultyValue.equals("EASY") ? Difficulty.EASY : Difficulty.HARD;

            try {
                quizManager.startQuiz(username, selectedDifficulty);
                setupQuestionScreen();
                cardLayout.show(frame.getContentPane(), "Question");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });
    }

    public void setupQuestionScreen() {
        questionPanel.removeAll();
        activeCards.clear();

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(new Color(20, 126, 21));

        JLabel questionTitle = new JLabel("Answer all Questions");
        questionTitle.setFont(new Font("Times New Roman", Font.BOLD, 45));
        questionTitle.setForeground(Color.WHITE);
        questionTitle.setOpaque(false);
        questionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        container.add(Box.createVerticalStrut(20));
        container.add(questionTitle);
        container.add(Box.createVerticalStrut(20));

        List<Questions> questions = quizManager.getCurrentQuizQuestions();

        for (int i = 0; i < questions.size(); i++) {
            Questions q = questions.get(i);
            String[] options;
            if (q instanceof MultipleChoiceQuestion) {
                options = ((MultipleChoiceQuestion) q).getOptions();
            } else {
                options = new String[]{"True", "False"};
            }

            QuestionCard card = new QuestionCard(i, q.getText(), options);
            activeCards.add(card);

            container.add(card);
            container.add(Box.createVerticalStrut(30));
        }

        JButton submitButton = new JButton("Submit Quiz");
        styleButton(submitButton);
        addHoverEffect(submitButton);
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        container.add(submitButton);
        container.add(Box.createVerticalStrut(30));

        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);

        questionPanel.setLayout(new BorderLayout());
        questionPanel.add(scroll, BorderLayout.CENTER);

        submitButton.addActionListener(e -> {
            for (QuestionCard card : activeCards) {
                String ans = card.getSelectedAnswer();
                if (ans != null) {
                    quizManager.answerQuestion(card.getQuestionIndex(), ans);
                }
            }
            quizManager.recordScore();
            setupResultsScreen(); // This triggers the leaderboard load
            cardLayout.show(frame.getContentPane(), "Result");
        });
    }

    public void setupResultsScreen() {
        resultPanel.removeAll();
        resultPanel.setLayout(new BorderLayout());
        resultPanel.setBackground(new Color(20, 126, 21));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel resultTitle = new JLabel("Quiz Results");
        resultTitle.setFont(new Font("Times New Roman", Font.BOLD, 45));
        resultTitle.setForeground(Color.WHITE);
        resultTitle.setOpaque(false);
        resultTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel userLabel = new JLabel("Well done, " + quizManager.getUserName() + "!");
        userLabel.setFont(new Font("Arial", Font.BOLD, 30));
        userLabel.setForeground(Color.WHITE);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreLabel = new JLabel("Your Score: " + quizManager.getCurrentScore() + "/" + quizManager.getTotalQuestions());
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 35));
        scoreLabel.setForeground(Color.YELLOW);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // LEADERBOARD LOGIC STARTS HERE
        JLabel leaderTitle = new JLabel("Top Scores ");
        leaderTitle.setFont(new Font("Arial", Font.BOLD, 22));
        leaderTitle.setForeground(Color.WHITE);
        leaderTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel leaderList = new JPanel();
        leaderList.setLayout(new BoxLayout(leaderList, BoxLayout.Y_AXIS));
        leaderList.setOpaque(false);
        leaderList.setAlignmentX(Component.CENTER_ALIGNMENT);

        List<UserScoreRecord> scores = quizManager.getLeaderboard();
        if (scores.isEmpty()) {
            JLabel empty = new JLabel("No scores yet. You're the first!");
            empty.setForeground(Color.WHITE);
            empty.setFont(new Font("Arial", Font.ITALIC, 16));
            leaderList.add(empty);
        } else {
            int count = 0;
            for (UserScoreRecord r : scores) {
                if (count >= 5) break; // Limit to Top 5 to fit screen
                JLabel row = new JLabel(String.format("%d. %s : %d (%s)",
                        count + 1, r.getUserName(), r.getScore(), r.getDifficulty()));
                row.setFont(new Font("Arial", Font.PLAIN, 18));
                row.setForeground(Color.WHITE);
                row.setAlignmentX(Component.CENTER_ALIGNMENT);
                leaderList.add(row);
                leaderList.add(Box.createVerticalStrut(5));
                count++;
            }
        }


        JButton backButton = new JButton("Play Again");
        styleButton(backButton);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        backButton.addActionListener(e -> {
            setupStartScreen();
            cardLayout.show(frame.getContentPane(), "Start");
        });

        content.add(Box.createVerticalStrut(30));
        content.add(resultTitle);
        content.add(Box.createVerticalStrut(20));
        content.add(userLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(scoreLabel);
        content.add(Box.createVerticalStrut(30));
        content.add(leaderTitle);
        content.add(Box.createVerticalStrut(10));
        content.add(leaderList);
        content.add(Box.createVerticalStrut(30));
        content.add(backButton);

        resultPanel.add(content, BorderLayout.CENTER);
        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 20));
        button.setBackground(new Color(0, 150, 136));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void addHoverEffect(JButton btn) {
        Color normal = new Color(0, 150, 136);
        Color hover = new Color(0, 170, 150);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(MouseEvent e) { btn.setBackground(normal); }
        });
    }

    public static void main(String[] args) {
        new QuizApp();
    }
}