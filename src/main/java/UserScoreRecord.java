
public class UserScoreRecord {
    private String userName;
    private int score;
    private Difficulty difficulty;
    private long timestamp;

    public UserScoreRecord(String userName, int score, Difficulty difficulty, long timestamp) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (score < 0) {
            throw new IllegalArgumentException("Score cannot be negative");
        }
        if (difficulty == null) {
            throw new IllegalArgumentException("Difficulty cannot be null or empty");
        }
        if (timestamp <= 0) {
            throw new IllegalArgumentException("Timestamp must be positive");
        }

        this.userName = userName.trim();
        this.score = score;
        this.difficulty = difficulty;
        this.timestamp = timestamp;
    }

    public String getUserName() { return userName; }
    public int getScore() { return score; }
    public Difficulty getDifficulty() { return difficulty; }
    public long getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("UserRecord[username=%s, score=%d, difficulty=%s, timestamp=%d]", userName, score, difficulty, timestamp);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserScoreRecord that = (UserScoreRecord) obj;
        return score == that.score &&
                timestamp == that.timestamp &&
                userName.equals(that.userName) &&
                difficulty == that.difficulty;
    }

    @Override
    public int hashCode() {
        int result = userName.hashCode();
        result = 31 * result + score;
        result = 31 * result + difficulty.hashCode();
        result = 31 * result + Long.hashCode(timestamp);
        return result;
    }
}