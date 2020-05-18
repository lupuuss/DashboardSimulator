package p.lodz.dashboardsimulator.model.control;

import io.reactivex.disposables.Disposable;
import p.lodz.dashboardsimulator.model.engine.Engine;
import p.lodz.dashboardsimulator.model.engine.EngineState;

public class ActiveCruiseControlSimulator implements ActiveCruiseControl {

    private Engine currentEngine;
    private double speedLimit = 0;

    private Disposable engineDispose;
    private Vehicle vehicle;

    @Override
    public void keepEngineSpeed(Engine engine, double speed) {
        currentEngine = engine;
        speedLimit = speed;

        engineDispose = engine
                .getEngineState()
                .subscribe(this::keepFunction);
    }

    private void keepFunction(EngineState engineState) {

        double speedToKeep;

        if (vehicle == null) {
            speedToKeep = speedLimit;
        } else {
            speedToKeep = Math.min(vehicle.getSpeed(), speedLimit);
        }

        if (engineState.getSpeed() < speedToKeep) {
            currentEngine.setAcceleration(true);
        } else {
            currentEngine.setAcceleration(false);
        }

    }

    @Override
    public void dropControl() {
        engineDispose.dispose();
        engineDispose = null;
        currentEngine = null;
        speedLimit = 0;
    }

    @Override
    public void setFrontVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
