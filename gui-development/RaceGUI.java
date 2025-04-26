import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class RaceGUI extends JFrame {
    private JPanel trackSettingsPanel;
    private JPanel horseSettingsPanel;
    private JPanel horseListPanel;
    private JPanel controlPanel;

    private JSpinner laneCountSpinner;
    private JTextField trackLengthField;
    private JComboBox<String> trackShapeComboBox;
    private JComboBox<String> weatherConditionComboBox;

    private JButton startRaceButton;
    private JButton resetButton;

    private Race race;

    private List<HorseConfig> horseConfigs = new ArrayList<>();

    public RaceGUI() {
        setTitle("Race Simulator");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(createSettingsPanel(), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createSettingsPanel() {
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(1, 2, 10, 10)); // Two side-by-side panels

        // --- Track Settings Panel ---
        trackSettingsPanel = new JPanel();
        trackSettingsPanel.setBorder(BorderFactory.createTitledBorder("Track Settings"));
        trackSettingsPanel.setLayout(new GridLayout(4, 2, 5, 5));

        trackSettingsPanel.add(new JLabel("Track Length (m):"));
        trackLengthField = new JTextField("50");
        trackSettingsPanel.add(trackLengthField);

        trackSettingsPanel.add(new JLabel("Track Shape:"));
        trackShapeComboBox = new JComboBox<>(new String[]{"Oval", "Figure-eight", "Custom"});
        trackSettingsPanel.add(trackShapeComboBox);

        trackSettingsPanel.add(new JLabel("Weather Condition:"));
        weatherConditionComboBox = new JComboBox<>(new String[]{"Dry", "Muddy", "Icy"});
        trackSettingsPanel.add(weatherConditionComboBox);

        trackSettingsPanel.add(new JLabel("Number of Horses:"));
        laneCountSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        trackSettingsPanel.add(laneCountSpinner);

        laneCountSpinner.addChangeListener(e -> updateHorseList());

        settingsPanel.add(trackSettingsPanel);

        // --- Horse Settings Panel ---
        horseSettingsPanel = new JPanel();
        horseSettingsPanel.setBorder(BorderFactory.createTitledBorder("Horse Settings"));
        horseSettingsPanel.setLayout(new BorderLayout());

        horseListPanel = new JPanel();
        horseListPanel.setLayout(new BoxLayout(horseListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(horseListPanel);
        horseSettingsPanel.add(scrollPane, BorderLayout.CENTER);

        settingsPanel.add(horseSettingsPanel);

        updateHorseList(); // Initialize horses

        return settingsPanel;
    }

    private JPanel createControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createTitledBorder("Race Controls"));

        startRaceButton = new JButton("Start Race");
        startRaceButton.addActionListener(e -> startRace());

        resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetSettings());

        controlPanel.add(startRaceButton);
        controlPanel.add(resetButton);

        return controlPanel;
    }

    private void updateHorseList() {
        int count = (Integer) laneCountSpinner.getValue();
        horseListPanel.removeAll();
        horseConfigs.clear();

        for (int i = 0; i < count; i++) {
            horseConfigs.add(createHorseConfigPanel(i));
        }

        horseListPanel.revalidate();
        horseListPanel.repaint();
    }

    private HorseConfig createHorseConfigPanel(int index) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField nameField = new JTextField("Horse " + (index + 1), 10);
        JComboBox<String> symbolBox = new JComboBox<>(new String[]{"🐎", "🐴", "♘", "♞"});
        JLabel previewLabel = new JLabel("🐎");

        symbolBox.addActionListener(e -> {
            String selected = (String) symbolBox.getSelectedItem();
            previewLabel.setText(selected);
        });

        panel.add(new JLabel("Horse " + (index + 1) + ":"));
        panel.add(nameField);
        panel.add(symbolBox);
        panel.add(previewLabel);

        horseListPanel.add(panel);
        return new HorseConfig(nameField, symbolBox, previewLabel);
    }

    private void startRace() {
        int laneCount = (Integer) laneCountSpinner.getValue();
        int trackLength = Integer.parseInt(trackLengthField.getText());
        String trackShape = (String) trackShapeComboBox.getSelectedItem();
        String weatherCondition = (String) weatherConditionComboBox.getSelectedItem();

        race = new Race(trackLength, laneCount, trackShape, weatherCondition, this);

        for (int i = 0; i < laneCount; i++) {
            HorseConfig config = horseConfigs.get(i);
            String horseName = config.nameField.getText().trim();
            if (horseName.isEmpty()) {
                horseName = "Horse " + (i + 1);
            }
            char symbol = ((String) config.symbolBox.getSelectedItem()).charAt(0);
            Horse horse = new Horse(symbol, horseName, 0.8);
            race.addHorse(horse, i);
        }

        new RaceViewer(race);
    }

    private void resetSettings() {
        trackLengthField.setText("50");
        trackShapeComboBox.setSelectedIndex(0);
        weatherConditionComboBox.setSelectedIndex(0);
        laneCountSpinner.setValue(2);
        updateHorseList();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RaceGUI::new);
    }

    private static class HorseConfig {
        JTextField nameField;
        JComboBox<String> symbolBox;
        JLabel previewLabel;

        HorseConfig(JTextField nameField, JComboBox<String> symbolBox, JLabel previewLabel) {
            this.nameField = nameField;
            this.symbolBox = symbolBox;
            this.previewLabel = previewLabel;
        }
    }
}
