package com.race;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TrackPanel extends JPanel {

    private BufferedImage carImage;
    private double progress = 0.0; // 0.0 (A) → 1.0 (B)

    private static final int TRACK_H = 90;
    private static final int CAR_SIZE = 54;
    private static final int MARGIN = 70;

    public TrackPanel() {
        setBackground(new Color(20, 20, 35));
        setPreferredSize(new Dimension(750, 170));
        try {
            carImage = ImageIO.read(new File("input/car.png"));
        } catch (IOException e) {
            System.err.println("Cannot load car image: " + e.getMessage());
        }
    }

    public void setProgress(double distance) {
        this.progress = Math.min(distance / 400.0, 1.0);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int w = getWidth();
        int h = getHeight();
        int trackX = MARGIN;
        int trackW = w - 2 * MARGIN;
        int trackY = (h - TRACK_H) / 2;
        int centerY = trackY + TRACK_H / 2;

        // Grass background
        GradientPaint grass = new GradientPaint(0, 0, new Color(28, 90, 28), 0, h, new Color(20, 65, 20));
        g2.setPaint(grass);
        g2.fillRect(0, 0, w, h);

        // Asphalt track
        g2.setColor(new Color(60, 60, 60));
        g2.fillRoundRect(trackX, trackY, trackW, TRACK_H, 8, 8);

        // Track border (white lines top + bottom)
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(trackX, trackY, trackX + trackW, trackY);
        g2.drawLine(trackX, trackY + TRACK_H, trackX + trackW, trackY + TRACK_H);

        // Dashed yellow center line
        g2.setColor(new Color(255, 220, 0));
        float[] dash = {18f, 12f};
        g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, dash, 0f));
        g2.drawLine(trackX, centerY, trackX + trackW, centerY);

        // Checkerboard start line at A
        drawCheckerLine(g2, trackX, trackY, TRACK_H);

        // Checkerboard finish line at B
        drawCheckerLine(g2, trackX + trackW, trackY, TRACK_H);

        // Distance markers 100m / 200m / 300m
        g2.setStroke(new BasicStroke(1));
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        for (int m = 100; m < 400; m += 100) {
            int mx = trackX + (int) (trackW * m / 400.0);
            g2.setColor(new Color(180, 180, 180, 150));
            g2.fillRect(mx - 1, trackY + 1, 2, TRACK_H - 2);
            g2.setColor(Color.LIGHT_GRAY);
            String label = m + "m";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, mx - fm.stringWidth(label) / 2, trackY - 4);
        }

        // A label
        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.setColor(Color.WHITE);
        g2.drawString("A", trackX - 42, centerY + 8);

        // B label
        g2.drawString("B", trackX + trackW + 14, centerY + 8);

        // Progress fill on track (teal tint)
        if (progress > 0) {
            g2.setColor(new Color(0, 200, 180, 40));
            g2.fillRect(trackX + 2, trackY + 2, (int) (trackW * progress) - 2, TRACK_H - 4);
        }

        // Car
        if (carImage != null) {
            int carX = trackX + (int) (trackW * progress);
            drawCar(g2, carX, centerY);
        }
    }

    private void drawCheckerLine(Graphics2D g2, int x, int y, int h) {
        int boxH = h / 6;
        for (int i = 0; i < 6; i++) {
            g2.setColor(i % 2 == 0 ? Color.BLACK : Color.WHITE);
            g2.fillRect(x - 3, y + i * boxH, 6, boxH);
        }
    }

    private void drawCar(Graphics2D g2, int cx, int cy) {
        // Car image faces UP → rotate 90° CW to face RIGHT
        double scale = (double) CAR_SIZE / Math.max(carImage.getWidth(), carImage.getHeight());
        int sw = (int) (carImage.getWidth() * scale);
        int sh = (int) (carImage.getHeight() * scale);

        Graphics2D g2c = (Graphics2D) g2.create();
        g2c.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2c.translate(cx, cy);
        g2c.rotate(Math.PI / 2);  // 90° CW → car faces right
        g2c.scale(scale, scale);
        g2c.drawImage(carImage, -carImage.getWidth() / 2, -carImage.getHeight() / 2, null);
        g2c.dispose();
    }
}
