package com.race;

import javax.swing.*;
import java.awt.*;

public class NitroGaugePanel extends JPanel {
    private double level = 1.0;

    public NitroGaugePanel() {
        setPreferredSize(new Dimension(28, 150));
        setMaximumSize(new Dimension(28, 160));
        setOpaque(false);
    }

    public void setLevel(double level) {
        this.level = Math.max(0.0, Math.min(1.0, level));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        // Background
        g2.setColor(new Color(226, 232, 240));
        g2.fillRoundRect(0, 0, w, h, 6, 6);

        // Fill (bottom-up)
        int fillH = (int) (h * level);
        if (fillH > 0) {
            g2.setColor(new Color(59, 130, 246));
            g2.fillRoundRect(0, h - fillH, w, fillH, 6, 6);
        }

        // Border
        g2.setColor(new Color(203, 213, 225));
        g2.setStroke(new BasicStroke(1));
        g2.drawRoundRect(0, 0, w - 1, h - 1, 6, 6);
    }
}
