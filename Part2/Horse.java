import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Horse {
    // Core fields
    private String name;
    private char symbol;
    private int distanceTravelled;
    private boolean hasFallen;
    private double confidence;

    // Breed & Equipment
    private String breed;
    private String equipment;

    // Per‐race performance metrics
    private int finishingTimeTicks = -1;
    private int totalWins = 0;

    // Historical data
    private List<Integer> finishingTimeHistory = new ArrayList<>();
    private List<Double>  avgSpeedHistory       = new ArrayList<>();
    private List<Double>  confidenceHistory     = new ArrayList<>();

    // Best times by track condition
    private Map<String, Integer> bestTimesByTrackCondition = new HashMap<>();

    // Constructor
    public Horse(char symbol, String name, double confidence) {
        this.symbol = symbol;
        this.name = name;
        this.confidence = confidence;
        this.distanceTravelled = 0;
        this.hasFallen = false;
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Mutation methods
    public void fall() {
        hasFallen = true;
    }

    public void moveForward(int steps) {
        distanceTravelled += steps;
    }

    public void setConfidence(double newConfidence) {
        if (newConfidence >= 0.0 && newConfidence <= 1.0) {
            confidence = newConfidence;
        }
    }

    public void setSymbol(char newSymbol) {
        symbol = newSymbol;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public void setFinishingTimeTicks(int ticks) {
        this.finishingTimeTicks = ticks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void incrementWins() {
        totalWins++;
    }

    public void setBestTimeForCondition(String condition, int time) {
        bestTimesByTrackCondition.put(condition, time);
    }
    // ──────────────────────────────────────────────────────────────────────────

    // ──────────────────────────────────────────────────────────────────────────
    // Per‐race recording
    /** Call once after each race to record this race’s results. */
    public void recordRaceResult(int finishingTicks, int distance, double confAtEnd) {
        finishingTimeHistory.add(finishingTicks);
        avgSpeedHistory.add(distance / (double)Math.max(1, finishingTicks));
        confidenceHistory.add(confAtEnd);
    }
    // ──────────────────────────────────────────────────────────────────────────

    // ──────────────────────────────────────────────────────────────────────────
    // Accessors
    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }

    public int getDistanceTravelled() {
        return distanceTravelled;
    }

    public boolean hasFallen() {
        return hasFallen;
    }

    public double getConfidence() {
        return confidence;
    }

    public String getBreed() {
        return breed;
    }

    public String getEquipment() {
        return equipment;
    }

    public int getFinishingTimeTicks() {
        return finishingTimeTicks;
    }

    public int getTotalWins() {
        return totalWins;
    }

    // Historical getters
    public List<Integer> getFinishingTimeHistory() {
        return finishingTimeHistory;
    }

    public List<Double> getAvgSpeedHistory() {
        return avgSpeedHistory;
    }

    public List<Double> getConfidenceHistory() {
        return confidenceHistory;
    }

    // Track‐condition records
    public int getBestTimeForCondition(String condition) {
        return bestTimesByTrackCondition.getOrDefault(condition, -1);
    }

    public Map<String, Integer> getAllBestTimes() {
        return bestTimesByTrackCondition;
    }

    /** Reset per‐race fields (but keep history and best times). */
    public void resetForRace() {
        this.distanceTravelled    = 0;
        this.hasFallen            = false;
        this.finishingTimeTicks   = -1;
        // confidence remains as last‐race end confidence
    }
    // ──────────────────────────────────────────────────────────────────────────
}
