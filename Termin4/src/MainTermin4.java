import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainTermin4 {

    public enum SelectionType { ROULETTE_WHEEL, TOURNAMENT }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SelectionType selectedMethod = null;


        while (selectedMethod == null) {
            System.out.print("Choose selection method -> Enter 'w' for Roulette Wheel or 't' for Tournament: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("w")) {
                selectedMethod = SelectionType.ROULETTE_WHEEL;
            } else if (input.equals("t")) {
                selectedMethod = SelectionType.TOURNAMENT;
            } else {
                System.out.println("Invalid choice! Please type 'w' or 't'.");
            }
        }

        System.out.println("Starting GA with mode: " + selectedMethod + "...\n");


        long startTime = System.currentTimeMillis();


        String sequence = Examples.SEQ36;
        int directionLength = sequence.length() - 1;
        int popSize = 200;
        int maxGenerations = 250;
        int tournamentSize = 5;

        double crossoverRate = 0.85;
        double initialMutationRate = 0.15;
        double finalMutationRate = 0.01;

        List<Faltung> population = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            String dirs = PopulationGenerator.generateRandomDirections(directionLength);
            Faltung f = new Faltung(sequence, dirs);
            FitnessCalculator.evaluate(f);
            population.add(f);
        }

        Faltung bestEver = getBest(population);

        try (PrintWriter writer = new PrintWriter(new FileWriter("ga_log_termin4.csv"))) {
            writer.println("Generation;AvgFitness;BestGenFitness;BestEverFitness;BestEverHH;BestEverOverlaps;MutationRate");

            for (int gen = 0; gen <= maxGenerations; gen++) {

                double currentMutationRate = Mutation.calculateMutationRate(
                        gen, maxGenerations, initialMutationRate, finalMutationRate);


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

                writer.printf("%d;%.6f;%.6f;%.6f;%d;%d;%.6f\n",
                        gen, totalFitness / popSize, bestInGen.getFitness(),
                        bestEver.getFitness(), bestEver.getHhContactsCount(),
                        bestEver.getOverlapsCount(), currentMutationRate);

                if (gen == maxGenerations) break;


                List<Faltung> nextGen = new ArrayList<>();

                if (selectedMethod == SelectionType.ROULETTE_WHEEL) {
                    List<Faltung> parentsPool = Selection.selectNextGeneration(population);

                    for (int i = 0; i < popSize; i += 2) {
                        Faltung p1 = parentsPool.get(i);
                        Faltung p2 = parentsPool.get((i + 1) % popSize);

                        String[] childrenDirs = Crossover.mate(p1.getDirections(), p2.getDirections(), crossoverRate);
                        String child1Dirs = Mutation.mutate(childrenDirs[0], currentMutationRate);
                        String child2Dirs = Mutation.mutate(childrenDirs[1], currentMutationRate);

                        Faltung c1 = new Faltung(sequence, child1Dirs);
                        Faltung c2 = new Faltung(sequence, child2Dirs);

                        FitnessCalculator.evaluate(c1);
                        FitnessCalculator.evaluate(c2);

                        nextGen.add(c1);
                        if (nextGen.size() < popSize) nextGen.add(c2);
                    }
                } else {
                    while (nextGen.size() < popSize) {
                        Faltung p1 = Selection.tournamentSelect(population, tournamentSize);
                        Faltung p2 = Selection.tournamentSelect(population, tournamentSize);

                        String[] childrenDirs = Crossover.mate(p1.getDirections(), p2.getDirections(), crossoverRate);
                        String child1Dirs = Mutation.mutate(childrenDirs[0], currentMutationRate);
                        String child2Dirs = Mutation.mutate(childrenDirs[1], currentMutationRate);

                        Faltung c1 = new Faltung(sequence, child1Dirs);
                        Faltung c2 = new Faltung(sequence, child2Dirs);

                        FitnessCalculator.evaluate(c1);
                        FitnessCalculator.evaluate(c2);

                        nextGen.add(c1);
                        if (nextGen.size() < popSize) nextGen.add(c2);
                    }
                }

                population = nextGen;
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Termin 4 Run Completed using mode: " + selectedMethod);
            System.out.println("Execution Time: " + (endTime - startTime) + " ms");
            System.out.println("Best Ever Fitness: " + bestEver.getFitness());

            Visualization.drawFaltung(bestEver, "best_fold_termin4.png");

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