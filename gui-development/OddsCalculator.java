// OddsCalculator.java
import java.util.*;

public class OddsCalculator {
    /**
     * @param horses         array of lanes; some entries may be null
     * @param trackCondition current track condition (e.g. "Muddy")
     * @return Map from Horse to its current odds (e.g. 4.25 means 4.25:1)
     */
    public static Map<Horse, Double> calculateOdds(Horse[] horses, String trackCondition) {
        // 1) Compute raw scores
        Map<Horse, Double> scores = new HashMap<>();
        double totalScore = 0;
        for (Horse h : horses) {
            if (h == null) continue;
            double score = computeRawScore(h, trackCondition);
            scores.put(h, score);
            totalScore += score;
        }

        // 2) Normalize into odds
        Map<Horse, Double> odds = new HashMap<>();
        for (Map.Entry<Horse, Double> e : scores.entrySet()) {
            Horse h = e.getKey();
            double prob = e.getValue() / totalScore;
            double odd = prob > 0 ? (1.0 / prob) - 1.0 : Double.POSITIVE_INFINITY;
            odds.put(h, odd);
        }
        return odds;
    }

    private static double computeRawScore(Horse h, String trackCondition) {
        double confidence = h.getConfidence();                     // current confidence
        List<Double> speeds = h.getAvgSpeedHistory();
        double recentSpeed = speeds.isEmpty() ? 0 : speeds.get(speeds.size() - 1);

        // track‐condition affinity: faster best‐time → higher affinity
        int bestTime = h.getBestTimeForCondition(trackCondition);
        double trackAffinity = bestTime > 0 ? 1.0 / bestTime : 0.2;

        // Weighted sum
        return 0.6 * confidence
             + 0.3 * recentSpeed
             + 0.1 * trackAffinity;
    }
}
