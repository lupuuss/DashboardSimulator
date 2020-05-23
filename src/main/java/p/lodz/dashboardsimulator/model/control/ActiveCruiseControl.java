package p.lodz.dashboardsimulator.model.control;

import p.lodz.dashboardsimulator.model.engine.Engine;

/**
 * Describes every active cruise control.
 */
public interface ActiveCruiseControl {

    /**
     * Keeps engine speed on passed level if front vehicle drives faster or is not present.
     * Otherwise speed is adjusted to a vehicle in front of a car.
     * @param engine {@link Engine} to be controlled.
     * @param speed Speed level in km/h that has to be kept.
     */
    void keepEngineSpeed(Engine engine, double speed);

    /**
     * Stops control over the engine.
     */
    void dropControl();

    /**
     * Sets a vehicle in front of a car.
     * @param vehicle Instance of {@link Vehicle} which speed is to be followed.
     */
    void setFrontVehicle(Vehicle vehicle);

    boolean isOn();
}
