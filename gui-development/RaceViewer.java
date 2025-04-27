import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;

public class RaceViewer extends JFrame {
    private Race race;
    private RacePanel racePanel;
    private OddsPanel oddsPanel;
    private Timer timer;

    public RaceViewer(Race race) {
        this.race = race;
        setTitle("🏁 Live Race View");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        racePanel = new RacePanel();
        add(new JScrollPane(racePanel), BorderLayout.CENTER); // Scrollable if race is big

        oddsPanel = new OddsPanel();
        add(oddsPanel, BorderLayout.EAST);

        setVisible(true);

        // Timer controls the race speed
        timer = new Timer(100, e -> {
            if (race.isRaceOngoing()) {
                race.advanceRaceTick();
                // pull & show new odds
                oddsPanel.updateOdds(race.getCurrentOdds());
                racePanel.repaint();
                oddsPanel.repaint();
            } else {
                timer.stop();
                race.displayResultsInViewer(this);
            }
        });
        timer.start();
    }

    /** Panel that draws the current odds for each horse */
    private class OddsPanel extends JPanel {
        private Map<Horse, Double> odds = new HashMap<>();

        public OddsPanel() {
            setPreferredSize(new Dimension(200, 600));
            setBorder(BorderFactory.createTitledBorder("Betting Odds"));
        }

        public void updateOdds(Map<Horse, Double> newOdds) {
            this.odds = newOdds;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setFont(new Font("SansSerif", Font.PLAIN, 12));
            int y = 30;
            for (Map.Entry<Horse, Double> entry : odds.entrySet()) {
                String name = entry.getKey().getName();
                String oddStr = String.format("%.2f:1", entry.getValue());
                g.drawString(name + " → " + oddStr, 10, y);
                y += 20;
            }
        }
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
