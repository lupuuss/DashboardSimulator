package p.lodz.dashboardsimulator.model.engine;

import io.reactivex.Observable;

/**
 * Describes simplified car engine interface.
 */
public interface Engine {

    /**
     * Starts the engine and perform all the initialization of the engine.
     */
    void launch();

    /**
     * Changes state of the engine acceleration (on/off).
     * @param isOn Determines if engine should speed up or not.
     */
    void setAcceleration(boolean isOn);

    /**
     * Changes state of the engine brakes (on/off).
     * @param isOn Determines if engine brakes are on or off.
     */
    void setBrake(boolean isOn);

    /**
     * Stops the engine and performs all clean up.
     */
    void stop();

    /**
     * Returns an {@link Observable} object that allows to watch current {@link EngineState}.
     * @return {@link Observable} that streams {@link EngineState}.
     */
    Observable<EngineState> getEngineState();
}
