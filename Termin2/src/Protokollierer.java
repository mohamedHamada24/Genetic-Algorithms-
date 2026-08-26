
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Protokollierer {
    private PrintWriter schreiber;

    public Protokollierer(String dateiname) throws IOException {
        File datei = new File(dateiname);
        datei.getParentFile().mkdirs();

        schreiber = new PrintWriter(new FileWriter(datei));
        schreiber.println("Generation\tDurchschnitt_Fitness\tBeste_Gen_Fitness\tBeste_Gesamt_Fitness\tBeste_HH_Kontakte\tBeste_Überlappungen");
    }

    public void schreibeGeneration(int generation, GenetischerAlgorithmus algorithmus) {
        double durchschnitt = algorithmus.getDurchschnittlicheFitness();
        Faltung besteGen = algorithmus.getBestesIndividuum();
        Faltung besteGesamt = algorithmus.getBesteGesamt();

        if (besteGen == null || besteGesamt == null) return;

        schreiber.printf("%d\t%.2f\t%.2f\t%.2f\t%d\t%d\n",
                generation,
                durchschnitt,
                besteGen.fitness,
                besteGesamt.fitness,
                besteGesamt.hhKontakte,
                besteGesamt.ueberlappungen);

        schreiber.flush();
    }

    public void schließe() {
        if (schreiber != null) {
            schreiber.close();
        }
    }
}