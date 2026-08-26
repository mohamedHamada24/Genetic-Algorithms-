 import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MainTermin2 {

    public static void main(String[] args) {
        String sequence = Examples.SEQ20; // "10100110100101100101"
        int directionLength = sequence.length() - 1; // 19
        int popSize = 100;
        int maxGenerations = 100;


        List<Faltung> population = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            String randomDirs = PopulationGenerator.generateRandomDirections(directionLength);
            Faltung f = new Faltung(sequence, randomDirs);
            FitnessCalculator.evaluate(f);
            population.add(f);
        }


        Faltung bestEver = getBest(population);


        try (PrintWriter writer = new PrintWriter(new FileWriter("ga_log.csv"))) {

            writer.println("Generation;AvgFitness;BestGenFitness;BestEverFitness;BestEverHH;BestEverOverlaps");

            for (int gen = 0; gen <= maxGenerations; gen++) {


                double totalFitness = 0.0;
                Faltung bestInGen = population.get(0);

                for (Faltung f : population) {
                    totalFitness += f.getFitness();
                    if (f.getFitness() > bestInGen.getFitness()) {
                        bestInGen = f;
                    }
                }

                double avgFitness = totalFitness / popSize;


                if (bestInGen.getFitness() > bestEver.getFitness()) {
                    bestEver = bestInGen;
                }


                writer.printf("%d;%.6f;%.6f;%.6f;%d;%d\n",
                        gen, avgFitness, bestInGen.getFitness(),
                        bestEver.getFitness(), bestEver.getHhContactsCount(), bestEver.getOverlapsCount());


                if (gen == maxGenerations) break;


                population = Selection.selectNextGeneration(population);
            }

            System.out.println("Finished 100 Generations!");
            System.out.println("Log successfully saved to 'ga_log.csv'");

        } catch (IOException e) {
            System.err.println("Error writing to CSV: " + e.getMessage());
        }


        Visualization.drawFaltung(bestEver, "best_fold.png");
    }

    private static Faltung getBest(List<Faltung> population) {
        Faltung best = population.get(0);
        for (Faltung f : population) {
            if (f.getFitness() > best.getFitness()) {
                best = f;
            }
        }
        return best;
    }
}
