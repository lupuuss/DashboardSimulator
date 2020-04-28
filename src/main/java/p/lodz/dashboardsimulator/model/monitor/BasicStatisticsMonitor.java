package p.lodz.dashboardsimulator.model.monitor;

import io.reactivex.Observable;
import p.lodz.dashboardsimulator.model.engine.EngineState;

public class BasicStatisticsMonitor extends StatisticsMonitor {

    @Override
    public Observable<EngineStats> getCurrentStats() {
        return Observable.empty();
    }

    @Override
    protected void onEngineStateChange(EngineState engineState) {
        System.out.println("Monitor Thread: " + Thread.currentThread().getName());
    }
}
