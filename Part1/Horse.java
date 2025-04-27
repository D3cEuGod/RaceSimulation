public class Horse {
    // Fields (private to enforce encapsulation)
    private String name;
    private char symbol;
    private int distanceTravelled;
    private boolean hasFallen;
    private double confidence;

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

    public void moveForward() {
        distanceTravelled += 1;
    }

    public void setConfidence(double newConfidence) {
        if (newConfidence >= 0.0 && newConfidence <= 1.0) {
            confidence = newConfidence;
        }
    }

    public void setSymbol(char newSymbol) {
        symbol = newSymbol;
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
}
