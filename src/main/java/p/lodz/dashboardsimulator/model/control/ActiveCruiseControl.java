package p.lodz.dashboardsimulator.model.control;

import p.lodz.dashboardsimulator.model.engine.Engine;

/**
 * Describes every active cruise control. If active, keeps engine speed at a given level.
 * Also, it can adjust the speed of the vehicle in front of a car. Cruise control always chose lower speeds.
 * If user defined lower speed than a vehicle in front, cruise control keeps this speed.
 * If speed defined by the user is higher than a vehicle in front, cruise control adjusts engine speed to this vehicle.
 * If the vehicle is not defined cruise control uses only speed given by the user.
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

    /**
     * Return true if control is active.
     * @return True if control is active.
     */
    boolean isOn();
}
