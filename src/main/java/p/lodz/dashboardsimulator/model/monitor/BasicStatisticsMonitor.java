package p.lodz.dashboardsimulator.model.monitor;

import io.reactivex.Observable;
import p.lodz.dashboardsimulator.model.engine.EngineState;

import java.util.concurrent.atomic.AtomicReference;

public class BasicStatisticsMonitor extends StatisticsMonitor {

    @Override
    public Observable<EngineStats> getCurrentStats() {
        return null;
    }

}
