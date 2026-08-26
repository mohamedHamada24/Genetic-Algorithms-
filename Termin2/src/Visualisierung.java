import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public class Visualisierung {

    private static final int CELL_SIZE = 60;      // Larger cells
    private static final int CELL_SPACING = 15;   // Space between cells
    private static final int PADDING = 100;

    public static void zeichneFaltung(Faltung faltung, String dateiPfad) throws IOException {
        // Find grid bounds
        int minX = 0, maxX = 0, minY = 0, maxY = 0;
        boolean first = true;

        for (int i = 0; i < faltung.getLänge(); i++) {
            if (!faltung.hatPosition(i)) continue;

            int[] pos = faltung.getPosition(i);
            if (first) {
                minX = maxX = pos[0];
                minY = maxY = pos[1];
                first = false;
            } else {
                if (pos[0] < minX) minX = pos[0];
                if (pos[0] > maxX) maxX = pos[0];
                if (pos[1] < minY) minY = pos[1];
                if (pos[1] > maxY) maxY = pos[1];
            }
        }

        // Calculate image size with spacing
        int gridWidth = (maxX - minX + 1) * (CELL_SIZE + CELL_SPACING);
        int gridHeight = (maxY - minY + 1) * (CELL_SIZE + CELL_SPACING);
        int width = gridWidth + 2 * PADDING;
        int height = gridHeight + 2 * PADDING + 140;

        BufferedImage bild = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bild.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // White background
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);

        // ============================================================
        // STEP 1: Draw BACKBONE connections (gray lines between sequence neighbors)
        // ============================================================
        g2.setColor(new Color(150, 150, 150, 200));
        g2.setStroke(new java.awt.BasicStroke(2.0f));

        for (int i = 0; i < faltung.getLänge() - 1; i++) {
            if (!faltung.hatPosition(i)) continue;
            if (!faltung.hatPosition(i + 1)) continue;

            int[] pos1 = faltung.getPosition(i);
            int[] pos2 = faltung.getPosition(i + 1);

            int x1 = PADDING + (pos1[0] - minX) * (CELL_SIZE + CELL_SPACING) + CELL_SIZE / 2;
            int y1 = PADDING + (pos1[1] - minY) * (CELL_SIZE + CELL_SPACING) + CELL_SIZE / 2;
            int x2 = PADDING + (pos2[0] - minX) * (CELL_SIZE + CELL_SPACING) + CELL_SIZE / 2;
            int y2 = PADDING + (pos2[1] - minY) * (CELL_SIZE + CELL_SPACING) + CELL_SIZE / 2;

            g2.drawLine(x1, y1, x2, y2);
        }

        // ============================================================
        // STEP 2: Draw H-H contact LINES (RED)
        // Only when BOTH are BLACK AND adjacent on grid AND not sequence neighbors
        // ============================================================

        g2.setStroke(new java.awt.BasicStroke(2.5f));

        // Count actual H-H contacts for verification
        for (int i = 0; i < faltung.getLänge(); i++) {
            if (!faltung.istHydrophob(i)) continue;  // Must be BLACK
            if (!faltung.hatPosition(i)) continue;

            int[] pos1 = faltung.getPosition(i);

            for (int j = i + 2; j < faltung.getLänge(); j++) {  // j must be i+2 or more (not sequence neighbors)
                if (!faltung.istHydrophob(j)) continue;  // Must be BLACK
                if (!faltung.hatPosition(j)) continue;

                int[] pos2 = faltung.getPosition(j);

                // Only draw if adjacent on grid (distance = 1)
                int distance = Math.abs(pos1[0] - pos2[0]) + Math.abs(pos1[1] - pos2[1]);

                if (distance == 1) {
                    // This is a real H-H contact! Draw it!
                    int x1 = PADDING + (pos1[0] - minX) * (CELL_SIZE + CELL_SPACING) + CELL_SIZE / 2;
                    int y1 = PADDING + (pos1[1] - minY) * (CELL_SIZE + CELL_SPACING) + CELL_SIZE / 2;
                    int x2 = PADDING + (pos2[0] - minX) * (CELL_SIZE + CELL_SPACING) + CELL_SIZE / 2;
                    int y2 = PADDING + (pos2[1] - minY) * (CELL_SIZE + CELL_SPACING) + CELL_SIZE / 2;

                    g2.drawLine(x1, y1, x2, y2);
                }
            }
        }

        // ============================================================
        // STEP 3: Group amino acids by position
        // ============================================================
        Map<String, List<Integer>> positionMap = new HashMap<>();

        for (int i = 0; i < faltung.getLänge(); i++) {
            if (!faltung.hatPosition(i)) continue;

            int[] pos = faltung.getPosition(i);
            String key = pos[0] + "," + pos[1];

            List<Integer> aminoAcids = positionMap.getOrDefault(key, new ArrayList<>());
            aminoAcids.add(i);
            positionMap.put(key, aminoAcids);
        }

        // ============================================================
        // STEP 4: Draw CELLS and ALL AMINO ACIDS (including overlaps)
        // ============================================================
        for (String key : positionMap.keySet()) {
            List<Integer> aminoAcids = positionMap.get(key);
            String[] parts = key.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);

            // Calculate position with spacing
            int px = PADDING + (x - minX) * (CELL_SIZE + CELL_SPACING);
            int py = PADDING + (y - minY) * (CELL_SIZE + CELL_SPACING);

            // Draw cell border
            g2.setColor(new Color(120, 120, 120, 220));
            g2.setStroke(new java.awt.BasicStroke(2.0f));
            g2.drawRect(px, py, CELL_SIZE, CELL_SIZE);

            // Determine if any amino acid is hydrophobic
            boolean hasHydrophobic = false;
            for (int idx : aminoAcids) {
                if (faltung.istHydrophob(idx)) {
                    hasHydrophobic = true;
                    break;
                }
            }

            // Fill color - BLACK if any hydrophobic, WHITE otherwise
            if (hasHydrophobic) {
                g2.setColor(new Color(0, 0, 0));  // BLACK
            } else {
                g2.setColor(new Color(240, 240, 240));  // WHITE/GRAY
            }
            g2.fillRect(px + 2, py + 2, CELL_SIZE - 4, CELL_SIZE - 4);

            // ============================================================
            // DISPLAY AMINO ACIDS IN THIS BOX
            // ============================================================
            if (aminoAcids.size() == 1) {
                // Single amino acid - show normally
                int idx = aminoAcids.get(0);
                g2.setColor(faltung.istHydrophob(idx) ? Color.WHITE : Color.BLACK);
                Font font = new Font("Arial", Font.BOLD, 18);
                g2.setFont(font);
                FontMetrics metrics = g2.getFontMetrics();
                String label = String.valueOf(idx);
                int labelWidth = metrics.stringWidth(label);
                int ascent = metrics.getAscent();

                g2.drawString(label,
                        px + CELL_SIZE / 2 - labelWidth / 2,
                        py + CELL_SIZE / 2 + ascent / 3);
            } else {
                // MULTIPLE amino acids in same box
                boolean allBlack = true;
                for (int idx : aminoAcids) {
                    if (!faltung.istHydrophob(idx)) {
                        allBlack = false;
                        break;
                    }
                }

                if (allBlack) {
                    // ALL BLACK - show all with diagonal line
                    g2.setColor(Color.WHITE);
                    Font font = new Font("Arial", Font.BOLD, 11);
                    g2.setFont(font);
                    FontMetrics metrics = g2.getFontMetrics();

                    int yOffset = py + 12;
                    for (int idx : aminoAcids) {
                        String label = String.valueOf(idx);
                        int labelWidth = metrics.stringWidth(label);
                        g2.drawString(label,
                                px + CELL_SIZE / 2 - labelWidth / 2,
                                yOffset);
                        yOffset += 11;
                    }

                    // Draw diagonal line showing overlap
                    g2.setColor(new Color(255, 150, 150, 200));
                    g2.setStroke(new java.awt.BasicStroke(1.5f));
                    g2.drawLine(px + 5, py + CELL_SIZE - 5, px + CELL_SIZE - 5, py + 5);
                } else {
                    // MIXED or WHITE - show only FIRST, NO overlap indicator
                    int idx = aminoAcids.get(0);
                    g2.setColor(faltung.istHydrophob(idx) ? Color.WHITE : Color.BLACK);
                    Font font = new Font("Arial", Font.BOLD, 18);
                    g2.setFont(font);
                    FontMetrics metrics = g2.getFontMetrics();
                    String label = String.valueOf(idx);
                    int labelWidth = metrics.stringWidth(label);
                    int ascent = metrics.getAscent();

                    g2.drawString(label,
                            px + CELL_SIZE / 2 - labelWidth / 2,
                            py + CELL_SIZE / 2 + ascent / 3);
                }
            }
        }

        // ============================================================
        // STEP 5: Draw STATISTICS at bottom
        // ============================================================
        int statsY = gridHeight + PADDING + 110;

        g2.setColor(new Color(240, 240, 240));
        g2.fillRect(0, gridHeight + PADDING + 80, width, 50);

        g2.setColor(Color.BLACK);
        Font statsFont = new Font("Arial", Font.BOLD, 16);
        g2.setFont(statsFont);

        g2.drawString(String.format("Fitness: %.2f", faltung.fitness),
                PADDING, statsY);
        g2.drawString(String.format("H-H Kontakte: %d", faltung.hhKontakte),
                PADDING + 300, statsY);
        g2.drawString(String.format("Überlappungen: %d", faltung.ueberlappungen),
                PADDING + 650, statsY);

        String status = (faltung.ueberlappungen == 0) ? "✓ GÜLTIG" : "✗ UNGÜLTIG";
        Color statusColor = (faltung.ueberlappungen == 0) ? new Color(0, 160, 0) : new Color(220, 0, 0);
        g2.setColor(statusColor);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.drawString(status, PADDING + 1050, statsY);

        File outputFile = new File(dateiPfad);
        outputFile.getParentFile().mkdirs();
        ImageIO.write(bild, "png", outputFile);
    }
}
