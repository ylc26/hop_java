import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreHistorique {
    private static final List<Integer> scores = new ArrayList<>();


    public static void addScore(int score) {
        scores.add(score);
    }

    public static List<Integer> getScores() {
        return new ArrayList<>(scores);
    }

    public static int getBestScore() {
        if (scores.isEmpty()) {
            return 0;
        }
        return Collections.max(scores);
    }
}


