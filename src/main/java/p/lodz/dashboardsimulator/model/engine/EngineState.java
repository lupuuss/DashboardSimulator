package p.lodz.dashboardsimulator.model.engine;

/**
 * Data class that describes engine state. Contains properties like speed and rpm.
 */
public class EngineState {

    private double speed;
    private long betweenTicks;
    private int rpm;
    private int gear;
    private double fuelConsumption;

    /**
     * @param speed Speed in km/h
     * @param betweenTicks Period of time between engine ticks in ms.
     * @param rpm Revolutions per minute.
     * @param gear Current gear.
     * @param fuelConsumption Fuel consumption level in liters per 100 km.
     */
    public EngineState(double speed, long betweenTicks, int rpm, int gear, double fuelConsumption) {
        this.speed = speed;
        this.betweenTicks = betweenTicks;
        this.rpm = rpm;
        this.gear = gear;
        this.fuelConsumption = fuelConsumption;
    }

    /**
     * Returns speed in km/h.
     * @return Speed in km/h
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Returns time period between two ticks in ms.
     * @return Period of time between ticks in ms.
     */
    public long getBetweenTicks() {
        return betweenTicks;
    }

    /**
     * Returns the engine revolutions per minute
     * @return Engine revolutions per mninute.
     */
    public int getRpm() {
        return rpm;
    }

    /**
     * Returns engine current gear.
     * @return engine current gear.
     */
    public int getGear() {
        return gear;
    }

    /**
     * Returns current fuel consumption in liters per 100 km.
     * @return current fuel consumption in liters per 100 km.
     */
    public double getFuelConsumption() {
        return fuelConsumption;
    }
}
