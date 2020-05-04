package p.lodz.dashboardsimulator.model.monitor.statistics;

import p.lodz.dashboardsimulator.utils.Utils;

/**
 * Typical data class. Contains info about particular travel.
 */
public class TravelStatistics {

    private final int roundToDecimal = 3;

    private double avgSpeed;
    private double maxSpeed;
    private long travelTime;
    private double distance;
    private double avgFuelConsumption;

    /**
     * @param avgSpeed Average speed in km/h.
     * @param maxSpeed Maximum speed in in km/h.
     * @param travelTime Travel time in ms.
     * @param distance Distance in km.
     * @param avgFuelConsumption Average fuel consumption in liters per kilometer
     */
    public TravelStatistics(double avgSpeed, double maxSpeed, long travelTime, double distance, double avgFuelConsumption) {
        this.avgSpeed = avgSpeed;
        this.maxSpeed = maxSpeed;
        this.travelTime = travelTime;
        this.distance = distance;
        this.avgFuelConsumption = avgFuelConsumption;
    }

    /**
     * Returns average speed during travel in km/h.
     * @return Average speed in km/h.
     */
    public double getAvgSpeed() {
        return avgSpeed;
    }

    /**
     * Returns maximum speed during a travel in km/h.
     * @return maximum speed in km/h.
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Returns travel time in ms.
     * @return Travel time in ms.
     */
    public long getTravelTime() {
        return travelTime;
    }

    /**
     * Returns traveled distance in km.
     * @return Distance in km.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Returns average fuel consumption in liters per kilometer.
     * @return Average fuel use in liters per kilometer.
     */
    public double getAvgFuelConsumption() {
        return avgFuelConsumption;
    }

    /**
     * Standard implementation of toString (json like).
     * @return {@link String} that contains all the travel data rounded to {@link TravelStatistics#roundToDecimal} decimal places.
     */
    @Override
    public String toString() {
        return "EngineStatistics{" +
                "avg=" + Utils.round(avgSpeed, roundToDecimal) +
                ", max=" + Utils.round(maxSpeed, roundToDecimal) +
                ", time=" + travelTime +
                ", dist=" + Utils.round(distance, roundToDecimal) +
                ", avgFuel=" + Utils.round(avgFuelConsumption,roundToDecimal) +
                '}';
    }
}
