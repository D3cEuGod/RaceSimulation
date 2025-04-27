import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.HashMap;

public class Race {
    private int raceLength;
    private int numLanes;
    private Horse[] lanes;
    private boolean raceOngoing = true;
    private String weatherCondition;
    private Map<Horse, Double> currentOdds = new HashMap<>();

    private int raceTicks = 0;  // ── NEW: count ticks in this race

    public Race(int raceLength, int numLanes, String trackShape, String weatherCondition, RaceGUI gui) {
        this.raceLength = raceLength;
        this.numLanes = numLanes;
        this.lanes = new Horse[numLanes];
        this.weatherCondition = weatherCondition;
    }

    public void addHorse(Horse horse, int lane) {
        if (lane>=0 && lane<numLanes && lanes[lane]==null) {
            lanes[lane] = horse;
        }
    }

    public void advanceRaceTick() {
        raceTicks++;  // ── NEW

        raceOngoing = false;
        for (int i = 0; i < numLanes; i++) {
            Horse h = lanes[i];
            if (h==null || h.hasFallen()) continue;

            // movement & fall logic (unchanged, plus finishing time)
            if (Math.random() < 0.95) {
                int step = (int)(h.getConfidence()*3 + 1);
                if ("Muddy".equalsIgnoreCase(weatherCondition)) step = Math.max(1, step-1);
                if ("Arabian".equals(h.getBreed())) step++;
                else if ("Shire".equals(h.getBreed())) step = Math.max(1,step-1);
                if ("Speed Shoes".equals(h.getEquipment())) step++;
                else if ("Heavy Armor".equals(h.getEquipment())) step = Math.max(1,step-1);
                h.moveForward(step);
            }

            double fallChance = h.getConfidence()*0.03;
            if ("Icy".equalsIgnoreCase(weatherCondition)) fallChance *= 2;
            if ("Speed Shoes".equals(h.getEquipment())) fallChance *= 1.2;
            else if ("Heavy Armor".equals(h.getEquipment())) fallChance *= 0.8;
            if (Math.random()<fallChance) {
                h.fall();
                h.setConfidence(Math.max(0, h.getConfidence()-0.05));
            }

            // record finishing time at the moment crossing line
            if (h.getDistanceTravelled()>=raceLength && h.getFinishingTimeTicks()==-1) {
                h.setFinishingTimeTicks(raceTicks);
            }

            if (h.getDistanceTravelled() < raceLength) {
                raceOngoing = true;
            }
        }
	updateOdds();
    }

    private void updateOdds() {
        currentOdds = OddsCalculator.calculateOdds(lanes, weatherCondition);
    }

    public Map<Horse, Double> getCurrentOdds() {
        return currentOdds;
    }

    public boolean isRaceOngoing() { return raceOngoing; }

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
        Horse winner = null; int maxDist = -1;
        for (Horse h : lanes) {
            if (h!=null && !h.hasFallen() && h.getDistanceTravelled()>maxDist) {
                winner = h; maxDist = h.getDistanceTravelled();
            }
        }

        if (winner!=null) {
            winner.incrementWins();
            winner.setConfidence(Math.min(1.0, winner.getConfidence()+0.05));
            JOptionPane.showMessageDialog(parent, "🏆 Winner: "+winner.getName());
        } else {
            JOptionPane.showMessageDialog(parent, "No horse finished!");
        }

        // ── Performance & history recording ─────────────────────────────────────
        StringBuilder rpt = new StringBuilder("Race Report:\n\n");
        for (Horse h : lanes) {
            if (h==null) continue;
            int ft = h.getFinishingTimeTicks();
            double avg = h.getDistanceTravelled() / (double)Math.max(1, ft);
            rpt.append(h.getName()).append(" [").append(h.getBreed()).append("]\n")
               .append(" • Time: ").append(ft==-1?"DNF":ft+" ticks").append("\n")
               .append(" • Avg Speed: ").append(String.format("%.2f",avg)).append("\n")
               .append(" • Wins: ").append(h.getTotalWins()).append("\n\n");

            // ── NEW: record into history lists ─────────────────────────────
            h.recordRaceResult(ft, h.getDistanceTravelled(), h.getConfidence());
            // ─────────────────────────────────────────────────────────────────
        }

        JTextArea ta = new JTextArea(rpt.toString());
        ta.setFont(new Font("Monospaced",Font.PLAIN,12));
        ta.setEditable(false);
        JOptionPane.showMessageDialog(parent, new JScrollPane(ta), "Race Stats",
                                      JOptionPane.INFORMATION_MESSAGE);
    }

    public Horse getHorseAtLane(int lane) { 
        return (lane>=0 && lane<numLanes) ? lanes[lane] : null; 
    }
    public int   getNumLanes()    { return numLanes; }
    public int   getRaceLength()  { return raceLength; }
}
