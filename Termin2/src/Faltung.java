import java.util.HashMap;
import java.util.Map;

public class Faltung implements Cloneable {
    private String sequenz;
    private Map<Integer, int[]> gitter;
    public double fitness;
    public int hhKontakte;
    public int ueberlappungen;

    public Faltung(String sequenz) {
        this.sequenz = sequenz;
        this.gitter = new HashMap<>();
        this.fitness = 0;
        this.hhKontakte = 0;
        this.ueberlappungen = 0;
    }

    public String getSequenz() {
        return sequenz;
    }

    public int getLänge() {
        return sequenz.length();
    }

    public boolean istHydrophob(int index) {
        return sequenz.charAt(index) == '1';
    }

    public void setzePosition(int index, int x, int y) {
        gitter.put(index, new int[]{x, y});
    }

    public int[] getPosition(int index) {
        return gitter.get(index);
    }

    public Map<Integer, int[]> getGitter() {
        return gitter;
    }

    public boolean hatPosition(int index) {
        return gitter.containsKey(index);
    }

    /**
     * Random walk with conditional overlap:
     * - BLACK (1/Hydrophobic): Simple random walk, overlaps CAN happen naturally
     * - WHITE (0/Polar): Try to avoid overlaps by finding free position
     */
    public void zufälligeFaltung() {
        gitter.clear();
        setzePosition(0, 0, 0);

        int currentX = 0;
        int currentY = 0;
        Map<String, Boolean> occupiedByWhite = new HashMap<>();

        // Mark initial position if it's WHITE
        if (!istHydrophob(0)) {
            occupiedByWhite.put("0,0", true);
        }

        for (int i = 1; i < sequenz.length(); i++) {
            if (istHydrophob(i)) {
                // BLACK (1) amino acid - Simple random walk
                // Overlaps CAN happen naturally, but not forced
                int richtung = (int)(Math.random() * 4);
                int newX = currentX;
                int newY = currentY;

                switch(richtung) {
                    case 0: newY++; break;  // Up
                    case 1: newX++; break;  // Right
                    case 2: newY--; break;  // Down
                    case 3: newX--; break;  // Left
                }

                // Place BLACK amino acid (may or may not overlap)
                setzePosition(i, newX, newY);
                currentX = newX;
                currentY = newY;
            } else {
                // WHITE (0) amino acid - Try to find free position
                boolean platziert = false;
                int versuche = 0;
                int maxVersuche = 4;

                int newX = currentX;
                int newY = currentY;

                // Try all 4 directions to find free position
                while (!platziert && versuche < maxVersuche) {
                    int richtung = (int)(Math.random() * 4);
                    newX = currentX;
                    newY = currentY;

                    switch(richtung) {
                        case 0: newY++; break;  // Up
                        case 1: newX++; break;  // Right
                        case 2: newY--; break;  // Down
                        case 3: newX--; break;  // Left
                    }

                    String key = newX + "," + newY;

                    // Check if position is free (not occupied by another WHITE)
                    if (!occupiedByWhite.containsKey(key)) {
                        // Position is free - place WHITE amino acid
                        setzePosition(i, newX, newY);
                        occupiedByWhite.put(key, true);
                        currentX = newX;
                        currentY = newY;
                        platziert = true;
                    } else {
                        // Position occupied by WHITE - try next direction
                        versuche++;
                    }
                }

                // If all 4 directions blocked for WHITE, still place it anyway
                if (!platziert) {
                    int richtung = (int)(Math.random() * 4);
                    switch(richtung) {
                        case 0: currentY++; break;
                        case 1: currentX++; break;
                        case 2: currentY--; break;
                        case 3: currentX--; break;
                    }

                    setzePosition(i, currentX, currentY);
                    occupiedByWhite.put(currentX + "," + currentY, true);
                }
            }
        }
    }

    @Override
    public Faltung clone() {
        Faltung klon = new Faltung(this.sequenz);
        klon.gitter = new HashMap<>(this.gitter);
        klon.fitness = this.fitness;
        klon.hhKontakte = this.hhKontakte;
        klon.ueberlappungen = this.ueberlappungen;
        return klon;
    }
}
