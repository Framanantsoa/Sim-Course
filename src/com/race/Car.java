package com.race;

public class Car {
    private final String name;
    private final double acceleration;     // km/h/s
    private final double maxSpeed;         // km/h
    private final double nitroCapacity;    // kg
    private final double nitroConsumption; // kg/min
    private final double nitroAccelBoost;  // km/h/s per second (jerk while boosting)

    public Car(String name, double acceleration) {
        this(name, acceleration, 220.0, 2.0, 1.0, 30.0);
    }

    public Car(String name, double acceleration, double maxSpeed) {
        this(name, acceleration, maxSpeed, 2.0, 1.0, 30.0);
    }

    public Car(String name, double acceleration, double maxSpeed,
               double nitroCapacity, double nitroConsumption, double nitroAccelBoost) {
        this.name = name;
        this.acceleration = acceleration;
        this.maxSpeed = maxSpeed;
        this.nitroCapacity = nitroCapacity;
        this.nitroConsumption = nitroConsumption;
        this.nitroAccelBoost = nitroAccelBoost;
    }

    public String getName()             { return name; }
    public double getAcceleration()     { return acceleration; }
    public double getMaxSpeed()         { return maxSpeed; }
    public double getNitroCapacity()    { return nitroCapacity; }
    public double getNitroConsumption() { return nitroConsumption; }
    public double getNitroAccelBoost()  { return nitroAccelBoost; }

    @Override
    public String toString() {
        return name + " (" + acceleration + " km/h/s)";
    }
}
