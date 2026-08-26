import java.util.*;

public class Faltung {

    // Position of an amino acid on the 2D grid
    static class Position {
        int x;
        int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Position)) {
                return false;
            }

            Position other = (Position) obj;
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    // Represents a contact between two amino acids
    static class Contact {
        int index1;
        int index2;

        Contact(int index1, int index2) {
            this.index1 = index1;
            this.index2 = index2;
        }
    }

    // Checks whether two positions are neighbours
    static boolean areNeighbours(Position a, Position b) {
        int dx = Math.abs(a.x - b.x);
        int dy = Math.abs(a.y - b.y);

        return dx + dy == 1;
    }

    // Finds hydrophobic-hydrophobic contacts
    static List<Contact> findHHContacts(
            String sequence,
            Position[] positions) {

        List<Contact> contacts = new ArrayList<>();

        for (int i = 0; i < sequence.length(); i++) {

            // Only hydrophobic amino acids
            if (sequence.charAt(i) != '1') {
                continue;
            }

            for (int j = i + 1; j < sequence.length(); j++) {

                if (sequence.charAt(j) != '1') {
                    continue;
                }

                // Ignore consecutive amino acids
                if (j == i + 1) {
                    continue;
                }

                if (areNeighbours(positions[i], positions[j])) {
                    contacts.add(new Contact(i, j));
                }
            }
        }

        return contacts;
    }

    // Finds overlapping positions
    static List<Contact> findOverlaps(Position[] positions) {

        List<Contact> overlaps = new ArrayList<>();

        for (int i = 0; i < positions.length; i++) {
            for (int j = i + 1; j < positions.length; j++) {

                if (positions[i].equals(positions[j])) {
                    overlaps.add(new Contact(i, j));
                }
            }
        }

        return overlaps;
    }

    // FITNESS FUNCTION
    static double fitness(
            String sequence,
            Position[] positions) {

        int hhContacts =
                findHHContacts(sequence, positions).size();

        int overlaps =
                findOverlaps(positions).size();
        double fitness= hhContacts - overlaps*10.0;
        if(fitness<0){
        return fitness* - 0.01;
    } else
            return fitness;
    }

    public static void main(String[] args) {

        String sequence = "10110001";

        Position[] fold1 = {
                new Position(2, 0),
                new Position(2, 1),
                new Position(1, 1),
                new Position(1, 0),
                new Position(0, 0),
                new Position(0, 1),
                new Position(0, 2),
                new Position(1, 2)
        };

        Position[] fold2 = {
                new Position(1, 1),
                new Position(1, 2),
                new Position(0, 2),
                new Position(0, 1),
                new Position(-1, 1),
                new Position(-1, 0),
                new Position(0, 0),
                new Position(0, 1)
        };

        System.out.println("Fitness Faltung 1: "
                + fitness(sequence, fold1));

        System.out.println("Fitness Faltung 2: "
                + fitness(sequence, fold2));
    }
}