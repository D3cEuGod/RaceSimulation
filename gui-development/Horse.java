import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Horse {
    // Fields (private to enforce encapsulation)
    private String name;
    private char symbol;
    private int distanceTravelled;
    private boolean hasFallen;
    private double confidence;
    private String breed;
    private String equipment;
    private int finishingTimeTicks = -1; // How many ticks to finish
    private int totalWins = 0; // How many races won
    private List<Double> confidenceHistory = new ArrayList<>(); // Store confidence after each race
    private Map<String, Integer> bestTimesByTrackCondition = new HashMap<>();


    // Constructor
    public Horse(char horseSymbol, String horseName, double horseConfidence) {
        this.symbol = horseSymbol;
        this.name = horseName;
        this.confidence = horseConfidence;
        this.distanceTravelled = 0;
        this.hasFallen = false;
    }

    // Mutator (setter) methods
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

    public void setBestTimeForCondition(String condition, int time) {
    	bestTimesByTrackCondition.put(condition, time);
    }

    // Accessor (getter) methods
    public double getConfidence() {
        return confidence;
    }

    public int getDistanceTravelled() {
        return distanceTravelled;
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }

    public boolean hasFallen() {
        return hasFallen;
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

    public void incrementWins() {
    	totalWins++;
    }

    public int getTotalWins() {
    	return totalWins;
    }

    public void recordConfidence() {
    	confidenceHistory.add(confidence);
    }

    public List<Double> getConfidenceHistory() {
    	return confidenceHistory;
    }

    public int getBestTimeForCondition(String condition) {
    	return bestTimesByTrackCondition.getOrDefault(condition, -1);
    }

    public Map<String, Integer> getAllBestTimes() {
    	return bestTimesByTrackCondition;
    }
}
