import java.util.*;

public class Race {
    private int raceLength;
    private int numLanes;
    private Horse[] lanes;
    private Random random = new Random();

    public Race(int raceLength, int numLanes) {
        this.raceLength = raceLength;
        this.numLanes = numLanes;
        this.lanes = new Horse[numLanes];
    }

    public void addHorse(Horse horse, int lane) {
        if (lane >= 0 && lane < numLanes && lanes[lane] == null) {
            lanes[lane] = horse;
        }
    }

    public void startRace() {
        int[] positions = new int[numLanes];
        boolean raceOngoing = true;

        while (raceOngoing) {
            clearScreen();
            raceOngoing = false;
            
            for (int i = 0; i < numLanes; i++) {
                if (lanes[i] != null && !lanes[i].isEliminated()) {
                    // Simulate movement and possible fall
                    double speed = lanes[i].getSpeed();
                    positions[i] += (int) speed;
                    
                    if (random.nextDouble() < lanes[i].getConfidence() * 0.1) { // Higher confidence, higher fall chance
                        lanes[i].fall();
                    }
                    
                    raceOngoing |= positions[i] < raceLength;
                }
            }
            displayRace(positions);
            try {
                Thread.sleep(500); // Animation delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        displayResults(positions);
    }

    private void displayRace(int[] positions) {
        for (int i = 0; i < numLanes; i++) {
            if (lanes[i] != null) {
                System.out.println("|" + " ".repeat(positions[i]) + lanes[i].getSymbol());
            } else {
                System.out.println("|");
            }
        }
        System.out.println("=".repeat(raceLength));
    }

    private void displayResults(int[] positions) {
        System.out.println("Race Finished! Results:");
        List<Horse> sortedHorses = new ArrayList<>();
        for (int i = 0; i < numLanes; i++) {
            if (lanes[i] != null && !lanes[i].isEliminated()) {
                sortedHorses.add(lanes[i]);
            }
        }
        sortedHorses.sort(Comparator.comparingInt(h -> -positions[Arrays.asList(lanes).indexOf(h)]));
        
        for (int i = 0; i < sortedHorses.size(); i++) {
            sortedHorses.get(i).winRace();
            System.out.println((i + 1) + ". " + sortedHorses.get(i).getName());
        }
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
