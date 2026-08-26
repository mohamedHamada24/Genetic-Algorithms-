
import java.awt.BasicStroke;
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

public class Visualization {

    public static void drawFaltung(Faltung faltung, String filename) {
        int width = 800;
        int height = 800;
        int cellSize = 50;
        int originX = width / 2;
        int originY = height / 2;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Background color
        g2.setColor(Color.YELLOW);
        g2.fillRect(0, 0, width, height);

        int[][] coords = faltung.getCoordinates();
        String seq = faltung.getSequence();
        int n = coords.length;

        // Group indices by coordinate
        Map<String, List<Integer>> gridMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String posKey = coords[i][0] + "," + coords[i][1];
            gridMap.computeIfAbsent(posKey, k -> new ArrayList<>()).add(i);
        }

        // 1. Draw backbone connection lines
        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(3.0f));
        for (int i = 0; i < n - 1; i++) {
            int x1 = originX + coords[i][0] * cellSize;
            int y1 = originY - coords[i][1] * cellSize;
            int x2 = originX + coords[i + 1][0] * cellSize;
            int y2 = originY - coords[i + 1][1] * cellSize;
            g2.drawLine(x1, y1, x2, y2);
        }

        // 2. Draw amino acid nodes
        int baseRadius = 16;

        for (Map.Entry<String, List<Integer>> entry : gridMap.entrySet()) {
            List<Integer> indicesAtPos = entry.getValue();
            int firstIdx = indicesAtPos.get(0);

            int x = originX + coords[firstIdx][0] * cellSize;
            int y = originY - coords[firstIdx][1] * cellSize;

            // IF OVERLAP
            if (indicesAtPos.size() > 1) {
                g2.setColor(Color.RED);
                g2.setStroke(new BasicStroke(3.5f));
                g2.drawOval(x - baseRadius - 3, y - baseRadius - 3, (baseRadius + 3) * 2, (baseRadius + 3) * 2);

                boolean leftHydro = (seq.charAt(indicesAtPos.get(0)) == '1' || seq.charAt(indicesAtPos.get(0)) == 'H');
                boolean rightHydro = (seq.charAt(indicesAtPos.get(1)) == '1' || seq.charAt(indicesAtPos.get(1)) == 'H');

                g2.setColor(leftHydro ? Color.BLACK : Color.WHITE);
                g2.fillArc(x - baseRadius, y - baseRadius, baseRadius * 2, baseRadius * 2, 90, 180);

                g2.setColor(rightHydro ? Color.BLACK : Color.WHITE);
                g2.fillArc(x - baseRadius, y - baseRadius, baseRadius * 2, baseRadius * 2, 270, 180);

                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(x - baseRadius, y - baseRadius, baseRadius * 2, baseRadius * 2);

                Font fontSmall = new Font("SansSerif", Font.BOLD, 9);
                g2.setFont(fontSmall);

                g2.setColor(leftHydro ? Color.WHITE : Color.BLACK);
                String leftLabel = String.valueOf(indicesAtPos.get(0));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(leftLabel, x - baseRadius / 2 - fm.stringWidth(leftLabel) / 2, y + fm.getAscent() / 3);

                g2.setColor(rightHydro ? Color.WHITE : Color.BLACK);
                String rightLabel = String.valueOf(indicesAtPos.get(1));
                g2.drawString(rightLabel, x + baseRadius / 2 - fm.stringWidth(rightLabel) / 2, y + fm.getAscent() / 3);

            } else {
                // NORMAL NODE
                boolean isHydrophobic = (seq.charAt(firstIdx) == '1' || seq.charAt(firstIdx) == 'H');

                g2.setColor(isHydrophobic ? Color.BLACK : Color.WHITE);
                g2.fillOval(x - baseRadius, y - baseRadius, baseRadius * 2, baseRadius * 2);

                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval(x - baseRadius, y - baseRadius, baseRadius * 2, baseRadius * 2);

                g2.setColor(isHydrophobic ? Color.WHITE : Color.BLACK);
                Font font = new Font("SansSerif", Font.BOLD, 11);
                g2.setFont(font);

                String label = String.valueOf(firstIdx);
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(label);
                int labelHeight = metrics.getAscent();
                g2.drawString(label, x - labelWidth / 2, y + labelHeight / 4);
            }
        }

        // =========================================================================
        // 3. DRAW DIRECT DATA TEXT ONLY (Top-Left Corner)
        // =========================================================================
        int textX = 20;
        int textY = 30;

        g2.setFont(new Font("SansSerif", Font.BOLD, 14));
        g2.setColor(Color.BLACK);

        // Fitness
        g2.drawString(String.format("Fitness: %.6f", faltung.getFitness()), textX, textY);


        g2.drawString("HH-Kontakte: " + faltung.getHhContactsCount(), textX, textY + 22);


        if (faltung.getOverlapsCount() > 0) {
            g2.setColor(Color.RED);
        } else {
            g2.setColor(new Color(0, 0, 0));
        }
        g2.drawString("Überlappungen: " + faltung.getOverlapsCount(), textX, textY + 44);

        g2.dispose();

        File outputDir = new File("output");
        if (!outputDir.exists()) outputDir.mkdirs();

        try {
            ImageIO.write(image, "png", new File(outputDir, filename));
            System.out.println("Visualization saved to: output/" + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}