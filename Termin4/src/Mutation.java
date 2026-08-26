public class Mutation {
    private static final java.util.Random random = new java.util.Random();
    private static final char[] DIRECTIONS = {'N', 'S', 'E', 'W'};


    public static double calculateMutationRate(int currentGen, int maxGen, double startRate, double endRate) {
        return startRate - ((double) currentGen / maxGen) * (startRate - endRate);
    }

    public static String mutate(String directions, double mutationRate) {
        char[] chars = directions.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (random.nextDouble() < mutationRate) {
                chars[i] = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
            }
        }
        return new String(chars);
    }
}