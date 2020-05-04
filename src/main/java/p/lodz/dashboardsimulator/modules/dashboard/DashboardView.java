package p.lodz.dashboardsimulator.modules.dashboard;

import p.lodz.dashboardsimulator.base.View;
import p.lodz.dashboardsimulator.model.monitor.statistics.TravelStatistics;
import p.lodz.dashboardsimulator.model.monitor.odometer.Mileage;

/**
 * Every {@link View} bounded to {@link DashboardPresenter} must extend this class.
 * Methods in this class are supposed to change the state of view.
 */
public abstract class DashboardView extends View<DashboardPresenter> {

    /**
     * Updates current speed on the view.
     * @param speed value of the speed in km/h.
     */
    public abstract void updateSpeed(double speed);

    /**
     * Updates current info about mileage.
     * @param mileage instance of {@link Mileage}.
     */
    public abstract void updateMileage(Mileage mileage);

    /**
     * Updates current engine statistics.
     * @param travelStatistics instance of {@link TravelStatistics}.
     */
    public abstract void updateEngineStats(TravelStatistics travelStatistics);
}
