import java.util.ArrayList;
import java.util.List;

public class Faltung {
    private String sequence; // Stores raw string like "101001..."
    private String directions;
    private int[][] coordinates;


    private double fitness;
    private int hhContactsCount;
    private int overlapsCount;
    private List<String> hhPairs = new ArrayList<>();
    private List<String> overlapPairs = new ArrayList<>();

    public Faltung(String sequence, String directions) {
        this.sequence = sequence;
        this.directions = directions;
        this.coordinates = calculateCoordinates();
    }

    private int[][] calculateCoordinates() {
        int n = directions.length() + 1;
        int[][] coords = new int[n][2];
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

    // Getters and Setters
    public String getSequence() { return sequence; }
    public String getDirections() { return directions; }
    public int[][] getCoordinates() { return coordinates; }

    public double getFitness() { return fitness; }
    public void setFitness(double fitness) { this.fitness = fitness; }

    public int getHhContactsCount() { return hhContactsCount; }
    public void setHhContactsCount(int hhContactsCount) { this.hhContactsCount = hhContactsCount; }

    public int getOverlapsCount() { return overlapsCount; }
    public void setOverlapsCount(int overlapsCount) { this.overlapsCount = overlapsCount; }

    public List<String> getHhPairs() { return hhPairs; }
    public List<String> getOverlapPairs() { return overlapPairs; }
}