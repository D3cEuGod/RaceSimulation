import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    // For race visualization
    private Race race;

    public RaceGUI() {
        setTitle("Race Simulator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Track configuration panel
        trackConfigPanel = new JPanel();
        trackConfigPanel.setLayout(new GridLayout(4, 2)); // 4 rows, 2 columns
        addTrackConfigComponents();

        // Race control panel
        raceControlPanel = new JPanel();
        raceControlPanel.setLayout(new FlowLayout());
        addRaceControlComponents();

        // Race visualization panel (empty for now)
        raceVisualizationPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (race != null) {
                    race.displayRace(g); // Draw the race on the panel
                }
            }
        };
        raceVisualizationPanel.setPreferredSize(new Dimension(800, 300));
        add(raceVisualizationPanel, BorderLayout.CENTER);

        // Add panels to frame
        add(trackConfigPanel, BorderLayout.NORTH);
        add(raceControlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addTrackConfigComponents() {
        // Lane count spinner
        trackConfigPanel.add(new JLabel("Number of Lanes:"));
        laneCountSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        trackConfigPanel.add(laneCountSpinner);

        // Track length field
        trackConfigPanel.add(new JLabel("Track Length (m):"));
        trackLengthField = new JTextField("1000");
        trackConfigPanel.add(trackLengthField);

        // Track shape combo box
        trackConfigPanel.add(new JLabel("Track Shape:"));
        String[] shapes = {"Oval", "Figure-eight", "Custom"};
        trackShapeComboBox = new JComboBox<>(shapes);
        trackConfigPanel.add(trackShapeComboBox);

        // Weather condition combo box
        trackConfigPanel.add(new JLabel("Weather Condition:"));
        String[] weatherConditions = {"Dry", "Muddy", "Icy"};
        weatherConditionComboBox = new JComboBox<>(weatherConditions);
        trackConfigPanel.add(weatherConditionComboBox);
    }

    private void addRaceControlComponents() {
        // Start race button
        startRaceButton = new JButton("Start Race");
        startRaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startRace();
            }
        });
        raceControlPanel.add(startRaceButton);

        // Reset button
        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetTrackConfig();
            }
        });
        raceControlPanel.add(resetButton);

        // Speed control slider
        raceControlPanel.add(new JLabel("Race Speed:"));
        speedSlider = new JSlider(1, 10, 5);
        raceControlPanel.add(speedSlider);
    }

    private void startRace() {
        // Get values from the GUI
        int laneCount = (Integer) laneCountSpinner.getValue();
        int trackLength = Integer.parseInt(trackLengthField.getText());
        String trackShape = (String) trackShapeComboBox.getSelectedItem();
        String weatherCondition = (String) weatherConditionComboBox.getSelectedItem();

        // Create a new Race object with these parameters and pass 'this' (the current RaceGUI instance)
        race = new Race(trackLength, laneCount, trackShape, weatherCondition, this);

        // Start the race simulation
        race.startRace();
        raceVisualizationPanel.repaint();
    }

    private void resetTrackConfig() {
        // Reset GUI components to default values
        laneCountSpinner.setValue(2);
        trackLengthField.setText("1000");
        trackShapeComboBox.setSelectedIndex(0);
        weatherConditionComboBox.setSelectedIndex(0);
    }

    public void displayResults() {
        // Show a simple dialog with the race result
        JOptionPane.showMessageDialog(this, "The race has finished!");
    }

    public static void main(String[] args) {
        new RaceGUI();
    }
}
