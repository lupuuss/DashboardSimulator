package p.lodz.dashboardsimulator.model.repositories;

import p.lodz.dashboardsimulator.model.monitor.statistics.TravelStatistics;

/**
 * Travel statistics that are extended by id from database.
 */
public class SignedTravelStatistics extends TravelStatistics {

    private int id;
    private java.util.Date date;

    /**
     * Initialize object properties with given values.
     * @param id                 Travel statistics identifier.
     * @param avgSpeed           Average speed in km/h.
     * @param maxSpeed           Maximum speed in in km/h.
     * @param travelTime         Travel time in ms.
     * @param distance           Distance in km.
     * @param avgFuelConsumption Average fuel consumption in liters per kilometer
     * @param date               Creation date as SQL timestamp that will be converted into {@link java.util.Date}.
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
     * Initialize object properties with given values.
     * @param id                 Travel statistics identifier.
     * @param avgSpeed           Average speed in km/h.
     * @param maxSpeed           Maximum speed in in km/h.
     * @param travelTime         Travel time in ms.
     * @param distance           Distance in km.
     * @param avgFuelConsumption Average fuel consumption in liters per kilometer
     * @param date               Creation date.
     */
    public SignedTravelStatistics(
            int id,
            double avgSpeed,
            double maxSpeed,
            long travelTime,
            double distance,
            double avgFuelConsumption,
            java.util.Date date
    ) {

        super(avgSpeed, maxSpeed, travelTime, distance, avgFuelConsumption);
        this.id = id;
        this.date = date;
    }

    /**
     * Returns travel statistics id.
     * @return Integer that identifies statistics in the database.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the date the statistics were added to the database.
     * @return The {@link java.util.Date} the statistics were added to the database.
     */
    public java.util.Date getDate() {
        return date;
    }
}
