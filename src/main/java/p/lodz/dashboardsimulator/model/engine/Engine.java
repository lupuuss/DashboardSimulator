package p.lodz.dashboardsimulator.model.engine;

import io.reactivex.Observable;

/**
 * Describes simplified car engine interface.
 */
public interface Engine {

    /**
     * Should start the engine and perform all the initialization of the engine.
     */
    void launch();

    /**
     * Should change state of the engine acceleration (on/off).
     * @param isOn determines if engine should speed up or not.
     */
    void setAcceleration(boolean isOn);

    /**
     * Should change state of the engine brakes (on/off).
     * @param isOn determines if engine brakes are on/off.
     */
    void setBrake(boolean isOn);

    /**
     * Should stop the engine and perform all clean up.
     */
    void stop();

    /**
     * Should return observable object that allows to observe engine state.
     * @return Observable engine state.
     */
    Observable<EngineState> getEngineState();
}
