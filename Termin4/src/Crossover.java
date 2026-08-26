import java.util.Random;

public class Crossover {
    private static final Random random = new Random();
    public static String[] mate(String parent1, String parent2, double crossoverRate) {
        if (random.nextDouble() > crossoverRate) {
            return new String[]{parent1, parent2};
        }
        int len = parent1.length();
        int cutPoint = 1 + random.nextInt(len - 1);
        String child1 = parent1.substring(0, cutPoint) + parent2.substring(cutPoint);
        String child2 = parent2.substring(0, cutPoint) + parent1.substring(cutPoint);
        return new String[]{child1, child2};
    }
}