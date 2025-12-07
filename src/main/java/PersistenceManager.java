import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenceManager {
    private String DB_URL;
    private static String TABLE_NAME = "quiz_scores";

    // CONSTRUCTORS
    public PersistenceManager() {
        this.DB_URL = "jdbc:sqlite:quiz_scores.db";
    }

    public PersistenceManager(String dbFilename) {
        if (dbFilename == null || dbFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("Database file name cannot be null or empty");
        }
        this.DB_URL = "jdbc:sqlite:" + dbFilename;
    }

    // METHODS OF INITIALIZATION
    public void initializeDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " user_name TEXT NOT NULL," +
                " score INTEGER NOT NULL," +
                " difficulty TEXT NOT NULL," +
                " timestamp INTEGER NOT NULL" +
                ")";

        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();
            stmt.execute(createTableSQL);
            System.out.println("Database initialized successfully at: " + DB_URL);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database: " + e.getMessage(), e);
        } finally {
            closeQuietly(stmt);
            closeQuietly(conn);
        }
    }

    // METHODS OF SAVE SCORE
    public void saveScore(UserScoreRecord record) {
        if (record == null) {
            throw new IllegalArgumentException("Score record cannot be null");
        }
        String insertSQL = "INSERT INTO " + TABLE_NAME +
                "(user_name, score, difficulty, timestamp) VALUES(?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL);
            pstmt = conn.prepareStatement(insertSQL);
            pstmt.setString(1, record.getUserName());
            pstmt.setInt(2, record.getScore());
            pstmt.setString(3, record.getDifficulty().name());
            pstmt.setLong(4, record.getTimestamp());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Failed to insert score record - no rows affected");
            }
            System.out.println("Score saved : " + record);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save score: " + e.getMessage(), e);
        } finally {
            closeQuietly(pstmt);
            closeQuietly(conn);
        }
    }

    // LOAD SCORES
    public List<UserScoreRecord> loadAllScores() {
        List<UserScoreRecord> scores = new ArrayList<>();
        String selectSQL = "SELECT user_name, score, difficulty, timestamp " +
                "FROM " + TABLE_NAME + " " +
                "ORDER BY score DESC, timestamp DESC";

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(selectSQL);

            while (rs.next()) {
                String userName = rs.getString("user_name");
                int score = rs.getInt("score");
                String difficultyStr = rs.getString("difficulty");
                long timestamp = rs.getLong("timestamp");

                Difficulty difficulty = Difficulty.valueOf(difficultyStr);
                UserScoreRecord record = new UserScoreRecord(userName, score, difficulty, timestamp);
                scores.add(record);
            }
            System.out.println("Loaded " + scores.size() + " score records");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load scores : " + e.getMessage(), e);
        } finally {
            closeQuietly(rs);
            closeQuietly(stmt);
            closeQuietly(conn);
        }
        return scores;
    }

    // HELPER METHODS
    private void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                System.err.println("Error closing resource: " + e.getMessage());
            }
        }
    }

    // UTILITY TESTING METHODS (Fixed typos here!)
    public void clearAllScores() {
        String deleteSQL = "DELETE FROM " + TABLE_NAME; // Fixed DELTE

        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();
            int deleted = stmt.executeUpdate(deleteSQL);
            System.out.println("Cleared " + deleted + " score records");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear scores: " + e.getMessage(), e);
        } finally {
            closeQuietly(stmt);
            closeQuietly(conn);
        }
    }

    public String getDatabaseUrl() {
        return DB_URL;
    }
}