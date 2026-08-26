import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MainTermin3 {

    public static void main(String[] args) {
        String sequence = Examples.SEQ20; // Test with SEQ20 first
        int directionLength = sequence.length() - 1;
        int popSize = 100;
        int maxGenerations = 100;

        double crossoverRate = 0.8; // 80% chance to crossover
        double mutationRate = 0.05;  // 5% mutation rate per gene

        // 1. Initial Population
        List<Faltung> population = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            String dirs = PopulationGenerator.generateRandomDirections(directionLength);
            Faltung f = new Faltung(sequence, dirs);
            FitnessCalculator.evaluate(f);
            population.add(f);
        }

        Faltung bestEver = getBest(population);

        try (PrintWriter writer = new PrintWriter(new FileWriter("ga_log_termin3.csv"))) {
            writer.println("Generation;AvgFitness;BestGenFitness;BestEverFitness;BestEverHH;BestEverOverlaps");

            for (int gen = 0; gen <= maxGenerations; gen++) {

                // Stats collection
                double totalFitness = 0.0;
                Faltung bestInGen = population.get(0);
                for (Faltung f : population) {
                    totalFitness += f.getFitness();
                    if (f.getFitness() > bestInGen.getFitness()) {
                        bestInGen = f;
                    }
                }

                if (bestInGen.getFitness() > bestEver.getFitness()) {
                    bestEver = bestInGen;
                }

                // Write Log
                writer.printf("%d;%.6f;%.6f;%.6f;%d;%d\n",
                        gen, totalFitness / popSize, bestInGen.getFitness(),
                        bestEver.getFitness(), bestEver.getHhContactsCount(), bestEver.getOverlapsCount());

                if (gen == maxGenerations) break;

                // 2. Selection
                List<Faltung> parents = Selection.selectNextGeneration(population);
                List<Faltung> nextGen = new ArrayList<>();

                // 3. Crossover & Mutation
                for (int i = 0; i < popSize; i += 2) {
                    Faltung p1 = parents.get(i);
                    Faltung p2 = parents.get((i + 1) % popSize);

                    String[] childrenDirs = Crossover.mate(p1.getDirections(), p2.getDirections(), crossoverRate);

                    String child1Dirs = Mutation.mutate(childrenDirs[0], mutationRate);
                    String child2Dirs = Mutation.mutate(childrenDirs[1], mutationRate);

                    Faltung c1 = new Faltung(sequence, child1Dirs);
                    Faltung c2 = new Faltung(sequence, child2Dirs);

                    FitnessCalculator.evaluate(c1);
                    FitnessCalculator.evaluate(c2);

                    nextGen.add(c1);
                    nextGen.add(c2);
                }

                population = nextGen;
            }

            System.out.println("Termin 3 GA Run Completed!");
            System.out.println("Best Ever Fitness: " + bestEver.getFitness());
            System.out.println("Best Ever Overlaps: " + bestEver.getOverlapsCount());
            System.out.println("Best Ever HH Contacts: " + bestEver.getHhContactsCount());

            // 4. Render final fold visual PNG
            Visualization.drawFaltung(bestEver, "best_fold_termin3.png");

        } catch (IOException e) {
            e.printStackTrace();
        }
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