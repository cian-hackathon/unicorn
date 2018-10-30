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

    public void move() {
        this.healthPoints = nextPoints(healthPoints);
        this.magicPoints = nextPoints(magicPoints);
        double bearing = Math.random() * Math.PI * 2;
        double distance =  ( (double) new Random().nextInt(MAX_DISTANCE - MIN_DISTANCE) + MIN_DISTANCE ) + Math.random();
        this.latitude = nextLatitude(latitude, bearing, distance);
        this.longitude = nextLongitude(longitude, bearing, distance);
    }

    private double nextLatitude(double currentLatitude, double bearing, double distance) {
        return currentLatitude + distance * Math.sin(bearing) / LATITUDE_OFFSET;
    }

    private double nextLongitude(double currentLongitude, double bearing, double distance) {
        return currentLongitude + distance * Math.cos(bearing) / (LONGITUDE_OFFSET * Math.cos(Math.PI * latitude / 100));
    }

    private int nextPoints(int points) {
        Random random = new Random();
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
}
