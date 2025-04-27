import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.util.List;

public class RaceViewer extends JFrame {
    private Race race;
    private RacePanel racePanel;
    private OddsPanel oddsPanel;
    private BettingPanel bettingPanel;
    private BetHistoryPanel historyPanel;
    private Timer timer;

    public RaceViewer(Race race) {
        this.race = race;
        setTitle("🏁 Live Race View");
        setSize(1400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        racePanel = new RacePanel();
        add(new JScrollPane(racePanel), BorderLayout.CENTER); // Scrollable if race is big

        oddsPanel = new OddsPanel();
        add(oddsPanel, BorderLayout.EAST);

        bettingPanel = new BettingPanel();
        add(bettingPanel, BorderLayout.WEST);

        // below odds, show history
        historyPanel = new BetHistoryPanel();
        add(historyPanel, BorderLayout.SOUTH);

        setVisible(true);

        // Timer controls the race speed
        timer = new Timer(100, e -> {
            if (race.isRaceOngoing()) {
                race.advanceRaceTick();
                // pull & show new odds
                oddsPanel.updateOdds(race.getCurrentOdds());
                racePanel.repaint();
                oddsPanel.repaint();
		bettingPanel.refreshHorseList();
            } else {
                timer.stop();
		historyPanel.refreshHistory();
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

    private class BettingPanel extends JPanel {
        private JComboBox<String> horseSelect;
        private JTextField amountField;
        private JButton placeBtn;

        public BettingPanel() {
            setLayout(new GridLayout(0,1,5,5));
            setBorder(BorderFactory.createTitledBorder("Place Your Bet"));

            horseSelect = new JComboBox<>();
            refreshHorseList();

            amountField = new JTextField("10");

            placeBtn = new JButton("Place Bet");
            placeBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int idx = horseSelect.getSelectedIndex();
                    Horse h = race.getHorseAtLane(idx);
                    double amt;
                    try { amt = Double.parseDouble(amountField.getText()); }
                    catch(Exception ex){ JOptionPane.showMessageDialog(BettingPanel.this,
                           "Invalid amount"); return; }
                    boolean ok = race.placeBet(h, amt);
                    if (!ok) {
                        JOptionPane.showMessageDialog(BettingPanel.this,
                           "Could not place bet");
                    } else {
                        JOptionPane.showMessageDialog(BettingPanel.this,
                           "Bet placed on " + h.getName() + " at " +
                           String.format("%.2f:1", race.getCurrentOdds().get(h)));
                    }
                }
            });

            add(new JLabel("Horse:"));
            add(horseSelect);
            add(new JLabel("Amount:"));
            add(amountField);
            add(placeBtn);
        }

        /** Call on every tick so the dropdown matches current lanes. */
        public void refreshHorseList() {
            horseSelect.removeAllItems();
            for (int i = 0; i < race.getNumLanes(); i++) {
                Horse h = race.getHorseAtLane(i);
                if (h!=null) {
                    horseSelect.addItem(h.getName());
                }
            }
        }
    }

    private class BetHistoryPanel extends JPanel {
        private DefaultListModel<Bet> model;
        private JList<Bet> list;

        public BetHistoryPanel() {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createTitledBorder("Bet History"));

            model = new DefaultListModel<>();
            list  = new JList<>(model);
            list.setVisibleRowCount(5);
            add(new JScrollPane(list), BorderLayout.CENTER);
        }

        /** Reloads the JList from race.getBetHistory() */
        public void refreshHistory() {
            model.clear();
            List<Bet> all = race.getBetHistory();
            for (Bet b : all) {
                model.addElement(b);
            }
        }
    }
}
