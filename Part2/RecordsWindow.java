import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class RecordsWindow extends JFrame {
    public RecordsWindow(Race race) {
        setTitle("Track Records");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JTextArea recordsArea = new JTextArea();
        recordsArea.setEditable(false);
        recordsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        Map<String, java.util.List<String>> recordsByCondition = new HashMap<>();

        String[] conditions = {"Dry", "Muddy", "Icy"};

        for (String condition : conditions) {
            java.util.List<String> lines = new java.util.ArrayList<>();

            for (int i = 0; i < race.getNumLanes(); i++) {
                Horse horse = race.getHorseAtLane(i);
                if (horse != null) {
                    int bestTime = horse.getBestTimeForCondition(condition);
                    if (bestTime != -1) {
                        lines.add(String.format("%-15s - %3d ticks", horse.getName(), bestTime));
                    } else {
                        lines.add(String.format("%-15s -   -", horse.getName()));
                    }
                }
            }

            // Sort horses by best times (fastest first)
            lines.sort((a, b) -> {
                int timeA = extractTime(a);
                int timeB = extractTime(b);
                return Integer.compare(timeA, timeB);
            });

            recordsByCondition.put(condition, lines);
        }

        // Build final report
        StringBuilder sb = new StringBuilder();
        for (String condition : conditions) {
            sb.append("=== ").append(condition).append(" Track ===\n");
            for (String line : recordsByCondition.get(condition)) {
                sb.append(line).append("\n");
            }
            sb.append("\n");
        }

        recordsArea.setText(sb.toString());

        add(new JScrollPane(recordsArea), BorderLayout.CENTER);

        setVisible(true);
    }

    private int extractTime(String line) {
        try {
            String[] parts = line.split("-");
            String timePart = parts[1].trim().split(" ")[0];
            if (timePart.equals("-")) {
                return Integer.MAX_VALUE; // Sort unfinished horses last
            }
            return Integer.parseInt(timePart);
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }
}
