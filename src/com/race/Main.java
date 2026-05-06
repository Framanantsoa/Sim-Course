package com.race;

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public Main() {
        setTitle("Simulation de Course");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        CarSelectionPanel selectionPanel = new CarSelectionPanel(car -> {
            SimulationPanel simulationPanel = new SimulationPanel(car, this::showSelectionPanel);
            mainPanel.add(simulationPanel, "simulation");
            cardLayout.show(mainPanel, "simulation");
        });

        mainPanel.add(selectionPanel, "selection");
        add(mainPanel);

        cardLayout.show(mainPanel, "selection");
    }

    private void showSelectionPanel() {
        cardLayout.show(mainPanel, "selection");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.setVisible(true);
        });
    }
}
