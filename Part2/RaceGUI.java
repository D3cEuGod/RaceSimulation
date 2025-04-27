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
    private JButton toggleFullscreenButton; // 🆕

    private Race race;

    private List<HorseConfig> horseConfigs = new ArrayList<>();
    private List<Horse> horsePool = new ArrayList<>();

    public RaceGUI() {
        setTitle("Race Simulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        add(createSettingsPanel(), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);

        setMinimumSize(new Dimension(1100, 700)); // Always minimum size
        setExtendedState(JFrame.MAXIMIZED_BOTH);  // Start maximized
        setVisible(true);
    }

    private JPanel createSettingsPanel() {
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Track Settings Panel
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

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3; // 30% width
        settingsPanel.add(trackSettingsPanel, gbc);

        // Horse Settings Panel
        horseSettingsPanel = new JPanel();
        horseSettingsPanel.setBorder(BorderFactory.createTitledBorder("Horse Settings"));
        horseSettingsPanel.setLayout(new BorderLayout());

        horseListPanel = new JPanel();
        horseListPanel.setLayout(new BoxLayout(horseListPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(horseListPanel);
        horseSettingsPanel.add(scrollPane, BorderLayout.CENTER);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7; // 70% width
        settingsPanel.add(horseSettingsPanel, gbc);

        updateHorseList();

        return settingsPanel;
    }

    private JPanel createControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createTitledBorder("Race Controls"));

        startRaceButton = new JButton("Start Race");
        startRaceButton.addActionListener(e -> startRace());

        resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetSettings());

        toggleFullscreenButton = new JButton("Toggle Fullscreen");
        toggleFullscreenButton.addActionListener(e -> toggleFullscreen());

	JButton viewRecordsButton = new JButton("View Records");
	viewRecordsButton.addActionListener(e -> openRecordsWindow());

	JButton viewHistoryButton = new JButton("View History");
        viewHistoryButton.addActionListener(e -> openHistoryWindow());

        controlPanel.add(startRaceButton);
        controlPanel.add(resetButton);
	controlPanel.add(viewRecordsButton);
	controlPanel.add(viewHistoryButton);  
        controlPanel.add(toggleFullscreenButton);

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
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout(5, 5));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        cardPanel.setBackground(new Color(245, 245, 245)); // Light gray background

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JPanel bottomRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JTextField nameField = new JTextField("Horse " + (index + 1), 10);
        JComboBox<String> symbolBox = new JComboBox<>(new String[]{"🐎", "🐴", "♘", "♞"});
        JLabel previewLabel = new JLabel("🐎");

        JComboBox<String> breedBox = new JComboBox<>(new String[]{"Thoroughbred", "Arabian", "Mustang", "Shire"});
        JComboBox<String> equipmentBox = new JComboBox<>(new String[]{"Light Saddle", "Heavy Armor", "Speed Shoes", "No Equipment"});

        symbolBox.addActionListener(e -> {
            String selected = (String) symbolBox.getSelectedItem();
            previewLabel.setText(selected);
        });

        // Top Row
        topRow.add(new JLabel("Horse " + (index + 1) + ":"));
        topRow.add(nameField);
        topRow.add(new JLabel("Symbol:"));
        topRow.add(symbolBox);
        topRow.add(previewLabel);

        // Bottom Row
        bottomRow.add(new JLabel("Breed:"));
        bottomRow.add(breedBox);
        bottomRow.add(new JLabel("(affects confidence/speed)"));

        bottomRow.add(new JLabel("Equipment:"));
        bottomRow.add(equipmentBox);
        bottomRow.add(new JLabel("(affects speed/fall chance)"));

        cardPanel.add(topRow, BorderLayout.NORTH);
        cardPanel.add(bottomRow, BorderLayout.SOUTH);

        horseListPanel.add(cardPanel);

        return new HorseConfig(nameField, symbolBox, previewLabel, breedBox, equipmentBox);
    }

    private void startRace() {
        int laneCount = (Integer) laneCountSpinner.getValue();
        int trackLength = Integer.parseInt(trackLengthField.getText());
        String trackShape = (String) trackShapeComboBox.getSelectedItem();
        String weatherCondition = (String) weatherConditionComboBox.getSelectedItem();

	// 1) Adjust horsePool size
        if (horsePool.size() > laneCount) {
            horsePool.subList(laneCount, horsePool.size()).clear();
        }
        while (horsePool.size() < laneCount) {
            // Symbol and name here are placeholders; we'll overwrite them below
            horsePool.add(new Horse('A', "Horse", 0.8));
        }

	race = new Race(trackLength, laneCount, trackShape, weatherCondition, this);

        // 2) Reset each Horse for the new race and apply current config
        for (int i = 0; i < laneCount; i++) {
            Horse h = horsePool.get(i);
            h.resetForRace();

            HorseConfig cfg = horseConfigs.get(i);
            String horseName = cfg.nameField.getText().trim();
            if (horseName.isEmpty()) horseName = "Horse " + (i + 1);
	    h.setName(horseName);
            char symbol = ((String)cfg.symbolBox.getSelectedItem()).charAt(0);
            String breed = (String)cfg.breedBox.getSelectedItem();
            String equipment = (String)cfg.equipmentBox.getSelectedItem();

            double baseConfidence = 0.8;
            if ("Thoroughbred".equals(breed))      baseConfidence += 0.1;
            else if ("Arabian".equals(breed))      baseConfidence += 0.05;
            else if ("Shire".equals(breed))        baseConfidence -= 0.05;

            h.setSymbol(symbol);
            h.setBreed(breed);
            h.setEquipment(equipment);
            h.setConfidence(baseConfidence);
            h.setFinishingTimeTicks(-1);  // ensure reset

            race.addHorse(h, i);
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

    private void toggleFullscreen() {
        int state = getExtendedState();
        if ((state & JFrame.MAXIMIZED_BOTH) == JFrame.MAXIMIZED_BOTH) {
            setExtendedState(JFrame.NORMAL);
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }

    private void openRecordsWindow() {
    	if (race != null) {
            new RecordsWindow(race);
    	} else {
            JOptionPane.showMessageDialog(this, "No race started yet!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void openHistoryWindow() {
        if (race != null) {
            new HistoryWindow(race);
        } else {
            JOptionPane.showMessageDialog(this,
                "No race data yet!",
                "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RaceGUI::new);
    }

    private static class HorseConfig {
        JTextField nameField;
        JComboBox<String> symbolBox;
        JLabel previewLabel;
        JComboBox<String> breedBox;
        JComboBox<String> equipmentBox;

        HorseConfig(JTextField nameField, JComboBox<String> symbolBox, JLabel previewLabel,
                    JComboBox<String> breedBox, JComboBox<String> equipmentBox) {
            this.nameField = nameField;
            this.symbolBox = symbolBox;
            this.previewLabel = previewLabel;
            this.breedBox = breedBox;
            this.equipmentBox = equipmentBox;
        }
    }
}
