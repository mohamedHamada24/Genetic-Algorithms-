
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenetischerAlgorithmus {
    private List<Faltung> population;
    private FitnessRechner fitnessRechner;
    private String sequenz;
    private int generationszaehler;
    private Faltung besteGesamt;

    public GenetischerAlgorithmus(String sequenz) {
        this.sequenz = sequenz;
        this.population = new ArrayList<>();
        this.fitnessRechner = new FitnessRechner();
        this.generationszaehler = 0;
        this.besteGesamt = null;
    }

    public void initialisiere(int populationsgröße) {
        population.clear();
        for (int i = 0; i < populationsgröße; i++) {
            Faltung faltung = new Faltung(sequenz);
            faltung.zufälligeFaltung();
            fitnessRechner.berechneFitness(faltung);
            population.add(faltung);

            if (besteGesamt == null || faltung.fitness > besteGesamt.fitness) {
                besteGesamt = faltung.clone();
            }
        }
    }

    public void laufeGeneration() {
        for (Faltung faltung : population) {
            fitnessRechner.berechneFitness(faltung);

            if (faltung.fitness > besteGesamt.fitness) {
                besteGesamt = faltung.clone();
            }
        }

        double gesamtfitness = 0;
        double minFitness = Double.MAX_VALUE;

        for (Faltung faltung : population) {
            if (faltung.fitness < minFitness) {
                minFitness = faltung.fitness;
            }
        }

        double versatz = (minFitness < 0) ? -minFitness + 1 : 0;

        for (Faltung faltung : population) {
            gesamtfitness += (faltung.fitness + versatz);
        }

        List<Faltung> neuePopulation = new ArrayList<>();
        Random zufall = new Random();

        for (int i = 0; i < population.size(); i++) {
            double r = zufall.nextDouble() * gesamtfitness;
            double summe = 0;

            for (Faltung faltung : population) {
                summe += (faltung.fitness + versatz);
                if (summe >= r) {
                    neuePopulation.add(faltung.clone());
                    break;
                }
            }
        }

        population = neuePopulation;
        generationszaehler++;
    }

    public double getDurchschnittlicheFitness() {
        return population.stream()
                .mapToDouble(f -> f.fitness)
                .average()
                .orElse(0);
    }

    public Faltung getBestesIndividuum() {
        return population.stream()
                .max((f1, f2) -> Double.compare(f1.fitness, f2.fitness))
                .orElse(null);
    }

    public Faltung getBesteGesamt() {
        return besteGesamt;
    }

    public List<Faltung> getPopulation() {
        return population;
    }

    public int getGenerationszaehler() {
        return generationszaehler;
    }
}