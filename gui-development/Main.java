public class Main {
    public static void main(String[] args) {
        // Start the GUI application
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RaceGUI(); // Initialize the GUI
            }
        });
    }
}
