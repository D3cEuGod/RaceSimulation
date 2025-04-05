public class Horse {
    private String name;
    private char symbol;
    private boolean eliminated;
    private double confidence; // Value between 0 and 1

    public Horse(String name, char symbol, double confidence) {
        this.name = name;
        this.symbol = symbol;
        this.eliminated = false;
        setConfidence(confidence);
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = Math.max(0, Math.min(1, confidence)); // Ensure within bounds
    }

    public void eliminate() {
        this.eliminated = true;
    }

    public void winRace() {
        setConfidence(confidence + 0.05); // Slightly increase confidence
    }

    public void fall() {
        eliminate();
        setConfidence(confidence - 0.05); // Slightly decrease confidence
    }

    public double getSpeed() {
        return 5 + (confidence * 5); // Example: Base speed + confidence impact
    }

    @Override
    public String toString() {
        return name + " (" + symbol + ") - " + (eliminated ? "Eliminated" : "Active") + ", Confidence: " + confidence;
    }
}
