package p.lodz.dashboardsimulator.model.monitor;

public class EngineStatistics {

    private double avgSpeed;
    private double maxSpeed;
    private long travelTime;
    private double distance;
    private double avgFuelConsumption;

    public EngineStatistics(double avgSpeed, double maxSpeed, long travelTime, double distance, double avgFuelConsumption) {
        this.avgSpeed = avgSpeed;
        this.maxSpeed = maxSpeed;
        this.travelTime = travelTime;
        this.distance = distance;
        this.avgFuelConsumption = avgFuelConsumption;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public long getTravelTime() {
        return travelTime;
    }

    public double getDistance() {
        return distance;
    }

    public double getAvgFuelConsumption() {
        return avgFuelConsumption;
    }

    @Override
    public String toString() {
        return "EngineStatistics{" +
                "avgSpeed=" + avgSpeed +
                ", maxSpeed=" + maxSpeed +
                ", travelTime=" + travelTime +
                ", distance=" + distance +
                ", avgFuelConsumption=" + avgFuelConsumption +
                '}';
    }
}
