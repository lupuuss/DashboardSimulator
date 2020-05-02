package p.lodz.dashboardsimulator.modules.dashboard;

import p.lodz.dashboardsimulator.base.View;
import p.lodz.dashboardsimulator.model.monitor.EngineStatistics;

public abstract class DashboardView extends View<DashboardPresenter> {

    public abstract void updateSpeed(double speed);

    public abstract void updateTotalMileage(double mileage);

    public abstract void updateEngineStats(EngineStatistics engineStatistics);
}
