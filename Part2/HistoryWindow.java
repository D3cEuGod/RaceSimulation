import javax.swing.*;
import java.awt.*;

public class HistoryWindow extends JFrame {
    public HistoryWindow(Race race) {
        setTitle("Historical Race Data");
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        // 1) Horse selector at top
        JComboBox<String> horseSelector = new JComboBox<>();
        for (int i = 0; i < race.getNumLanes(); i++) {
            Horse h = race.getHorseAtLane(i);
            if (h != null) horseSelector.addItem(h.getName());
        }
        add(horseSelector, BorderLayout.NORTH);

        // 2) Table in center
        String[] cols = {"Race #", "Finish Ticks", "Avg Speed", "Confidence"};
        JTable table = new JTable(new Object[][]{}, cols);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 3) (Optional) Chart placeholder
        JPanel chartPanel = new JPanel(new BorderLayout());
        add(chartPanel, BorderLayout.SOUTH);

        // When user selects a horse, populate the table
        horseSelector.addActionListener(e -> {
            String name = (String) horseSelector.getSelectedItem();
            Horse chosen = null;
            for (int i = 0; i < race.getNumLanes(); i++) {
                Horse h = race.getHorseAtLane(i);
                if (h != null && h.getName().equals(name)) {
                    chosen = h;
                    break;
                }
            }
            if (chosen == null) return;

            var times = chosen.getFinishingTimeHistory();
            var speeds = chosen.getAvgSpeedHistory();
            var confs  = chosen.getConfidenceHistory();
            Object[][] rows = new Object[times.size()][4];
            for (int i = 0; i < times.size(); i++) {
                rows[i][0] = i + 1;
                rows[i][1] = times.get(i) == -1 ? "DNF" : times.get(i);
                rows[i][2] = String.format("%.2f", speeds.get(i));
                rows[i][3] = String.format("%.2f", confs.get(i));
            }
            table.setModel(new javax.swing.table.DefaultTableModel(rows, cols));

            // ── OPTIONAL ──
            // If you integrate JFreeChart, you could draw a speed/confidence trend here
        });

        setVisible(true);
    }
}
