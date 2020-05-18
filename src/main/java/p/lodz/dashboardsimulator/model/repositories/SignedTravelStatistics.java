package p.lodz.dashboardsimulator.model.repositories;

import p.lodz.dashboardsimulator.model.monitor.statistics.TravelStatistics;

/**
 * Travel statistics that are extended by id from database.
 */
public class SignedTravelStatistics extends TravelStatistics {

    private int id;
    private java.util.Date date;

    /**
     * @param avgSpeed           Average speed in km/h.
     * @param maxSpeed           Maximum speed in in km/h.
     * @param travelTime         Travel time in ms.
     * @param distance           Distance in km.
     * @param avgFuelConsumption Average fuel consumption in liters per kilometer
     */
    public SignedTravelStatistics(
            int id,
            double avgSpeed,
            double maxSpeed,
            long travelTime,
            double distance,
            double avgFuelConsumption,
            java.sql.Timestamp date
    ) {

        super(avgSpeed, maxSpeed, travelTime, distance, avgFuelConsumption);
        this.id = id;
        this.date = new java.util.Date(date.getTime());
    }

    /**
     * Returns travel statistics id.
     * @return Integer that identifies statistics in the database.
     */
    public int getId() {
        return id;
    }

    public java.util.Date getDate() {
        return date;
    }
}
