import java.util.Random;

public class Mutation {
    private static final Random random = new Random();
    private static final char[] DIRECTIONS = {'N', 'S', 'E', 'W'};

    /**
     * Applies Point Mutation to a direction string.
     */
    public static String mutate(String directions, double mutationRate) {
        char[] chars = directions.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            if (random.nextDouble() < mutationRate) {
                // Pick a new random direction
                chars[i] = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
            }
        }
        return new String(chars);
    }
}