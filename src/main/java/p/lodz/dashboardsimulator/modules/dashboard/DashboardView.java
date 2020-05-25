package p.lodz.dashboardsimulator.modules.dashboard;

import p.lodz.dashboardsimulator.base.View;

/**
 * Describes interactions with view shown to the user in dashboard module.
 * Every {@link View} bounded to {@link DashboardPresenter} must implement this class.
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

    /**
     * Updates state of mileages on view.
     * @param total Total mileage {@link String}.
     * @param resettable Resettable mileages {@link String}s.
     */
    void updateMileage(String total, String... resettable);

    /**
     * Updates average speed state on view.
     * @param avgSpeed Well formatted {@link String} that contains average speed.
     */
    void setAverageSpeed(String avgSpeed);

    /**
     * Updates maximum speed state on view.
     * @param maximumSpeed Well formatted {@link String} that contains maximum speed.
     */
    void setMaximumSpeed(String maximumSpeed);

    /**
     * Updates distance state on view.
     * @param distance Well formatted {@link String} that contains distance.
     */
    void setDistance(String distance);

    /**
     * Updates travel time state on view.
     * @param formatDuration Well formatted {@link String} that contains travel duration.
     */
    void setTravelTime(String formatDuration);

    /**
     * Updates fuel consumption state on view.
     * @param fuelConsumption Well formatted {@link String} that contains average fuel consumption.
     */
    void setAvgFuelConsumption(String fuelConsumption);
}
