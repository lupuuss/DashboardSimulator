package p.lodz.dashboardsimulator.modules.dashboard;

import p.lodz.dashboardsimulator.base.View;
import p.lodz.dashboardsimulator.model.monitor.EngineStats;

public interface DashboardView extends View {

    void updateSpeed(double speed);

    void updateTotalMileage(double mileage);

    void updateEngineStats(EngineStats engineStats);
}
