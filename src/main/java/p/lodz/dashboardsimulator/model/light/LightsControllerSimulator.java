package p.lodz.dashboardsimulator.model.light;

import io.reactivex.Observable;

/**
 * Simulates standard behaviour of lights in every car.
 */
public class LightsControllerSimulator implements LightsController {
    @Override
    public void triggerLeftTurnSignal() {

    }

    @Override
    public void triggerRightTurnSignal() {

    }

    @Override
    public void setMainLightMode(LightsMode mode) {

    }

    @Override
    public void setFogFrontLights(boolean areOn) {

    }

    @Override
    public void setFogBackLights(boolean areOn) {

    }

    @Override
    public Observable<Boolean> getLeftTurnState() {
        return null;
    }

    @Override
    public Observable<Boolean> getRightTurnState() {
        return null;
    }

    @Override
    public Observable<LightsMode> getMainLightMode() {
        return null;
    }

    @Override
    public Observable<Boolean> getFogBackLightsState() {
        return null;
    }

    @Override
    public Observable<Boolean> getFogFrontLightsState() {
        return null;
    }
}
