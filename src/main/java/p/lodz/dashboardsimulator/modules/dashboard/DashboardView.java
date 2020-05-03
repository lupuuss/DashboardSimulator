package p.lodz.dashboardsimulator.modules.dashboard;

import p.lodz.dashboardsimulator.base.View;
import p.lodz.dashboardsimulator.model.monitor.EngineStatistics;
import p.lodz.dashboardsimulator.model.monitor.Mileage;

public abstract class DashboardView extends View<DashboardPresenter> {

    public abstract void updateSpeed(double speed);

    public abstract void updateMileage(Mileage mileage);

    public abstract void updateEngineStats(EngineStatistics engineStatistics);
}
