package p.lodz.dashboardsimulator.model.engine;

/**
 * Data class that describes engine state.
 */
public class EngineState {

    private double speed;
    private long betweenTicks;
    private int rpm;
    private int gear;
    /**
     * @param speed Speed in km/h
     * @param betweenTicks Period of time between engine ticks in ms.
     * @param rpm
     * @param gear
     */
    public EngineState(double speed, long betweenTicks, int rpm, int gear) {
        this.speed = speed;
        this.betweenTicks = betweenTicks;
        this.rpm = rpm;
        this.gear = gear;
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

    public int getRpm() {
        return rpm;
    }

    public int getGear() {
        return gear;
    }
}
