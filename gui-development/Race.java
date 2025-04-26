import javax.swing.*;
import java.awt.*;

public class Race {
    private int raceLength;
    private int numLanes;
    private Horse[] lanes;
    private boolean raceOngoing = true;
    private String weatherCondition; // 🆕 Added

    public Race(int raceLength, int numLanes, String trackShape, String weatherCondition, RaceGUI gui) {
        this.raceLength = raceLength;
        this.numLanes = numLanes;
        this.lanes = new Horse[numLanes];
        this.weatherCondition = weatherCondition; // 🆕 Save the weather condition
    }

    public void addHorse(Horse horse, int lane) {
        if (lane >= 0 && lane < numLanes && lanes[lane] == null) {
            lanes[lane] = horse;
        }
    }

    public void advanceRaceTick() {
        raceOngoing = false; // Assume race is over unless proven otherwise

        for (int i = 0; i < numLanes; i++) {
            Horse horse = lanes[i];
            if (horse != null && !horse.hasFallen()) {
                if (Math.random() < 0.95) {
                    int step = (int)(horse.getConfidence() * 3 + 1);

                    // 🌧️ Weather effect on movement
                    if ("Muddy".equalsIgnoreCase(weatherCondition)) {
                        step = Math.max(1, step - 1); // Slower movement on muddy track
                    }

                    horse.moveForward(step);
                }

                double fallChance = horse.getConfidence() * 0.03;

                // ❄️ Weather effect on fall chance
                if ("Icy".equalsIgnoreCase(weatherCondition)) {
                    fallChance *= 2; // Higher risk of falling on ice
                }

                if (Math.random() < fallChance) {
                    horse.fall();
                    horse.setConfidence(Math.max(0, horse.getConfidence() - 0.05));
                }

                if (horse.getDistanceTravelled() < raceLength) {
                    raceOngoing = true;
                }
            }
        }
    }

    public boolean isRaceOngoing() {
        return raceOngoing;
    }

    public void displayRace(Graphics g) {
        for (int i = 0; i < numLanes; i++) {
            Horse horse = lanes[i];
            if (horse != null) {
                String symbol = horse.hasFallen() ? "❌" : String.valueOf(horse.getSymbol());
                int pos = horse.getDistanceTravelled();
                g.drawString("|" + " ".repeat(pos) + symbol, 10, 30 + i * 40);
                g.drawString("| " + horse.getName() + " (Confidence: " +
                             String.format("%.2f", horse.getConfidence()) + ")", 10, 50 + i * 40);
            } else {
                g.drawString("|", 10, 30 + i * 40);
            }
        }
        g.drawString("=".repeat(raceLength), 10, 30 + numLanes * 40);
    }

    public void displayResultsInViewer(Component parent) {
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
            winner.setConfidence(Math.min(1.0, winner.getConfidence() + 0.05));
            JOptionPane.showMessageDialog(parent, "🏆 The winner is: " + winner.getName());
        } else {
            JOptionPane.showMessageDialog(parent, "No horses finished the race!");
        }
    }

    public Horse getHorseAtLane(int lane) {
        if (lane >= 0 && lane < numLanes) {
            return lanes[lane];
        }
        return null;
    }
}

