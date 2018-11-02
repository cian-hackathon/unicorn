package com.thelads.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.Random;

public class Unicorn {

    private double distance;
    private int healthPoints;
    private double latitude;
    private double longitude;
    private int magicPoints;
    private String name;
    private String statusTime;

    @JsonIgnore
    private Random random = new Random();

    private static final double LATITUDE_OFFSET = 110540;
    private static final double LONGITUDE_OFFSET = 111320;
    private static final int MAX_DISTANCE = 31;
    private static final int MIN_DISTANCE = 29;

    public Unicorn() {
    }

    public Unicorn(String name) {
        this.name = name;
        this.distance = 0;
        this.healthPoints = 100;
        this.magicPoints = 100;
        this.latitude = -6.2827;
        this.longitude = 53.3419;
        this.statusTime = LocalDateTime.now().toString();
    }

    public Unicorn(String name, double distance, int healthPoints,
        double longitude, double latitude, int magicPoints, String statusTime) {
        this.name = name;
        this.distance = distance;
        this.healthPoints = healthPoints;
        this.magicPoints = magicPoints;
        this.latitude = latitude;
        this.longitude = longitude;
        this.statusTime = statusTime;
    }

    public Unicorn(String name, double latitude, double longitude){
        this.name = name;
        this.distance = 0;
        this.healthPoints = 100;
        this.magicPoints = 100;
        this.latitude = latitude;
        this.longitude = longitude;
        this.statusTime = LocalDateTime.now().toString();
    }

    public void move() {
        this.statusTime = LocalDateTime.now().toString();
        this.healthPoints = nextPoints(healthPoints);
        this.magicPoints = nextPoints(magicPoints);
        double bearing = random.nextInt(360);
        double distanceToTravel =  ( (double) new Random().nextInt(MAX_DISTANCE - MIN_DISTANCE) + MIN_DISTANCE ) + Math.random();
        this.distance += distanceToTravel;
        this.latitude = nextLatitude(latitude, bearing, distanceToTravel);
        this.longitude = nextLongitude(longitude, bearing, distanceToTravel);
    }

    private double nextLatitude(double currentLatitude, double bearing, double distance) {
        return currentLatitude + distance * Math.cos(bearing) / LATITUDE_OFFSET;
    }

    private double nextLongitude(double currentLongitude, double bearing, double distance) {
        return currentLongitude + distance * Math.sin(bearing) / LONGITUDE_OFFSET;
    }

    private int nextPoints(int points) {
        int y = random.nextInt(2);

        if (random.nextInt(2) % 2 == 0) {
            y = y * -1;
        }

        return points + y;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getMagicPoints() {
        return magicPoints;
    }

    public void setMagicPoints(int magicPoints) {
        this.magicPoints = magicPoints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(String statusTime) {
        this.statusTime = statusTime;
    }

    public static class Builder{
        private double distance;
        private int healthPoints;
        private double latitude;
        private double longitude;
        private int magicPoints;
        private String name;
        private String statusTime;


        public Builder setDistance(double distance){
            this.distance = distance;
            return this;
        }

        public Builder setHealthPoints(int healthPoints){
            this.healthPoints = healthPoints;
            return this;
        }

        public Builder setLatitude(double latitude){
            this.latitude = latitude;
            return this;
        }

        public Builder setLongitude(double longitude){
            this.longitude = longitude;
            return this;
        }

        public Builder setMagicPoints(int magicPoints){
            this.magicPoints = magicPoints;
            return this;
        }

        public Builder setName(String name){
            this.name = name;
            return this;
        }

        public Builder setStatusTime(String statusTime){
            this.statusTime = statusTime;
            return this;
        }

        public Unicorn build(){return new Unicorn(name, distance, healthPoints,
            longitude, latitude, magicPoints, statusTime);}
    }
}
