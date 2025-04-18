import javax.swing.Timer;
import javax.swing.*;
import java.util.*;
import java.awt.*;

public class Race {
    private int raceLength;
    private int numLanes;
    private Horse[] lanes;
    private Random random = new Random();
    private RaceGUI gui;
    private Timer raceTimer;
    private boolean raceOngoing = true;

    public Race(int raceLength, int numLanes, String trackShape, String weatherCondition, RaceGUI gui) {
        this.raceLength = raceLength;
        this.numLanes = numLanes;
        this.lanes = new Horse[numLanes];
        this.gui = gui;
    }

    public void addHorse(Horse horse, int lane) {
        if (lane >= 0 && lane < numLanes && lanes[lane] == null) {
            lanes[lane] = horse;
        }
    }

    public void startRace() {
        raceOngoing = true;
        raceTimer = new Timer(100, e -> {
            boolean raceFinished = true;
            for (int i = 0; i < numLanes; i++) {
                Horse horse = lanes[i];
                if (horse != null && !horse.hasFallen()) {
                    // Simulate movement: Horses move forward by their speed
                    double speed = horse.getConfidence() * 3;
                    if (Math.random() < 0.95) { // Random chance to move forward
                        horse.moveForward(); // Move the horse forward
                    }

                    // Simulate fall (confidence affects fall chance)
                    if (Math.random() < horse.getConfidence() * 0.07) {
                        horse.fall();
                        horse.setConfidence(Math.max(0, horse.getConfidence() - 0.05)); // Penalty for falling
                    }

                    // Check if the horse has finished or not
                    if (horse.getDistanceTravelled() < raceLength) {
                        raceFinished = false;
                    }
                }
            }

            gui.repaint(); // Repaint the GUI to update the display

            // Stop the race if it's finished
            if (raceFinished) {
                raceTimer.stop(); // Stop the race once it's finished
                displayResults();
            }
        });
        raceTimer.start();
    }

    public void displayRace(Graphics g) {
        for (int i = 0; i < numLanes; i++) {
            Horse horse = lanes[i];
            if (horse != null) {
                String symbol = horse.hasFallen() ? "❌" : String.valueOf(horse.getSymbol());
                int pos = horse.getDistanceTravelled();
                // Draw horse progress on the GUI
                g.drawString("|" + " ".repeat(pos) + symbol, 10, 30 + i * 40); // Adjust positioning
                g.drawString("| " + horse.getName() + " (Confidence: " + String.format("%.2f", horse.getConfidence()) + ")", 10, 50 + i * 40);
            } else {
                g.drawString("|", 10, 30 + i * 40);
            }
        }
        g.drawString("=".repeat(raceLength), 10, 30 + numLanes * 40);
    }

    private void displayResults() {
        // Show the results in the GUI using JOptionPane
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
            JOptionPane.showMessageDialog(gui, "And the winner is… " + winner.getName());
        } else {
            JOptionPane.showMessageDialog(gui, "No horses finished the race.");
        }
    }

    private void clearScreen() {
        // Not necessary for GUI; you can clear the screen if required
    }
}

