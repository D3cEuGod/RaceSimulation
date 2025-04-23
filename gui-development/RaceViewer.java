import javax.swing.*;
import java.awt.*;

public class RaceViewer extends JFrame {
    private Race race;
    private RacePanel racePanel;
    private Timer timer;

    public RaceViewer(Race race) {
        this.race = race;
        setTitle("Live Race View");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        racePanel = new RacePanel();
        add(racePanel, BorderLayout.CENTER);

        setVisible(true);

        // Start the race timer
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
            race.displayRace(g);
        }
    }
}
