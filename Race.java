import java.util.*;

public class Race {
    private int raceLength;
    private int numLanes;
    private Horse[] lanes;
    private Random random = new Random();

    public Race(int raceLength, int numLanes) {
        this.raceLength = raceLength;
        this.numLanes = numLanes;
        this.lanes = new Horse[numLanes];
    }

    public void addHorse(Horse horse, int lane) {
        if (lane >= 0 && lane < numLanes && lanes[lane] == null) {
            lanes[lane] = horse;
        }
    }

    public void startRace() {
        boolean raceOngoing = true;

        while (raceOngoing) {
            clearScreen();
            raceOngoing = false;

            for (int i = 0; i < numLanes; i++) {
                Horse horse = lanes[i];
                if (horse != null && !horse.hasFallen()) {
                    // Move forward
                    if (Math.random() < horse.getConfidence()) {
                        horse.moveForward();
                    }

                    // Simulate fall (confidence affects risk)
                    if (Math.random() < horse.getConfidence() * 0.1) {
                        horse.fall();
                        horse.setConfidence(Math.max(0, horse.getConfidence() - 0.05)); // optional penalty
                    }

                    // Check if horse reached the finish line
                    raceOngoing |= horse.getDistanceTravelled() < raceLength;
                }
            }

            displayRace(); // No argument now
            try {
                Thread.sleep(500); // Animation delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        displayResults();
    }
    


    private void displayRace() {
        for (int i = 0; i < numLanes; i++) {
            Horse horse = lanes[i];
            if (horse != null) {
                String symbol = horse.hasFallen() ? "❌" : String.valueOf(horse.getSymbol());
                int pos = horse.getDistanceTravelled();
                System.out.println("|" + " ".repeat(pos) + symbol);
                System.out.println("| " + horse.getName() + " (Current confidence " + String.format("%.2f", horse.getConfidence()) + ")");
            } else {
                System.out.println("|");
            }
        }
        System.out.println("=".repeat(raceLength));
    }


    private void displayResults() {
        System.out.println("Race Finished! Results:");

        Horse winner = null;
        int maxDistance = -1;

        for (Horse horse : lanes) {
            if (horse != null && !horse.hasFallen()) {
                int distance = horse.getDistanceTravelled();
                if (distance > maxDistance) {
                    winner = horse;
                    maxDistance = distance;
                }
            }
        }

        if (winner != null) {
            winner.setConfidence(Math.min(1.0, winner.getConfidence() + 0.05)); // Optional reward
            System.out.println("And the winner is… " + winner.getName() + "!");
        } else {
            System.out.println("No horses finished the race.");
        }
    }


    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
