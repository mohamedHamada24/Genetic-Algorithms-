import java.util.HashMap;
import java.util.Map;

public class FitnessCalculator {

    public static void evaluate(Faltung faltung) {
        int[][] coordinates = faltung.getCoordinates();
        String sequence = faltung.getSequence();
        int n = coordinates.length;

        // Reset previous evaluation metrics
        faltung.setOverlapsCount(0);
        faltung.setHhContactsCount(0);
        faltung.getHhPairs().clear();
        faltung.getOverlapPairs().clear();

        // 1. Detect Overlaps
        Map<String, Integer> positionMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String posKey = coordinates[i][0] + "," + coordinates[i][1];
            if (positionMap.containsKey(posKey)) {
                faltung.setOverlapsCount(faltung.getOverlapsCount() + 1);
                int prevIndex = positionMap.get(posKey);
                faltung.getOverlapPairs().add("Index " + prevIndex + " and Index " + i);
            } else {
                positionMap.put(posKey, i);
            }
        }

        // 2. Detect Non-Sequential HH Contacts (Pure Coordinate Distance)
        for (int i = 0; i < n; i++) {
            if (sequence.charAt(i) != '1') continue;

            for (int j = i + 2; j < n; j++) {
                if (sequence.charAt(j) == '1') {

                    // Calculate distance directly from coordinates array
                    int dx = Math.abs(coordinates[i][0] - coordinates[j][0]);
                    int dy = Math.abs(coordinates[i][1] - coordinates[j][1]);

                    if (dx + dy == 1) { // Distance = 1
                        faltung.setHhContactsCount(faltung.getHhContactsCount() + 1);
                        faltung.getHhPairs().add("Index " + i + " and Index " + j);
                    }
                }
            }
        }

        // 3. Calculate Fitness using Exponential Penalty
        double gamma = 1.0;
        double epsilon = 0.1;
        double fitness = (faltung.getHhContactsCount() + epsilon) * Math.exp(-gamma * faltung.getOverlapsCount());

        faltung.setFitness(fitness);
    }

    public static void main(String[] args) {
        String testSequence = "HPPHPPPH";

        // Create Faltung objects[cite: 2]
        Faltung faltung1 = new Faltung(testSequence, "NWSWNNE");
        Faltung faltung2 = new Faltung(testSequence, "NWSWSEN");

        // Evaluate both
        evaluate(faltung1);
        evaluate(faltung2);

        // Print results
        printResult("Faltung 1", faltung1);
        printResult("Faltung 2", faltung2);
    }

    private static void printResult(String name, Faltung faltung) {
        System.out.println("=== " + name + " ===");
        System.out.printf("Fitness: %.5f\n", faltung.getFitness());
        System.out.println("HH-Contacts: " + faltung.getHhContactsCount() + " " + faltung.getHhPairs());
        System.out.println("Overlaps: " + faltung.getOverlapsCount() + " " + faltung.getOverlapPairs());
        System.out.println();
    }
}