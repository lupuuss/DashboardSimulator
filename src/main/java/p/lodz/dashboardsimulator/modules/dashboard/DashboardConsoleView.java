package p.lodz.dashboardsimulator.modules.dashboard;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import p.lodz.dashboardsimulator.model.monitor.EngineStats;

public class DashboardConsoleView implements DashboardView {
    @Override
    public void updateSpeed(double speed) {

    }

    @Override
    public void updateTotalMileage(double mileage) {

    }

    @Override
    public void updateEngineStats(EngineStats engineStats) {

    }

    @Override
    public Scheduler getViewScheduler() {
        return Schedulers.single();
    }
}
