package p.lodz.dashboardsimulator.model.control;

/**
 * Represents vehicle only by its speed.
 */
public class Vehicle {

    private double speed;

    public Vehicle() {
        this.speed = 0.0;
    }

    /**
     * Returns vehicle speed in km/h.
     * @return vehicle speed in km/h.
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets vehicle speed to a given value.
     * @param speed Speed in km/h.
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
