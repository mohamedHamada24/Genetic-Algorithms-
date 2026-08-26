import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Selection {
    private static final Random random = new Random();

    /**
     * Performs Fitness-Proportional (Roulette Wheel) Selection.
     * Selects popSize individuals from the current population based on their fitness.
     */
    public static List<Faltung> selectNextGeneration(List<Faltung> currentPop) {
        List<Faltung> nextGen = new ArrayList<>();
        int popSize = currentPop.size();

        // 1. Calculate total population fitness
        double totalFitness = 0.0;
        for (Faltung candidate : currentPop) {
            totalFitness += candidate.getFitness();
        }

        // 2. Spin the roulette wheel N times
        for (int i = 0; i < popSize; i++) {
            double randVal = random.nextDouble() * totalFitness;
            double runningSum = 0.0;

            for (Faltung candidate : currentPop) {
                runningSum += candidate.getFitness();
                if (runningSum >= randVal) {
                    // Create a clean copy of the selected candidate
                    Faltung selectedCopy = new Faltung(candidate.getSequence(), candidate.getDirections());
                    selectedCopy.setFitness(candidate.getFitness());
                    selectedCopy.setHhContactsCount(candidate.getHhContactsCount());
                    selectedCopy.setOverlapsCount(candidate.getOverlapsCount());

                    nextGen.add(selectedCopy);
                    break;
                }
            }
        }
        return nextGen;
    }

    /**
     * Performs Tournament Selection for Termin 4.
     * Picks k random individuals and returns a clean copy of the fittest one.
     */
    public static Faltung tournamentSelect(List<Faltung> population, int tournamentSize) {
        Faltung best = null;

        for (int i = 0; i < tournamentSize; i++) {
            Faltung randomIndividual = population.get(random.nextInt(population.size()));
            if (best == null || randomIndividual.getFitness() > best.getFitness()) {
                best = randomIndividual;
            }
        }

        // Return a fresh copy of the tournament winner
        Faltung winnerCopy = new Faltung(best.getSequence(), best.getDirections());
        winnerCopy.setFitness(best.getFitness());
        winnerCopy.setHhContactsCount(best.getHhContactsCount());
        winnerCopy.setOverlapsCount(best.getOverlapsCount());

        return winnerCopy;
    }
}