public class TestHorse {
    public static void main(String[] args) {
        // Create a horse with symbol ♘, name "PIPPI LONGSTOCKING", and confidence 0.6
        Horse horse1 = new Horse('♘', "PIPPI LONGSTOCKING", 0.6);

        // Test the initial state
        System.out.println("Initial State:");
        System.out.println("Name: " + horse1.getName());
        System.out.println("Symbol: " + horse1.getSymbol());
        System.out.println("Confidence: " + horse1.getConfidence());
        System.out.println("Distance Travelled: " + horse1.getDistanceTravelled());
        System.out.println("Has Fallen: " + horse1.hasFallen());
        System.out.println();

        // Test moving forward
        horse1.moveForward(); // Move the horse forward by 1 unit
        System.out.println("After moving forward:");
        System.out.println("Distance Travelled: " + horse1.getDistanceTravelled());
        System.out.println();

        // Test falling
        horse1.fall(); // Make the horse fall
        System.out.println("After falling:");
        System.out.println("Has Fallen: " + horse1.hasFallen());
        System.out.println();

        // Test going back to start
        horse1.goBackToStart(); // Reset the horse's position
        System.out.println("After going back to start:");
        System.out.println("Distance Travelled: " + horse1.getDistanceTravelled());
        System.out.println("Has Fallen: " + horse1.hasFallen());
        System.out.println();

        // Test setting confidence (valid and invalid cases)
        horse1.setConfidence(0.8); // Valid confidence
        System.out.println("After setting confidence to 0.8:");
        System.out.println("Confidence: " + horse1.getConfidence());

        horse1.setConfidence(1.2); // Invalid confidence (greater than 1)
        System.out.println("After attempting to set confidence to 1.2:");
        System.out.println("Confidence: " + horse1.getConfidence()); // Should still be 0.8

        horse1.setConfidence(-0.1); // Invalid confidence (less than 0)
        System.out.println("After attempting to set confidence to -0.1:");
        System.out.println("Confidence: " + horse1.getConfidence()); // Should still be 0.8
    }
}
