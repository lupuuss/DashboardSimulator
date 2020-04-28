package p.lodz.dashboardsimulator.model.monitor;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import p.lodz.dashboardsimulator.model.engine.Engine;
import p.lodz.dashboardsimulator.model.engine.EngineState;

public abstract class EngineMonitor {

    private Disposable subscription;

    public void watch(Engine engine) {
        subscription = engine.getEngineState()
                .subscribeOn(Schedulers.computation())
                .subscribe(this::onEngineStateChange);
    }

    protected abstract void onEngineStateChange(EngineState engineState);

    public void shutdown() {
        subscription.dispose();
    }
}
