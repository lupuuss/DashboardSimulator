package p.lodz.dashboardsimulator.model.monitor.statistics;

import io.reactivex.Observable;
import p.lodz.dashboardsimulator.model.monitor.EngineMonitor;
import p.lodz.dashboardsimulator.model.monitor.statistics.TravelStatistics;

/**
 * Super class for every engine monitor that is designed to produce temporary statistics described in {@link TravelStatistics}.
 */
public abstract class StatisticsMonitor extends EngineMonitor {


    /**
     * Returns observable travel statistics.
     * @return Instance of {@link Observable} that provides any update in {@link TravelStatistics}
     */
    public abstract Observable<TravelStatistics> getCurrentStats();

    /**
     * Returns last produced travel statistics.
     * @return Instance of {@link TravelStatistics} with last produced statistics.
     */
    public abstract TravelStatistics getLastStatistics();

    public abstract void closeAndSave();
}
