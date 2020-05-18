package p.lodz.dashboardsimulator.model.control;

import p.lodz.dashboardsimulator.model.engine.Engine;

public interface ActiveCruiseControl {

    void keepEngineSpeed(Engine engine, double speed);

    void dropControl();

    void setFrontVehicle(Vehicle vehicle);
}
