package p.lodz.dashboardsimulator.modules.dashboard;

import p.lodz.dashboardsimulator.base.View;
import p.lodz.dashboardsimulator.model.monitor.statistics.TravelStatistics;
import p.lodz.dashboardsimulator.model.monitor.odometer.Mileage;

/**
 * Describes interactions with view shown to the user in dashboard module.
 * Every {@link View} bounded to {@link DashboardPresenter} must extend this class.
 */
public interface DashboardView extends View<DashboardPresenter> {

    /**
     * Updates number of engine's rpm on the view.
     * @param rpm Engines rpm.
     */
    void updateRpm(int rpm);

    /**
     * Updates current gear on the view.
     * @param gear Current gear.
     */
    void updateGear(int gear);

    /**
     * Updates current speed on the view.
     * @param speed value of the speed in km/h.
     */
    void updateSpeed(double speed);

    /**
     * Updates current info about mileage.
     * @param mileage instance of {@link Mileage}.
     */
    void updateMileage(Mileage mileage);

    /**
     * Updates current engine statistics.
     * @param travelStatistics instance of {@link TravelStatistics}.
     */
    void updateEngineStats(TravelStatistics travelStatistics);

    void setCruiseControlState(boolean isOn);

    /**
     * Shows the state of left turn singal.
     * @param isOn Determines if left turn signal is on/off.
     */
    void setLeftTurnSignalLight(boolean isOn);

    /**
     * Shows the state of right turn singal.
     * @param isOn Determines if left turn signal is on/off.
     */
    void setRightTurnSignalLight(boolean isOn);

    /**
     * Shows the state of parking lights.
     * @param isOn Determines if left turn signal is on/off.
     */
    void setParkingLight(boolean isOn);

    /**
     * Shows the state of low beam lights.
     * @param isOn Determines if left turn signal is on/off.
     */
    void setLowBeamLight(boolean isOn);

    /**
     * Shows the state of high beam lights.
     * @param isOn Determines if left turn signal is on/off.
     */
    void setHighBeamLight(boolean isOn);

    /**
     * Shows the state of back fog light.
     * @param isOn Determines if back fog light is on/off.
     */
    void setBackFogLightState(boolean isOn);

    /**
     * Shows the state of front fog light.
     * @param isOn Determines if back fog light is on/off.
     */
    void setFrontFogLightState(boolean isOn);

    /**
     * Opens new view with statistics history.
     */
    void openStatsHistory();

    /**
     * Opens new view with settings.
     */
    void openSettings();

    /**
     * Opens new view with MP3 Player
     */
    void openMp3();
}
