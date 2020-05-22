package p.lodz.dashboardsimulator.model.control;

import io.reactivex.disposables.Disposable;
import p.lodz.dashboardsimulator.model.engine.Engine;
import p.lodz.dashboardsimulator.model.engine.EngineState;

public class ActiveCruiseControlSimulator implements ActiveCruiseControl {

    private Engine currentEngine;
    private double speedLimit = 0;

    private Disposable engineDispose;
    private Vehicle frontVehicle;

    /**
     * Starts to observe passed engine using {@link io.reactivex.Observable}.
     * On every change adjusts the engine acceleration to keep vehicle speed at given level.
     * @param engine {@link Engine} to be controlled.
     * @param speed Speed level in km/h that has to be kept.
     */
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

        if (frontVehicle == null) {
            speedToKeep = speedLimit;
        } else {
            speedToKeep = Math.min(frontVehicle.getSpeed(), speedLimit);
        }

        if (engineState.getSpeed() < speedToKeep) {
            currentEngine.setAcceleration(true);
        } else {
            currentEngine.setAcceleration(false);
        }

    }

    /**
     * Stops engine subscription and engine is no longer controlled.
     */
    @Override
    public void dropControl() {
        engineDispose.dispose();
        engineDispose = null;
        currentEngine = null;
        speedLimit = 0;
    }

    /**
     * Sets vehicle in front of a car. Its speed has higher priority than
     * speed passed in {@link ActiveCruiseControlSimulator#keepEngineSpeed(Engine, double)}
     * @param vehicle Instance of {@link Vehicle} which speed is to be followed.
     */
    @Override
    public void setFrontVehicle(Vehicle vehicle) {
        this.frontVehicle = vehicle;
    }
}
