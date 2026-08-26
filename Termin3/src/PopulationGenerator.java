import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PopulationGenerator {
    private static final Random random = new Random();
    private static final char[] DIRECTIONS = {'N', 'S', 'E', 'W'};

    /**
     * Generates a random direction string of a given length.
     */
    public static String generateRandomDirections(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(DIRECTIONS[random.nextInt(DIRECTIONS.length)]);
        }
        return sb.toString();
    }}

    /*public static void main(String[] args) {
        String targetSequence = Examples.SEQ20; // 20 amino acids long
        int directionLength = targetSequence.length() - 1; // 19 directions needed
        int popSize = 100;

        List<Faltung> population = new ArrayList<>();

        double totalFitness = 0.0;
        int totalOverlaps = 0;
        int totalHhContacts = 0;

        Faltung bestCandidate = null;

        // 1. Generate and evaluate 100 candidates
        for (int i = 0; i < popSize; i++) {
            String randomDirections = generateRandomDirections(directionLength);
            Faltung candidate = new Faltung(targetSequence, randomDirections);

            // Evaluate candidate
            FitnessCalculator.evaluate(candidate);
            population.add(candidate);

            // Accumulate statistics
            totalFitness += candidate.getFitness();
            totalOverlaps += candidate.getOverlapsCount();
            totalHhContacts += candidate.getHhContactsCount();

            // Track best candidate in generation 0
            if (bestCandidate == null || candidate.getFitness() > bestCandidate.getFitness()) {
                bestCandidate = candidate;
            }
        }

        // 2. Compute averages
        double avgFitness = totalFitness / popSize;
        double avgOverlaps = (double) totalOverlaps / popSize;
        double avgHhContacts = (double) totalHhContacts / popSize;

        // 3. Output results to console
        System.out.println("==================================================");
        System.out.println("      INITIAL POPULATION EVALUATION (100)         ");
        System.out.println("==================================================");
        System.out.printf("Average Fitness:         %.5f\n", avgFitness);
        System.out.printf("Average Overlaps:        %.2f\n", avgOverlaps);
        System.out.printf("Average HH-Contacts:     %.2f\n", avgHhContacts);
        System.out.println("--------------------------------------------------");
        System.out.println("BEST CANDIDATE IN INITIAL POPULATION:");
        System.out.println("Sequence:   " + bestCandidate.getSequence());
        System.out.println("Directions:   " + bestCandidate.getDirections());
        System.out.printf("Best Fitness: %.5f\n", bestCandidate.getFitness());
        System.out.println("HH-Contacts:  " + bestCandidate.getHhContactsCount() + " " + bestCandidate.getHhPairs());
        System.out.println("Overlaps:     " + bestCandidate.getOverlapsCount() + " " + bestCandidate.getOverlapPairs());
        System.out.println("==================================================");

 */