package p.lodz.dashboardsimulator.model.light;

import io.reactivex.Observable;

/**
 * Describes the interface for every lights controller.
 */
public interface LightsController {

    /**
     * Switches between on/off state of left turn signal. Initial state should be off.
     * After setting to on, expected behaviour is blinking state of left turn signal, and it should also set right turn singal state to off.
     */
    void triggerLeftTurnSignal();

    /**
     * Switches between on/off state of right turn signal. Initial state should be off.
     * After setting to on, expected behaviour is blinking state of right turn signal, and it should also set left turn singal state to off.
     */
    void  triggerRightTurnSignal();

    /**
     * Changes mode of the main lights. Initial mode should be {@link LightsMode#OFF}
     * @param mode Determines lights mode with enum {@link LightsMode}.
     */
    void setMainLightMode(LightsMode mode);

    /**
     * Switches between on/off state of front fog lights. Initial state should be off.
     * @param areOn Determines state of front fog lights (on/off).
     */
    void setFogFrontLights(boolean areOn);

    /**
     * Switches between on/off state of back fog lights. Initial state should be off.
     * @param areOn Determines state of back fog lights (on/off).
     */
    void setFogBackLights(boolean areOn);

    /**
     * Returns observable state of left turn signal.
     * @return Instance of {@link Observable} that allows to watch the state of left turn signal.
     */
    Observable<Boolean> getLeftTurnState();

    /**
     * Returns observable state of right turn signal.
     * @return Instance of {@link Observable} that allows to watch the state of right turn signal.
     */
    Observable<Boolean> getRightTurnState();

    /**
     * Returns observable mode of main lights.
     * @return Instance of {@link Observable} that allows to watch the mode of the main lights.
     */
    Observable<LightsMode> getMainLightMode();

    /**
     * Returns observable state of fog front lights.
     * @return Instance of {@link Observable} that allows to watch the state of fog back lights.
     */
    Observable<Boolean> getFogBackLightsState();

    /**
     * Returns observable state of fog back lights.
     * @return Instance of {@link Observable} that allows to watch the state of fog front lights.
     */
    Observable<Boolean> getFogFrontLightsState();
}
