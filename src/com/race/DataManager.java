package com.race;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataManager {
    private static final String FILE_PATH = "race_results.txt";
    private static final String CARS_PATH = "cars.txt";

    public static void saveResult(String carName, double time, double speed) {
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             PrintWriter pw = new PrintWriter(fw)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            pw.printf("[%s] Voiture: %s | Temps sur 400m: %.3fs | Vitesse: %.2f km/h%n",
                    dtf.format(now), carName, time, speed);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> readResults() {
        List<String> results = new ArrayList<>();
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) return results;
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.isBlank()) results.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static void saveCar(Car car) {
        try {
            java.io.File f = new java.io.File(CARS_PATH);
            if (f.exists() && f.length() > 0) {
                try (java.io.RandomAccessFile raf = new java.io.RandomAccessFile(f, "r")) {
                    raf.seek(f.length() - 1);
                    if (raf.read() != '\n') {
                        try (FileWriter fw2 = new FileWriter(CARS_PATH, true)) {
                            fw2.write(System.lineSeparator());
                        }
                    }
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        try (FileWriter fw = new FileWriter(CARS_PATH, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.printf(Locale.US, "%s|%.2f|%.2f|%.2f|%.2f|%.2f%n",
                    car.getName(),
                    car.getAcceleration(),
                    car.getMaxSpeed(),
                    car.getNitroCapacity(),
                    car.getNitroConsumption(),
                    car.getNitroAccelBoost());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Car> loadCars() {
        List<Car> cars = new ArrayList<>();
        Path path = Paths.get(CARS_PATH);
        if (!Files.exists(path)) return cars;
        try (BufferedReader br = new BufferedReader(new FileReader(CARS_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] p = line.split("\\|");
                if (p.length < 3) continue;
                double accel   = Double.parseDouble(p[1].replace(',', '.'));
                double vmax    = Double.parseDouble(p[2].replace(',', '.'));
                double nitroCap    = p.length >= 4 ? Double.parseDouble(p[3].replace(',', '.')) : 2.0;
                double nitroConsum = p.length >= 5 ? Double.parseDouble(p[4].replace(',', '.')) : 1.0;
                double nitroBoost  = p.length >= 6 ? Double.parseDouble(p[5].replace(',', '.')) : 30.0;
                cars.add(new Car(p[0], accel, vmax, nitroCap, nitroConsum, nitroBoost));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cars;
    }
}
