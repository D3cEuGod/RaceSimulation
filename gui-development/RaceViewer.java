import javax.swing.*;
import java.awt.*;

public class RaceViewer extends JFrame {
    private Race race;
    private RacePanel racePanel;
    private Timer timer;

    public RaceViewer(Race race) {
        this.race = race;
        setTitle("🏁 Live Race View");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        racePanel = new RacePanel();
        add(new JScrollPane(racePanel), BorderLayout.CENTER); // Scrollable if race is big

        setVisible(true);

        // Timer controls the race speed
        timer = new Timer(100, e -> {
            if (race.isRaceOngoing()) {
                race.advanceRaceTick();
                racePanel.repaint();
            } else {
                timer.stop();
                race.displayResultsInViewer(this);
            }
        });
        timer.start();
    }

    private class RacePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            displayRace(g);
        }

        private void displayRace(Graphics g) {
            g.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Use fixed-width font for better alignment

            for (int i = 0; i < race.getNumLanes(); i++) {
                Horse horse = race.getHorseAtLane(i);
                if (horse != null) {
                    int y = 30 + i * 70; // More vertical space per horse
                    int pos = horse.getDistanceTravelled();

                    // Symbol
                    String symbol = horse.hasFallen() ? "❌" : String.valueOf(horse.getSymbol());
                    g.drawString("|" + " ".repeat(pos) + symbol, 10, y);

                    // Full details
                    if (horse.hasFallen()) {
                        g.drawString("| " + horse.getName() + " (FALLEN) [Breed: " + horse.getBreed() +
                                     " | Equipment: " + horse.getEquipment() + "]", 10, y + 20);
                    } else {
                        g.drawString("| " + horse.getName() + " (Confidence: " +
                                     String.format("%.2f", horse.getConfidence()) + ") [Breed: " +
                                     horse.getBreed() + " | Equipment: " + horse.getEquipment() + "]", 10, y + 20);
                    }
                }
            }

            g.drawString("=".repeat(race.getRaceLength()), 10, 30 + race.getNumLanes() * 70);
        }
    }
}
