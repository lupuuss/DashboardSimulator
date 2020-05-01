package p.lodz.dashboardsimulator.model.monitor;

import io.reactivex.Observable;

public abstract class StatisticsMonitor extends EngineMonitor {

    public abstract Observable<EngineStatistics> getCurrentStats();
}
