import java.util.HashMap;
import java.util.Map;

/**
 * FitnessRechner - CORRECTED
 *
 * OVERLAPS ONLY for HYDROPHOBIC (BLACK) amino acids
 * - Multiple BLACK amino acids at same position = overlap (counts)
 * - Multiple WHITE amino acids at same position = NOT counted (shouldn't happen)
 * - Mixed at same position = NOT counted
 */
public class FitnessRechner {

    public void berechneFitness(Faltung faltung) {
        faltung.hhKontakte = 0;
        faltung.ueberlappungen = 0;

        // ============================================================
        // STEP 1: Count OVERLAPS - only for HYDROPHOBIC amino acids
        // ============================================================
        Map<String, Integer> hydrophobicCount = new HashMap<>();

        for (int i = 0; i < faltung.getLänge(); i++) {
            // ONLY count HYDROPHOBIC (BLACK) amino acids for overlaps
            if (!faltung.istHydrophob(i)) continue;
            if (!faltung.hatPosition(i)) continue;

            int[] pos = faltung.getPosition(i);
            String key = pos[0] + "," + pos[1];

            int count = hydrophobicCount.getOrDefault(key, 0);
            hydrophobicCount.put(key, count + 1);
        }

        // Count overlaps: positions with 2+ HYDROPHOBIC amino acids
        for (int count : hydrophobicCount.values()) {
            if (count > 1) {
                // 2 BLACK at same position = 1 overlap
                // 3 BLACK at same position = 2 overlaps
                faltung.ueberlappungen += (count - 1);
            }
        }

        // ============================================================
        // STEP 2: Count H-H CONTACTS
        // H-H contact = two HYDROPHOBIC amino acids that are:
        //   1. Adjacent on GRID (Manhattan distance = 1)
        //   2. NOT adjacent in SEQUENCE (index difference >= 2)
        // ============================================================
        for (int i = 0; i < faltung.getLänge(); i++) {
            if (!faltung.istHydrophob(i)) continue;
            if (!faltung.hatPosition(i)) continue;

            int[] pos1 = faltung.getPosition(i);

            for (int j = i + 2; j < faltung.getLänge(); j++) {
                if (!faltung.istHydrophob(j)) continue;
                if (!faltung.hatPosition(j)) continue;

                int[] pos2 = faltung.getPosition(j);

                // Manhattan distance = 1 means adjacent on grid
                int distance = Math.abs(pos1[0] - pos2[0]) + Math.abs(pos1[1] - pos2[1]);

                if (distance == 1) {
                    faltung.hhKontakte++;
                }
            }
        }

        // ============================================================
        // STEP 3: Calculate FITNESS
        // Fitness = H-H Contacts - (2 × Overlaps)
        // Never negative
        // ============================================================
        double rawFitness = faltung.hhKontakte - (2.0 * faltung.ueberlappungen);
        faltung.fitness = Math.max(0.0, rawFitness);
    }
}
