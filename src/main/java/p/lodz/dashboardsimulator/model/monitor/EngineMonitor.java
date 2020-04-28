package p.lodz.dashboardsimulator.model.monitor;

import io.reactivex.Observable;
import p.lodz.dashboardsimulator.model.engine.Engine;
import p.lodz.dashboardsimulator.model.engine.EngineState;

public abstract class EngineMonitor {

    protected Observable<EngineState> engineState;

    public void watch(Engine engine) {
        engineState = engine.getEngineState();
    }
}
