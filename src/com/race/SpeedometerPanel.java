package com.race;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;

public class SpeedometerPanel extends JPanel {
    private double currentSpeed = 0;
    private double maxSpeed = 220;

    private static final Color RING_BG  = new Color(226, 232, 240);
    private static final Color NEEDLE   = new Color(220, 38, 38);
    private static final Color TEXT_COL = new Color(30, 41, 59);

    public SpeedometerPanel(double maxSpeed) {
        this.maxSpeed = maxSpeed;
        setPreferredSize(new Dimension(175, 175));
        setBackground(Color.WHITE);
    }

    public void setCurrentSpeed(double speed) {
        this.currentSpeed = Math.min(speed, maxSpeed);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int size = Math.min(w, h) - 24;
        int cx = w / 2;
        int cy = h / 2;
        int ox = (w - size) / 2;
        int oy = (h - size) / 2;

        // Outer ring track (light gray background arc)
        g2.setColor(RING_BG);
        g2.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(new Arc2D.Double(ox, oy, size, size, -135, -270, Arc2D.OPEN));

        // Colored speed arc
        g2.setStroke(new BasicStroke(5));
        int steps = 100;
        for (int i = 0; i <= steps; i++) {
            double speed = ((double) i / steps) * maxSpeed;
            double angle = 135 - ((double) i / steps) * 270;
            if (speed < maxSpeed * 0.45) {
                g2.setColor(new Color(34, 197, 94));
            } else if (speed < maxSpeed * 0.80) {
                g2.setColor(new Color(234, 179, 8));
            } else {
                g2.setColor(new Color(220, 38, 38));
            }
            double rad = Math.toRadians(angle);
            int r1 = size / 2 - 9;
            int r2 = size / 2;
            g2.draw(new Line2D.Float(
                cx + (int) (Math.cos(rad) * r1), cy - (int) (Math.sin(rad) * r1),
                cx + (int) (Math.cos(rad) * r2), cy - (int) (Math.sin(rad) * r2)
            ));
        }

        // Ticks and labels
        g2.setFont(new Font("Arial", Font.BOLD, 8));
        int numTicks = 8;
        for (int i = 0; i <= numTicks; i++) {
            double speed = ((double) i / numTicks) * maxSpeed;
            double angle = 135 - ((double) i / numTicks) * 270;
            double rad = Math.toRadians(angle);
            int r1 = size / 2 - 19;
            int r2 = size / 2 - 11;
            g2.setColor(TEXT_COL);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new Line2D.Float(
                cx + (int) (Math.cos(rad) * r1), cy - (int) (Math.sin(rad) * r1),
                cx + (int) (Math.cos(rad) * r2), cy - (int) (Math.sin(rad) * r2)
            ));
            int rLabel = size / 2 - 30;
            int xl = cx + (int) (Math.cos(rad) * rLabel) - 7;
            int yl = cy - (int) (Math.sin(rad) * rLabel) + 4;
            g2.drawString(String.format("%.0f", speed), xl, yl);
        }

        // Needle
        double needleAngle = 135 - (currentSpeed / maxSpeed) * 270;
        double needleRad = Math.toRadians(needleAngle);
        int needleLen = size / 2 - 16;
        g2.setColor(NEEDLE);
        g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(new Line2D.Float(
            cx, cy,
            cx + (int) (Math.cos(needleRad) * needleLen),
            cy - (int) (Math.sin(needleRad) * needleLen)
        ));

        // Center cap
        g2.setColor(new Color(30, 41, 59));
        g2.fillOval(cx - 6, cy - 6, 12, 12);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1));
        g2.drawOval(cx - 6, cy - 6, 12, 12);

        // Digital speed text
        g2.setColor(TEXT_COL);
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        String speedText = String.format("%.0f km/h", currentSpeed);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(speedText, (w - fm.stringWidth(speedText)) / 2, cy + size / 2 - 2);
    }
}
