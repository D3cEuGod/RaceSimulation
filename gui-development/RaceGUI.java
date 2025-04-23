import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class RaceGUI extends JFrame {
    private JPanel trackConfigPanel;
    private JPanel raceControlPanel;
    private JPanel raceVisualizationPanel;

    // Components for track configuration
    private JSpinner laneCountSpinner;
    private JTextField trackLengthField;
    private JComboBox<String> trackShapeComboBox;
    private JComboBox<String> weatherConditionComboBox;

    // Components for race control
    private JButton startRaceButton;
    private JButton resetButton;
    private JSlider speedSlider;

    private Race race;

    private List<JTextField> horseNameFields = new ArrayList<>();

    public RaceGUI() {
        setTitle("Race Simulator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Track configuration panel
        trackConfigPanel = new JPanel();
        trackConfigPanel.setLayout(new GridLayout(6, 2)); // More rows to fit horse names
        addTrackConfigComponents();

        // Race control panel
        raceControlPanel = new JPanel(new FlowLayout());
        addRaceControlComponents();

        // Visualization panel placeholder
        raceVisualizationPanel = new JPanel();
        add(raceVisualizationPanel, BorderLayout.CENTER);

        // Add config and control panels
        add(trackConfigPanel, BorderLayout.NORTH);
        add(raceControlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addTrackConfigComponents() {
        // Lane count spinner
        trackConfigPanel.add(new JLabel("Number of Lanes:"));
        laneCountSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        trackConfigPanel.add(laneCountSpinner);

        // Track length
        trackConfigPanel.add(new JLabel("Track Length (m):"));
        trackLengthField = new JTextField("50");
        trackConfigPanel.add(trackLengthField);

        // Track shape
        trackConfigPanel.add(new JLabel("Track Shape:"));
        trackShapeComboBox = new JComboBox<>(new String[]{"Oval", "Figure-eight", "Custom"});
        trackConfigPanel.add(trackShapeComboBox);

        // Weather
        trackConfigPanel.add(new JLabel("Weather Condition:"));
        weatherConditionComboBox = new JComboBox<>(new String[]{"Dry", "Muddy", "Icy"});
        trackConfigPanel.add(weatherConditionComboBox);

        // Horse names
        trackConfigPanel.add(new JLabel("Horse Names:"));
        JPanel namesPanel = new JPanel(new GridLayout(1, 2)); // Default for 2 horses
        horseNameFields.clear();
        for (int i = 0; i < 2; i++) {
            JTextField field = new JTextField("Horse " + (i + 1));
            horseNameFields.add(field);
            namesPanel.add(field);
        }
        trackConfigPanel.add(namesPanel);

        // Update horse name fields dynamically
        laneCountSpinner.addChangeListener(e -> {
            int count = (Integer) laneCountSpinner.getValue();
            namesPanel.removeAll();
            horseNameFields.clear();
            for (int i = 0; i < count; i++) {
                JTextField field = new JTextField("Horse " + (i + 1));
                horseNameFields.add(field);
                namesPanel.add(field);
            }
            namesPanel.revalidate();
            namesPanel.repaint();
        });
    }

    private void addRaceControlComponents() {
        startRaceButton = new JButton("Start Race");
        startRaceButton.addActionListener(e -> startRace());
        raceControlPanel.add(startRaceButton);

        resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetTrackConfig());
        raceControlPanel.add(resetButton);

        raceControlPanel.add(new JLabel("Race Speed:"));
        speedSlider = new JSlider(1, 10, 5);
        raceControlPanel.add(speedSlider);
    }

    private void startRace() {
        int laneCount = (Integer) laneCountSpinner.getValue();
        int trackLength = Integer.parseInt(trackLengthField.getText());
        String trackShape = (String) trackShapeComboBox.getSelectedItem();
        String weatherCondition = (String) weatherConditionComboBox.getSelectedItem();

        race = new Race(trackLength, laneCount, trackShape, weatherCondition, this);

        for (int i = 0; i < laneCount; i++) {
            String horseName = horseNameFields.get(i).getText().trim();
            if (horseName.isEmpty()) {
                horseName = "Horse " + (i + 1);
            }
            Horse horse = new Horse((char) ('A' + i), horseName, 0.8);
            race.addHorse(horse, i);
        }

        // Open race viewer
        new RaceViewer(race);
    }

    private void resetTrackConfig() {
        laneCountSpinner.setValue(2);
        trackLengthField.setText("1000");
        trackShapeComboBox.setSelectedIndex(0);
        weatherConditionComboBox.setSelectedIndex(0);
        for (int i = 0; i < horseNameFields.size(); i++) {
            horseNameFields.get(i).setText("Horse " + (i + 1));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RaceGUI::new);
    }
}

