package com.race;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CarSelectionPanel extends JPanel {
    private final JComboBox<Car> carComboBox;
    private final JLabel accelLabel;
    private final JLabel vmaxLabel;
    private final JLabel nitroLabel;
    private final JLabel boostLabel;

    private static final Color BG     = new Color(245, 245, 240);
    private static final Color CARD   = Color.WHITE;
    private static final Color NAVY   = new Color(15, 23, 42);
    private static final Color RED    = new Color(220, 38, 38);
    private static final Color TEXT   = new Color(30, 41, 59);
    private static final Color MUTED  = new Color(100, 116, 139);
    private static final Color BORDER = new Color(226, 232, 240);
    private static final Color GREEN  = new Color(22, 163, 74);

    public CarSelectionPanel(CarSelectedListener listener) {
        setLayout(new BorderLayout());
        setBackground(BG);

        // NORTH: header bar
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(20, 32, 20, 32));

        JLabel title = new JLabel("ETU 2376");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("course");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(new Color(148, 163, 184));

        JPanel headerText = new JPanel();
        headerText.setOpaque(false);
        headerText.setLayout(new BoxLayout(headerText, BoxLayout.Y_AXIS));
        headerText.add(title);
        headerText.add(Box.createVerticalStrut(4));
        headerText.add(subtitle);
        header.add(headerText, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // CENTER: two-column split
        JPanel center = new JPanel(new GridLayout(1, 2, 1, 0));
        center.setBackground(BORDER);

        // LEFT card: car selection + stats
        JPanel left = new JPanel();
        left.setBackground(CARD);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));

        JLabel selTitle = new JLabel("Votre véhicule");
        selTitle.setFont(new Font("Arial", Font.BOLD, 15));
        selTitle.setForeground(TEXT);
        selTitle.setAlignmentX(LEFT_ALIGNMENT);
        left.add(selTitle);
        left.add(Box.createVerticalStrut(5));

        JLabel selSub = new JLabel("Sélectionnez un modèle dans la liste");
        selSub.setFont(new Font("Arial", Font.PLAIN, 12));
        selSub.setForeground(MUTED);
        selSub.setAlignmentX(LEFT_ALIGNMENT);
        left.add(selSub);
        left.add(Box.createVerticalStrut(16));

        List<Car> cars = new ArrayList<>();
        cars.addAll(DataManager.loadCars());

        carComboBox = new JComboBox<>(cars.toArray(new Car[0]));
        carComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        carComboBox.setBackground(Color.WHITE);
        carComboBox.setForeground(TEXT);
        carComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        carComboBox.setAlignmentX(LEFT_ALIGNMENT);
        left.add(carComboBox);
        left.add(Box.createVerticalStrut(20));

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(BORDER);
        sep.setAlignmentX(LEFT_ALIGNMENT);
        left.add(sep);
        left.add(Box.createVerticalStrut(14));

        JLabel statsTitle = new JLabel("CARACTÉRISTIQUES");
        statsTitle.setFont(new Font("Arial", Font.BOLD, 10));
        statsTitle.setForeground(MUTED);
        statsTitle.setAlignmentX(LEFT_ALIGNMENT);
        left.add(statsTitle);
        left.add(Box.createVerticalStrut(10));

        accelLabel = new JLabel();
        accelLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        accelLabel.setForeground(TEXT);
        accelLabel.setAlignmentX(LEFT_ALIGNMENT);
        left.add(accelLabel);
        left.add(Box.createVerticalStrut(5));

        vmaxLabel = new JLabel();
        vmaxLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        vmaxLabel.setForeground(TEXT);
        vmaxLabel.setAlignmentX(LEFT_ALIGNMENT);
        left.add(vmaxLabel);
        left.add(Box.createVerticalStrut(12));

        JSeparator sep2 = new JSeparator();
        sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep2.setForeground(BORDER);
        sep2.setAlignmentX(LEFT_ALIGNMENT);
        left.add(sep2);
        left.add(Box.createVerticalStrut(14));

        JLabel nitroTitle = new JLabel("NITRO");
        nitroTitle.setFont(new Font("Arial", Font.BOLD, 10));
        nitroTitle.setForeground(new Color(59, 130, 246));
        nitroTitle.setAlignmentX(LEFT_ALIGNMENT);
        left.add(nitroTitle);
        left.add(Box.createVerticalStrut(10));

        nitroLabel = new JLabel();
        nitroLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        nitroLabel.setForeground(TEXT);
        nitroLabel.setAlignmentX(LEFT_ALIGNMENT);
        left.add(nitroLabel);
        left.add(Box.createVerticalStrut(5));

        boostLabel = new JLabel();
        boostLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        boostLabel.setForeground(TEXT);
        boostLabel.setAlignmentX(LEFT_ALIGNMENT);
        left.add(boostLabel);
        left.add(Box.createVerticalGlue());

        updateStats();
        carComboBox.addActionListener(e -> updateStats());

        // RIGHT card: actions
        JPanel right = new JPanel();
        right.setBackground(BG);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(BorderFactory.createEmptyBorder(32, 36, 32, 36));

        JLabel actTitle = new JLabel("Actions");
        actTitle.setFont(new Font("Arial", Font.BOLD, 15));
        actTitle.setForeground(TEXT);
        actTitle.setAlignmentX(LEFT_ALIGNMENT);
        right.add(actTitle);
        right.add(Box.createVerticalStrut(5));

        JLabel actSub = new JLabel("Démarrez ou configurez votre course");
        actSub.setFont(new Font("Arial", Font.PLAIN, 12));
        actSub.setForeground(MUTED);
        actSub.setAlignmentX(LEFT_ALIGNMENT);
        right.add(actSub);
        right.add(Box.createVerticalStrut(22));

        JButton selectButton = makeButton("Confirmer le choix", RED, Color.WHITE, true);
        JButton customButton = makeButton("+ Créer une voiture personnalisée", CARD, TEXT, false);
        JButton historyButton = makeButton("Historique des courses", CARD, GREEN, false);

        right.add(selectButton);
        right.add(Box.createVerticalStrut(12));
        right.add(customButton);
        right.add(Box.createVerticalStrut(12));
        right.add(historyButton);
        right.add(Box.createVerticalGlue());

        selectButton.addActionListener(e -> {
            Car selectedCar = (Car) carComboBox.getSelectedItem();
            if (selectedCar != null) listener.onCarSelected(selectedCar);
        });
        customButton.addActionListener(e -> showCustomCarDialog());
        historyButton.addActionListener(e -> showHistoryDialog());

        center.add(left);
        center.add(right);
        add(center, BorderLayout.CENTER);

        // SOUTH: footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        footer.setBackground(CARD);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));
        JLabel footerLabel = new JLabel("ETU 2376 - Simulation de COURSE 400m");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        footerLabel.setForeground(MUTED);
        footer.add(footerLabel);
        add(footer, BorderLayout.SOUTH);
    }

    private JButton makeButton(String text, Color bg, Color fg, boolean filled) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setOpaque(true);
        btn.setFocusPainted(false);
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        if (filled) {
            btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        } else {
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                BorderFactory.createEmptyBorder(9, 19, 9, 19)
            ));
        }
        return btn;
    }

    private void showHistoryDialog() {
        List<String> results = DataManager.readResults();

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Historique des courses", true);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(CARD);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        JPanel dHeader = new JPanel(new BorderLayout());
        dHeader.setBackground(NAVY);
        dHeader.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel dTitle = new JLabel("Historique des courses");
        dTitle.setFont(new Font("Arial", Font.BOLD, 15));
        dTitle.setForeground(Color.WHITE);
        dHeader.add(dTitle, BorderLayout.WEST);
        dialog.add(dHeader, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setBackground(CARD);
        textArea.setForeground(TEXT);
        textArea.setMargin(new Insets(14, 16, 14, 16));

        if (results.isEmpty()) {
            textArea.setText("Aucun résultat enregistré.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (String line : results) sb.append(line).append("\n");
            textArea.setText(sb.toString());
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        dialog.add(scrollPane, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        south.setBackground(BG);
        JButton closeButton = makeButton("Fermer", CARD, TEXT, false);
        closeButton.setMaximumSize(new Dimension(100, 36));
        closeButton.addActionListener(e -> dialog.dispose());
        south.add(closeButton);
        dialog.add(south, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showCustomCarDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Nouvelle Voiture", true);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(CARD);
        dialog.setSize(420, 420);
        dialog.setLocationRelativeTo(this);

        JPanel dHeader = new JPanel(new BorderLayout());
        dHeader.setBackground(NAVY);
        dHeader.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel dTitle = new JLabel("Créer une voiture personnalisée");
        dTitle.setFont(new Font("Arial", Font.BOLD, 15));
        dTitle.setForeground(Color.WHITE);
        dHeader.add(dTitle, BorderLayout.WEST);
        dialog.add(dHeader, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(CARD);
        form.setBorder(BorderFactory.createEmptyBorder(20, 24, 10, 24));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 8, 7, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Arial", Font.BOLD, 13);
        Font fieldFont = new Font("Arial", Font.PLAIN, 13);

        // --- Base fields ---
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.45;
        JLabel nameLabel = new JLabel("Nom :");
        nameLabel.setFont(labelFont); nameLabel.setForeground(TEXT);
        form.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 0.55;
        JTextField nameField = new JTextField(10);
        nameField.setFont(fieldFont);
        form.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.45;
        JLabel accelLbl = new JLabel("Accélération (km/h/s) :");
        accelLbl.setFont(labelFont); accelLbl.setForeground(TEXT);
        form.add(accelLbl, gbc);
        gbc.gridx = 1; gbc.weightx = 0.55;
        JTextField accelField = new JTextField("20");
        accelField.setFont(fieldFont);
        form.add(accelField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.45;
        JLabel speedLbl = new JLabel("Vmax (km/h) :");
        speedLbl.setFont(labelFont); speedLbl.setForeground(TEXT);
        form.add(speedLbl, gbc);
        gbc.gridx = 1; gbc.weightx = 0.55;
        JTextField speedField = new JTextField("220");
        speedField.setFont(fieldFont);
        form.add(speedField, gbc);

        // --- Nitro separator ---
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JLabel nitroSep = new JLabel("— NITRO —");
        nitroSep.setFont(new Font("Arial", Font.BOLD, 11));
        nitroSep.setForeground(new Color(59, 130, 246));
        nitroSep.setBorder(BorderFactory.createEmptyBorder(6, 0, 2, 0));
        form.add(nitroSep, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.45;
        JLabel nitroCapLbl = new JLabel("Bouteille (kg) :");
        nitroCapLbl.setFont(labelFont); nitroCapLbl.setForeground(TEXT);
        form.add(nitroCapLbl, gbc);
        gbc.gridx = 1; gbc.weightx = 0.55;
        JTextField nitroCapField = new JTextField("2.0");
        nitroCapField.setFont(fieldFont);
        form.add(nitroCapField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.45;
        JLabel nitroConsumLbl = new JLabel("Consommation (kg/min) :");
        nitroConsumLbl.setFont(labelFont); nitroConsumLbl.setForeground(TEXT);
        form.add(nitroConsumLbl, gbc);
        gbc.gridx = 1; gbc.weightx = 0.55;
        JTextField nitroConsumField = new JTextField("1.0");
        nitroConsumField.setFont(fieldFont);
        form.add(nitroConsumField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0.45;
        JLabel nitroBoostLbl = new JLabel("Boost accél (km/h/s²) :");
        nitroBoostLbl.setFont(labelFont); nitroBoostLbl.setForeground(TEXT);
        form.add(nitroBoostLbl, gbc);
        gbc.gridx = 1; gbc.weightx = 0.55;
        JTextField nitroBoostField = new JTextField("30.0");
        nitroBoostField.setFont(fieldFont);
        form.add(nitroBoostField, gbc);

        dialog.add(form, BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 12));
        south.setBackground(BG);
        JButton cancelButton = makeButton("Annuler", CARD, TEXT, false);
        cancelButton.setMaximumSize(new Dimension(100, 36));
        cancelButton.addActionListener(e -> dialog.dispose());
        JButton saveButton = makeButton("Enregistrer", RED, Color.WHITE, true);
        saveButton.setMaximumSize(new Dimension(120, 36));
        south.add(cancelButton);
        south.add(saveButton);
        dialog.add(south, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Le nom ne peut pas être vide.");
                    return;
                }
                double accel      = Double.parseDouble(accelField.getText().trim().replace(',', '.'));
                double speed      = Double.parseDouble(speedField.getText().trim().replace(',', '.'));
                double nitroCap   = Double.parseDouble(nitroCapField.getText().trim().replace(',', '.'));
                double nitroConsum= Double.parseDouble(nitroConsumField.getText().trim().replace(',', '.'));
                double nitroBoost = Double.parseDouble(nitroBoostField.getText().trim().replace(',', '.'));

                if (accel <= 0 || speed <= 0 || nitroCap < 0 || nitroConsum < 0 || nitroBoost < 0) {
                    JOptionPane.showMessageDialog(dialog, "Les valeurs doivent être positives.");
                    return;
                }
                Car newCar = new Car(name, accel, speed, nitroCap, nitroConsum, nitroBoost);
                DataManager.saveCar(newCar);
                carComboBox.addItem(newCar);
                carComboBox.setSelectedItem(newCar);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Veuillez entrer des nombres valides.");
            }
        });

        dialog.setVisible(true);
    }

    private void updateStats() {
        Car c = (Car) carComboBox.getSelectedItem();
        if (c != null) {
            accelLabel.setText(String.format("Accélération : %.1f km/h/s", c.getAcceleration()));
            vmaxLabel.setText(String.format("Vitesse max : %.0f km/h", c.getMaxSpeed()));
            nitroLabel.setText(String.format("Bouteille : %.1f kg  |  Conso : %.1f kg/min",
                    c.getNitroCapacity(), c.getNitroConsumption()));
            boostLabel.setText(String.format("Boost accél : +%.1f km/h/s²", c.getNitroAccelBoost()));
        }
    }

    public interface CarSelectedListener {
        void onCarSelected(Car car);
    }
}
