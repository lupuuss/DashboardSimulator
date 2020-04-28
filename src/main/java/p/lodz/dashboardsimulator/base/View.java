package p.lodz.dashboardsimulator.base;

import io.reactivex.Scheduler;

public interface View {

    Scheduler getViewScheduler();
}
