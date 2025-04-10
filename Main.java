 public class Main {
    public static void main(String[] args) {
        // Create horses
        Horse horse1 = new Horse('♘', "PIPPI LONGSTOCKING", 0.7);
        Horse horse2 = new Horse('♞', "KOKOMO", 0.8);
        Horse horse3 = new Horse('1', "EL JEFE", 0.5);

        // Create race with length 30 meters and 3 lanes
        Race race = new Race(10, 3);

        // Add horses to race
        race.addHorse(horse1, 0); // Horse 1 in lane 0
        race.addHorse(horse2, 1); // Horse 2 in lane 1
        race.addHorse(horse3, 2); // Horse 3 in lane 2

        // Start the race
        race.startRace();
    }
}