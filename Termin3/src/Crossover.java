import java.util.Random;

public class Crossover {
    private static final Random random = new Random();

    /**
     * Performs 1-Point Crossover between two parent strings.
     * Returns an array containing two offspring direction strings.
     */
    public static String[] mate(String parent1, String parent2, double crossoverRate) {
        if (random.nextDouble() > crossoverRate) {
            return new String[]{parent1, parent2}; // No crossover occurs
        }

        int len = parent1.length();
        // Pick a cut point between index 1 and len - 1
        int cutPoint = 1 + random.nextInt(len - 1);

        String child1 = parent1.substring(0, cutPoint) + parent2.substring(cutPoint);
        String child2 = parent2.substring(0, cutPoint) + parent1.substring(cutPoint);

        return new String[]{child1, child2};
    }
}