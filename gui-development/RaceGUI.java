import javax.swing.*;
import java.awt.*;
import java.util.*;

public class RaceGUI extends JFrame {
    private int raceLength;
    private int numLanes;
    private Horse[] lanes;
    private Random random = new Random();
    private JPanel racePanel;
    private JLabel[] laneLabels;
    private boolean raceOngoing;

    public RaceGUI(int raceLength, int numLanes) {
        this.raceLength = raceLength;
        this.numLanes = numLanes;
        this.lanes = new Horse[numLanes];
        this.raceOngoing = false;

        // Set up the JFrame
        setTitle("Horse Race");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel to hold race lanes
        racePanel = new JPanel();
        racePanel.setLayout(new GridLayout(numLanes, 1));
        laneLabels = new JLabel[numLanes];

        // Add labels for each lane
        for (int i = 0; i < numLanes; i++) {
            laneLabels[i] = new JLabel("Lane " + (i + 1) + ": ");
            laneLabels[i].setFont(new Font("Arial", Font.PLAIN, 16));
            racePanel.add(laneLabels[i]);
        }

        // Add the race panel to the frame
        add(racePanel, BorderLayout.CENTER);

        // Button to start the race
        JButton startButton = new JButton("Start Race");
        startButton.addActionListener(e -> startRace());
        add(startButton, BorderLayout.SOUTH);

        // Show the frame
        setVisible(true);
    }

    public void addHorse(Horse horse, int lane) {
        if (lane >= 0 && lane < numLanes && lanes[lane] == null) {
            lanes[lane] = horse;
        }
    }

    public void startRace() {
        raceOngoing = true;
        new Thread(() -> {
            while (raceOngoing) {
                for (int i = 0; i < numLanes; i++) {
                    Horse horse = lanes[i];
                    if (horse != null && !horse.hasFallen()) {
                        // Calculate speed based on horse's confidence
                        double speed = horse.getConfidence() * 3;

                        // Simulate movement: Horses move forward by their speed (could be a random factor)
                        if (Math.random() < 0.95) { 
                            horse.moveForward(); 
                        }

                        // Simulate fall (confidence affects fall chance)
                        if (Math.random() < (1.0 - horse.getConfidence()) * 0.07) { 
                            horse.fall();
                            horse.setConfidence(Math.max(0, horse.getConfidence() - 0.05)); 
                        }

                        // Check if horse reached the finish line
                        if (horse.getDistanceTravelled() >= raceLength) {
                            raceOngoing = false;
                        }
                    }
                }
                updateRacePanel();
                try {
                    Thread.sleep(500); // Delay between race steps
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            displayResults();
        }).start();
    }

    private void updateRacePanel() {
        for (int i = 0; i < numLanes; i++) {
            Horse horse = lanes[i];
            if (horse != null) {
                String status = horse.hasFallen() ? "❌" : String.valueOf(horse.getSymbol());
                int distance = horse.getDistanceTravelled();
                laneLabels[i].setText("Lane " + (i + 1) + ": " + status + " " + " ".repeat(distance) + "|");
            }
        }
    }

    private void displayResults() {
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

        String resultMessage = (winner != null) ? "And the winner is… " + winner.getName() + "!" : "No horses finished the race.";
        JOptionPane.showMessageDialog(this, resultMessage, "Race Finished", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        RaceGUI raceGUI = new RaceGUI(30, 3); // 30 meters, 3 lanes
        // Add horses to lanes
        raceGUI.addHorse(new Horse("PIPPI LONGSTOCKING", '♘', 0.6), 0);
        raceGUI.addHorse(new Horse("KOKOMO", '♞', 0.6), 1);
        raceGUI.addHorse(new Horse("EL JEFE", '2', 0.4), 2);
    }
}

class Horse {
    private String name;
    private char symbol;
    private double confidence;
    private int distanceTravelled;
    private boolean hasFallen;

    public Horse(String name, char symbol, double confidence) {
        this.name = name;
        this.symbol = symbol;
        this.confidence = confidence;
        this.distanceTravelled = 0;
        this.hasFallen = false;
    }

    public String getName() {
        return name;
    }

    public char getSymbol() {
        return symbol;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public int getDistanceTravelled() {
        return distanceTravelled;
    }

    public boolean hasFallen() {
        return hasFallen;
    }

    public void fall() {
        hasFallen = true;
    }

    public void moveForward() {
        if (!hasFallen) {
            distanceTravelled++;
        }
    }
}
