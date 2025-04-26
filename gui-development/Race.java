import javax.swing.*;
import java.awt.*;

public class Race {
    private int raceLength;
    private int numLanes;
    private Horse[] lanes;
    private boolean raceOngoing = true;
    private String weatherCondition;
    private int raceTicks = 0; // 🆕 Count how many ticks the race has taken

    public Race(int raceLength, int numLanes, String trackShape, String weatherCondition, RaceGUI gui) {
        this.raceLength = raceLength;
        this.numLanes = numLanes;
        this.lanes = new Horse[numLanes];
        this.weatherCondition = weatherCondition;
    }

    public void addHorse(Horse horse, int lane) {
        if (lane >= 0 && lane < numLanes && lanes[lane] == null) {
            lanes[lane] = horse;
        }
    }

    public void advanceRaceTick() {
        raceTicks++; // 🆕 Increment race ticks

        raceOngoing = false; // Assume race is over unless a horse still running

        for (int i = 0; i < numLanes; i++) {
            Horse horse = lanes[i];
            if (horse != null && !horse.hasFallen()) {
                if (Math.random() < 0.95) {
                    int step = (int)(horse.getConfidence() * 3 + 1);

                    // Weather effects
                    if ("Muddy".equalsIgnoreCase(weatherCondition)) {
                        step = Math.max(1, step - 1);
                    }

                    // Breed effects
                    if ("Arabian".equals(horse.getBreed())) {
                        step += 1;
                    } else if ("Shire".equals(horse.getBreed())) {
                        step = Math.max(1, step - 1);
                    }

                    // Equipment effects
                    if ("Speed Shoes".equals(horse.getEquipment())) {
                        step += 1;
                    } else if ("Heavy Armor".equals(horse.getEquipment())) {
                        step = Math.max(1, step - 1);
                    }

                    horse.moveForward(step);
                }

                double fallChance = horse.getConfidence() * 0.03;

                // Weather effect on falling
                if ("Icy".equalsIgnoreCase(weatherCondition)) {
                    fallChance *= 2;
                }

                // Equipment effects on falling
                if ("Speed Shoes".equals(horse.getEquipment())) {
                    fallChance *= 1.2;
                } else if ("Heavy Armor".equals(horse.getEquipment())) {
                    fallChance *= 0.8;
                }

                if (Math.random() < fallChance) {
                    horse.fall();
                    horse.setConfidence(Math.max(0, horse.getConfidence() - 0.05));
                }

                if (horse.getDistanceTravelled() >= raceLength && horse.getFinishingTimeTicks() == -1) {
                    horse.setFinishingTimeTicks(raceTicks); // 🆕 Record finishing time
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
            winner.incrementWins(); // 🆕 Record win
            JOptionPane.showMessageDialog(parent, "🏆 The winner is: " + winner.getName());
        } else {
            JOptionPane.showMessageDialog(parent, "No horses finished the race!");
        }

        // 📝 Show Performance Report
        StringBuilder report = new StringBuilder();
        report.append("Race Performance Report:\n\n");
        for (Horse horse : lanes) {
            if (horse != null) {
                double avgSpeed = horse.getDistanceTravelled() / (double) Math.max(1, horse.getFinishingTimeTicks());
                report.append(horse.getName()).append(" (").append(horse.getBreed()).append(")\n")
                      .append(" - Distance: ").append(horse.getDistanceTravelled()).append(" m\n")
                      .append(" - Finishing Time: ").append(horse.getFinishingTimeTicks() == -1 ? "Did not finish" : horse.getFinishingTimeTicks() + " ticks").append("\n")
                      .append(" - Avg Speed: ").append(String.format("%.2f", avgSpeed)).append(" m/tick\n")
                      .append(" - Total Wins: ").append(horse.getTotalWins()).append("\n")
                      .append(" - Confidence History: ").append(horse.getConfidenceHistory()).append("\n\n");

                horse.recordConfidence(); // 🆕 Save confidence after race
		// Update best times for each track condition
		if (horse.getFinishingTimeTicks() != -1) { // Only update if horse finished
    		    String condition = weatherCondition;
   		    int previousBest = horse.getBestTimeForCondition(condition);

    		    if (previousBest == -1 || horse.getFinishingTimeTicks() < previousBest) {
        	        horse.setBestTimeForCondition(condition, horse.getFinishingTimeTicks());
    		    }
		}
            }
        }

        JTextArea textArea = new JTextArea(report.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);

        JOptionPane.showMessageDialog(parent, scrollPane, "Race Stats", JOptionPane.INFORMATION_MESSAGE);
    }

    public Horse getHorseAtLane(int lane) {
        if (lane >= 0 && lane < numLanes) {
            return lanes[lane];
        }
        return null;
    }

    public int getNumLanes() {
        return numLanes;
    }

    public int getRaceLength() {
        return raceLength;
    }
}
