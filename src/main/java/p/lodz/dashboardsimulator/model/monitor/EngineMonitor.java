package p.lodz.dashboardsimulator.model.monitor;

import io.reactivex.Observable;
import p.lodz.dashboardsimulator.model.engine.Engine;
import p.lodz.dashboardsimulator.model.engine.EngineState;

/**
 * Super class for every engine monitor. Provides method for observation initialization.
 */
public abstract class EngineMonitor {

    protected Observable<EngineState> engineState;

    /**
     * Injects instance of any class that implements {@link Engine}. After this method call {@link EngineState} is
     * provided by observable {@link EngineMonitor#engineState}.
     * @param engine Instance of {@link Engine} that will be monitored.
     */
    public void watch(Engine engine) {
        engineState = engine.getEngineState();
    }
}
