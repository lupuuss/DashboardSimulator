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

    /**
     * Shows the state of left turn singal.
     * @param isOn Determines if left turn signal is on/off.
     */
    public abstract void setLeftTurnSignalLight(boolean isOn);

    /**
     * Shows the state of right turn singal.
     * @param isOn Determines if left turn signal is on/off.
     */
    public abstract void setRightTurnSignalLight(boolean isOn);

    /**
     * Shows the main lights mode that is currently set.
     * @param isOn Determines current lights mode.
     */
    public abstract void setMainLightsMode(boolean isOn);

    /**
     * Shows the state of back fog light.
     * @param isOn Determines if back fog light is on/off.
     */
    public abstract void setBackFogLightState(boolean isOn);

    /**
     * Shows the state of front fog light.
     * @param isOn Determines if back fog light is on/off.
     */
    public abstract void setFrontFogLightState(boolean isOn);
}
