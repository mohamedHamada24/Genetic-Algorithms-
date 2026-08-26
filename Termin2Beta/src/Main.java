import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
            String targetSequence = Examples.SEQ20; // 20 amino acids long
            int directionLength = targetSequence.length() - 1; // 19 directions needed
            int popSize = 100;

            List<Faltung> population = new ArrayList<>();

            double totalFitness = 0.0;
            int totalOverlaps = 0;
            int totalHhContacts = 0;

            Faltung bestCandidate = null;


            for (int i = 0; i < popSize; i++) {
                String randomDirections = PopulationGenerator.generateRandomDirections(directionLength);
                Faltung candidate = new Faltung(targetSequence, randomDirections);


                FitnessCalculator.evaluate(candidate);
                population.add(candidate);

                totalFitness += candidate.getFitness();
                totalOverlaps += candidate.getOverlapsCount();
                totalHhContacts += candidate.getHhContactsCount();


                if (bestCandidate == null || candidate.getFitness() > bestCandidate.getFitness()) {
                    bestCandidate = candidate;
                }
            } }
}

