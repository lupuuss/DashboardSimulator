package p.lodz.dashboardsimulator.model.light;

import io.reactivex.Observable;

public interface LightsController {

    void triggerLeftTurnSignal();
    void triggerRightTurnSignal();

    void setMainLightMode(LightsMode mode);

    void setFogFrontLights(boolean areOn);
    void setFogBackLights(boolean areOn);

    Observable<Boolean> getLeftTurnState();
    Observable<Boolean> getRightTurnState();
    Observable<LightsMode> getMainLightMode();
}
