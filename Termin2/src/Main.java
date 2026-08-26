import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        String sequenz = Beispiele.SEQ20;

        System.out.println("=== TERMIN 2: Genetische Algorithmen ===");
        System.out.println("Sequenz: " + sequenz);
        System.out.println("Länge: " + sequenz.length());
        System.out.println();

        GenetischerAlgorithmus ga = new GenetischerAlgorithmus(sequenz);
        Protokollierer protokoll = new Protokollierer("ausgabe/ga_log.csv");

        int populationsgröße = 100;
        int generationen = 100;

        System.out.printf("Initialisiere Population von %d Individuen...\n", populationsgröße);
        ga.initialisiere(populationsgröße);

        System.out.printf("Laufe %d Generationen mit fitness-proportionaler Selektion...\n\n", generationen);
        System.out.println("Gen\tDurchn.\tBestGen\tBestAll\tHH\tÜberlp.");
        System.out.println("---\t------\t-------\t-------\t--\t-------");

        for (int gen = 0; gen < generationen; gen++) {
            ga.laufeGeneration();

            protokoll.schreibeGeneration(gen, ga);

            if (gen % 10 == 0) {
                Faltung beste = ga.getBestesIndividuum();
                System.out.printf("%d\t%.2f\t%.2f\t%.2f\t%d\t%d\n",
                        gen,
                        ga.getDurchschnittlicheFitness(),
                        beste.fitness,
                        ga.getBesteGesamt().fitness,
                        ga.getBesteGesamt().hhKontakte,
                        ga.getBesteGesamt().ueberlappungen);
            }

            if (gen % 20 == 0 || gen == generationen - 1) {
                Visualisierung.zeichneFaltung(
                        ga.getBestesIndividuum(),
                        String.format("ausgabe/faltungen/generation_%03d.png", gen)
                );
            }
        }

        Visualisierung.zeichneFaltung(
                ga.getBesteGesamt(),
                "ausgabe/faltungen/beste_gesamt.png"
        );

        protokoll.schließe();

        System.out.println("\n=== ERGEBNISSE ===");
        Faltung beste = ga.getBesteGesamt();
        System.out.printf("Beste Fitness: %.2f\n", beste.fitness);
        System.out.printf("H-H Kontakte: %d\n", beste.hhKontakte);
        System.out.printf("Überlappungen: %d\n", beste.ueberlappungen);
        System.out.println("Protokolldatei: ausgabe/ga_log.csv");
        System.out.println("Beste Faltung: ausgabe/faltungen/beste_gesamt.png");
        System.out.println("\nTERMIN 2 FERTIGGESTELLT!");
    }
}

