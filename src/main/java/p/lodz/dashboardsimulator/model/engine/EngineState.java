package p.lodz.dashboardsimulator.model.engine;

/**
 * Data class that describes engine state.
 */
public class EngineState {

    private double speed;
    private long betweenTicks;

    /**
     * @param speed Speed in km/h
     * @param betweenTicks Period of time between engine ticks in ms.
     */
    public EngineState(double speed, long betweenTicks) {
        this.speed = speed;
        this.betweenTicks = betweenTicks;
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
}
