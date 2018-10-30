import java.time.LocalDateTime;

public class Unicorn {

    private float distance;
    private int healthPoints;
    private float latitude;
    private float longitude;
    private int magicPoints;
    private String name;
    private String statusTime;

    public Unicorn() {
    }

    public Unicorn(String name) {
        this.name = name;
        this.distance = 0f;
        this.healthPoints = 100;
        this.magicPoints = 100;
        this.latitude = -6.2827f;
        this.longitude = 53.3419f;
        this.statusTime = LocalDateTime.now().toString();
    }

    public float getDistance() {
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

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
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
