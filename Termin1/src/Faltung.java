import java.util.*;import java.util.*;

public class Faltung  {

    // Sequence for the test: 0:H, 1:P, 2:H, 3:H, 4:P, 5:P, 6:P, 7:H
    private static final String SEQUENCE = "HPPHPPPH";

    // Converts directions ("N", "S", "E", "W") into (x, y) coordinates
    public static int[][] directionsToCoordinates(String directions) {
        int n = directions.length() + 1; // Number of amino acids
        int[][] coords = new int[n][2];

        // Fix starting amino acid (index 0) at origin (0, 0)
        coords[0] = new int[]{0, 0};

        int currX = 0;
        int currY = 0;

        for (int i = 0; i < directions.length(); i++) {
            char dir = directions.charAt(i);
            switch (dir) {
                case 'N': currY += 1; break;
                case 'S': currY -= 1; break;
                case 'E': currX += 1; break;
                case 'W': currX -= 1; break;
                default: throw new IllegalArgumentException("Invalid direction: " + dir);
            }
            coords[i + 1] = new int[]{currX, currY};
        }
        return coords;
    }

    public static EvaluationResult evaluate(String directions) {
        int[][] coordinates = directionsToCoordinates(directions);

        EvaluationResult res = new EvaluationResult();
        int n = coordinates.length;

        // 1. Detect Overlaps
        Map<String, Integer> positionMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String posKey = coordinates[i][0] + "," + coordinates[i][1];
            if (positionMap.containsKey(posKey)) {
                res.overlapsCount++;
                int prevIndex = positionMap.get(posKey);
                res.overlapPairs.add("Index " + prevIndex + " and Index " + i);
            } else {
                positionMap.put(posKey, i);
            }
        }

        // 2. Detect Non-Sequential HH Contacts
        for (int i = 0; i < n; i++) {
            if (SEQUENCE.charAt(i) != 'H') continue;
            for (int j = i + 2; j < n; j++) {
                if (SEQUENCE.charAt(j) == 'H') {
                    int dx = Math.abs(coordinates[i][0] - coordinates[j][0]);
                    int dy = Math.abs(coordinates[i][1] - coordinates[j][1]);
                    if (dx + dy == 1) { // Adjacent on lattice grid
                        res.hhContactsCount++;
                        res.hhPairs.add("Index " + i + " and Index " + j);
                    }
                }
            }
        }

        // 3. Exponential Penalty Calculation
        double gamma = 1.0;
        double epsilon = 0.1;
        res.fitness = (res.hhContactsCount + epsilon) * Math.exp(-gamma * res.overlapsCount);

        return res;
    }

    public static class EvaluationResult {
        public double fitness;
        public int hhContactsCount;
        public int overlapsCount;
        public List<String> hhPairs = new ArrayList<>();
        public List<String> overlapPairs = new ArrayList<>();
    }

    public static void main(String[] args) {
        // Faltung 1 directions: N, W, S, W, N, N, E
        String Faltung = "NWSWNNE".replace(" ", ""); // "NWSWNNE"

        // Faltung 2 directions: N, W, S, W, N, E, S (Index 7 moves South back onto Index 3)
        String faltung2 = "NWSWSEN";

        printResult("Faltung 1", evaluate(Faltung));
        printResult("Faltung 2", evaluate(faltung2));
    }

    private static void printResult(String name, EvaluationResult res) {
        System.out.println("=== " + name + " ===");
        System.out.printf("Fitness: %.5f\n", res.fitness);
        System.out.println("HH-Contacts: " + res.hhContactsCount + " " + res.hhPairs);
        System.out.println("Overlaps: " + res.overlapsCount + " " + res.overlapPairs);
        System.out.println();
    }
}