package p.lodz.dashboardsimulator.model.repositories;

import p.lodz.dashboardsimulator.model.monitor.statistics.TravelStatistics;

/**
 * Travel statistics that are extended by id from database.
 */
public class SignedTravelStatistics extends TravelStatistics {

    private int id;

    /**
     * @param avgSpeed           Average speed in km/h.
     * @param maxSpeed           Maximum speed in in km/h.
     * @param travelTime         Travel time in ms.
     * @param distance           Distance in km.
     * @param avgFuelConsumption Average fuel consumption in liters per kilometer
     */
    public SignedTravelStatistics(int id, double avgSpeed, double maxSpeed, long travelTime, double distance, double avgFuelConsumption) {
        super(avgSpeed, maxSpeed, travelTime, distance, avgFuelConsumption);
        this.id = id;
    }

    /**
     * Returns travel statistics id.
     * @return Integer that identifies statistics in the database.
     */
    public int getId() {
        return id;
    }
}
