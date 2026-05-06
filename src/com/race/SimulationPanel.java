package com.race;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SimulationPanel extends JPanel {
    private final Car car;
    private final SpeedometerPanel speedometerPanel;
    private final TrackPanel trackPanel;
    private final NitroGaugePanel nitroGauge;
    private final JLabel timeLabel;
    private final JLabel distanceLabel;
    private final JLabel resultLabel;
    private final JLabel nitroPercentLabel;
    private final JButton startButton;
    private final JButton stopButton;
    private final JButton resetButton;

    private static final Color BG     = new Color(245, 245, 240);
    private static final Color CARD   = Color.WHITE;
    private static final Color NAVY   = new Color(15, 23, 42);
    private static final Color RED    = new Color(220, 38, 38);
    private static final Color BLUE   = new Color(59, 130, 246);
    private static final Color TEXT   = new Color(30, 41, 59);
    private static final Color MUTED  = new Color(100, 116, 139);
    private static final Color BORDER = new Color(226, 232, 240);

    private Timer simulationTimer;
    private double elapsedTime = -5.0;
    private double distance = 0.0;
    private double currentSpeed = 0.0;
    private boolean isAccelerating = false;
    private boolean isFinished = false;
    private double timeAt400m = 0.0;
    private long lastTickNano = 0;

    // Nitro state
    private double nitroRemaining;
    private boolean isBoosting = false;
    private double currentBoostAccel = 0.0;

    public SimulationPanel(Car car, Runnable onBack) {
        this.car = car;
        this.nitroRemaining = car.getNitroCapacity();
        setLayout(new BorderLayout());
        setBackground(BG);

        // NORTH: header bar
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 24));
        header.setPreferredSize(new Dimension(0, 56));

        JButton backButton = new JButton("◀ Retour");
        backButton.setFont(new Font("Arial", Font.BOLD, 13));
        backButton.setForeground(new Color(148, 163, 184));
        backButton.setBackground(NAVY);
        backButton.setOpaque(true);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            stopSimulation();
            onBack.run();
        });

        JLabel titleLabel = new JLabel("400m — " + car.getName(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        header.add(backButton, BorderLayout.WEST);
        header.add(titleLabel, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // WEST: sidebar (chrono, distance, speedometer)
        JPanel sidebar = new JPanel();
        sidebar.setBackground(CARD);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(215, 0));
        sidebar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER),
            BorderFactory.createEmptyBorder(26, 20, 20, 20)
        ));

        JLabel chronoTag = new JLabel("CHRONO");
        chronoTag.setFont(new Font("Arial", Font.BOLD, 10));
        chronoTag.setForeground(MUTED);
        chronoTag.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(chronoTag);
        sidebar.add(Box.createVerticalStrut(4));

        timeLabel = new JLabel("−5.0 s");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        timeLabel.setForeground(TEXT);
        timeLabel.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(timeLabel);
        sidebar.add(Box.createVerticalStrut(20));

        JLabel distTag = new JLabel("DISTANCE");
        distTag.setFont(new Font("Arial", Font.BOLD, 10));
        distTag.setForeground(MUTED);
        distTag.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(distTag);
        sidebar.add(Box.createVerticalStrut(4));

        distanceLabel = new JLabel("0.0 / 400 m");
        distanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        distanceLabel.setForeground(TEXT);
        distanceLabel.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(distanceLabel);
        sidebar.add(Box.createVerticalStrut(20));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(BORDER);
        sep.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(18));

        speedometerPanel = new SpeedometerPanel(car.getMaxSpeed());
        speedometerPanel.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(speedometerPanel);
        sidebar.add(Box.createVerticalGlue());

        add(sidebar, BorderLayout.WEST);

        // EAST: nitro panel
        JPanel eastPanel = new JPanel();
        eastPanel.setBackground(CARD);
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        eastPanel.setPreferredSize(new Dimension(90, 0));
        eastPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 0, 0, BORDER),
            BorderFactory.createEmptyBorder(24, 14, 20, 14)
        ));

        JLabel nitroTag = new JLabel("NITRO");
        nitroTag.setFont(new Font("Arial", Font.BOLD, 10));
        nitroTag.setForeground(BLUE);
        nitroTag.setAlignmentX(CENTER_ALIGNMENT);
        eastPanel.add(nitroTag);
        eastPanel.add(Box.createVerticalStrut(10));

        nitroGauge = new NitroGaugePanel();
        nitroGauge.setAlignmentX(CENTER_ALIGNMENT);
        eastPanel.add(nitroGauge);
        eastPanel.add(Box.createVerticalStrut(8));

        nitroPercentLabel = new JLabel("100%", SwingConstants.CENTER);
        nitroPercentLabel.setFont(new Font("Arial", Font.BOLD, 13));
        nitroPercentLabel.setForeground(TEXT);
        nitroPercentLabel.setAlignmentX(CENTER_ALIGNMENT);
        nitroPercentLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        eastPanel.add(nitroPercentLabel);
        eastPanel.add(Box.createVerticalStrut(16));

        eastPanel.add(Box.createVerticalGlue());

        add(eastPanel, BorderLayout.EAST);

        // CENTER: main race area
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG);

        trackPanel = new TrackPanel();
        JPanel trackWrapper = new JPanel(new BorderLayout());
        trackWrapper.setOpaque(false);
        trackWrapper.setBorder(BorderFactory.createEmptyBorder(18, 16, 10, 16));
        trackWrapper.add(trackPanel, BorderLayout.CENTER);
        main.add(trackWrapper, BorderLayout.NORTH);

        // Center zone: result + accelerate button
        resultLabel = new JLabel(" ", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resultLabel.setForeground(new Color(22, 100, 50));

        JPanel midPanel = new JPanel();
        JLabel controlsLabel = new JLabel(
            "<html><center>S → Accélérer<br>N → Utiliser le nitro</center></html>",
            SwingConstants.CENTER
        );
        controlsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        controlsLabel.setForeground(TEXT);
        controlsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        midPanel.setOpaque(false);
        midPanel.setLayout(new BoxLayout(midPanel, BoxLayout.Y_AXIS));
        midPanel.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        midPanel.add(Box.createVerticalGlue());
        midPanel.add(resultLabel);
        midPanel.add(Box.createVerticalStrut(16));
        midPanel.add(controlsLabel);
        midPanel.add(Box.createVerticalGlue());
        main.add(midPanel, BorderLayout.CENTER);

        // SOUTH: control buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 14));
        bottomPanel.setBackground(CARD);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));

        startButton = makeControlButton("Démarrer", new Color(22, 163, 74), Color.WHITE);
        stopButton  = makeControlButton("Arrêter",  new Color(71, 85, 105),  Color.WHITE);
        resetButton = makeControlButton("Réinitialiser", new Color(71, 85, 105), Color.WHITE);

        bottomPanel.add(startButton);
        bottomPanel.add(stopButton);
        bottomPanel.add(resetButton);
        main.add(bottomPanel, BorderLayout.SOUTH);

        add(main, BorderLayout.CENTER);

        // Event listeners
        startButton.addActionListener(e -> startSimulation());
        stopButton.addActionListener(e -> stopSimulation());
        resetButton.addActionListener(e -> resetSimulation());

        simulationTimer = new Timer(16, e -> updateSimulation());

        // Keyboard bindings (S = accélérer, N = nitro) — hold to activate
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke("pressed S"),   "accel-on");
        im.put(KeyStroke.getKeyStroke("released S"),  "accel-off");
        im.put(KeyStroke.getKeyStroke("pressed N"),   "boost-on");
        im.put(KeyStroke.getKeyStroke("released N"),  "boost-off");

        am.put("accel-on",  new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) { isAccelerating = true; }
        });
        am.put("accel-off", new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) { isAccelerating = false; }
        });
        am.put("boost-on",  new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (nitroRemaining > 0) isBoosting = true;
            }
        });
        am.put("boost-off", new javax.swing.AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) { isBoosting = false; }
        });
    }

    private JButton makeControlButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return btn;
    }

    private void updateSimulation() {
        long now = System.nanoTime();
        double dt = (lastTickNano == 0) ? 0.016 : (now - lastTickNano) / 1_000_000_000.0;
        lastTickNano = now;

        elapsedTime += dt;

        if (elapsedTime >= 0) {
            // Boost logic: ramp up accel while boosting and nitro available
            if (isBoosting && nitroRemaining > 0) {
                currentBoostAccel += car.getNitroAccelBoost() * dt;
                nitroRemaining -= (car.getNitroConsumption() / 60.0) * dt;
                if (nitroRemaining <= 0) {
                    nitroRemaining = 0;
                    isBoosting = false;
                }
            }
            if (!isBoosting) {
                currentBoostAccel = 0.0;
            }

            // Acceleration (boost adds to base accel)
            if (isAccelerating) {
                double effectiveAccel = car.getAcceleration() + currentBoostAccel;
                currentSpeed += effectiveAccel * dt;
                if (currentSpeed > car.getMaxSpeed()) currentSpeed = car.getMaxSpeed();
            }

            distance += (currentSpeed / 3.6) * dt;

            if (distance >= 400.0 && !isFinished) {
                isFinished = true;
                timeAt400m = elapsedTime;
                resultLabel.setText(String.format(
                    "Temps final : %.3f s  |  Vitesse : %.1f km/h", timeAt400m, currentSpeed));
                DataManager.saveResult(car.getName(), timeAt400m, currentSpeed);
            }
        }

        // Update labels
        if (elapsedTime < 0) {
            timeLabel.setText(String.format("%.1f s", elapsedTime));
        } else {
            timeLabel.setText(String.format("%.3f s", elapsedTime));
        }
        distanceLabel.setText(String.format("%.1f / 400 m", Math.min(distance, 400.0)));

        // Update nitro gauge
        double cap = car.getNitroCapacity();
        double pct = cap > 0 ? nitroRemaining / cap : 0.0;
        nitroGauge.setLevel(pct);
        nitroPercentLabel.setText(String.format("%.0f%%", pct * 100));

        speedometerPanel.setCurrentSpeed(currentSpeed);
        trackPanel.setProgress(distance);
    }

    private void startSimulation() {
        if (!simulationTimer.isRunning() && !isFinished) simulationTimer.start();
    }

    private void stopSimulation() {
        if (simulationTimer.isRunning()) {
            simulationTimer.stop();
            isAccelerating = false;
            isBoosting = false;
        }
    }

    private void resetSimulation() {
        stopSimulation();
        elapsedTime = -5.0;
        distance = 0.0;
        currentSpeed = 0.0;
        isAccelerating = false;
        isFinished = false;
        lastTickNano = 0;
        nitroRemaining = car.getNitroCapacity();
        isBoosting = false;
        currentBoostAccel = 0.0;
        timeLabel.setText("−5.0 s");
        distanceLabel.setText("0.0 / 400 m");
        resultLabel.setText(" ");
        nitroGauge.setLevel(1.0);
        nitroPercentLabel.setText("100%");
        speedometerPanel.setCurrentSpeed(0.0);
        trackPanel.setProgress(0.0);
    }
}
