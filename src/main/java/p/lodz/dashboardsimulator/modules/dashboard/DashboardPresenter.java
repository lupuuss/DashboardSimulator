package p.lodz.dashboardsimulator.modules.dashboard;

import io.reactivex.disposables.Disposable;
import p.lodz.dashboardsimulator.base.Presenter;
import p.lodz.dashboardsimulator.model.engine.Engine;
import p.lodz.dashboardsimulator.model.engine.EngineState;
import p.lodz.dashboardsimulator.model.light.LightsController;
import p.lodz.dashboardsimulator.model.light.LightsMode;
import p.lodz.dashboardsimulator.model.monitor.odometer.Mileage;
import p.lodz.dashboardsimulator.model.monitor.odometer.Odometer;
import p.lodz.dashboardsimulator.model.monitor.statistics.StatisticsMonitor;
import p.lodz.dashboardsimulator.model.monitor.statistics.TravelStatistics;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes whole logic layer behind DashboardView.
 */
public class DashboardPresenter extends Presenter<DashboardView> {

    private Engine engine;

    private StatisticsMonitor engineMonitor;
    private LightsController lightsController;
    private Odometer odometer;

    private List<Disposable> subscriptions = new ArrayList<>();

    public DashboardPresenter(
            Engine engine,
            StatisticsMonitor engineMonitor,
            LightsController lightsController,
            Odometer odometer
    ) {
        this.engine = engine;
        this.engineMonitor = engineMonitor;
        this.lightsController = lightsController;
        this.odometer = odometer;
    }

    /**
     * Launches engine and every engine's monitor. Also, it subscribes all data sources.
     * @param view that is bounded to presenter.
     */
    @Override
    public void attach(DashboardView view) {
        super.attach(view);

        engine.launch();

        engineMonitor.watch(engine);
        odometer.watch(engine);

        Disposable engineSub = engine.getEngineState()
                .observeOn(currentScheduler)
                .subscribe(this::updateStateOnView);

        Disposable statisticsSub = engineMonitor.getCurrentStats()
                .observeOn(currentScheduler)
                .subscribe(this::updateStatisticsOnView);

        Disposable mileageSub = odometer.getMileage()
                .observeOn(currentScheduler)
                .subscribe(this::updateMileageOnView);

        Disposable leftTurnSub = lightsController.getLeftTurnState()
                .observeOn(currentScheduler)
                .subscribe(this::updateLeftTurnStateOnView);

        Disposable rightTurnSub = lightsController.getRightTurnState()
                .observeOn(currentScheduler)
                .subscribe(this::updateRightTurnStateOnView);

        Disposable frontFogSub = lightsController.getFogFrontLightsState()
                .observeOn(currentScheduler)
                .subscribe(this::updateFogFrontLightStateOnView);

        Disposable backFogSub = lightsController.getFogBackLightsState()
                .observeOn(currentScheduler)
                .subscribe(this::updateFogBackLightStateOnView);

        Disposable mainLightSub = lightsController.getMainLightMode()
                .observeOn(currentScheduler)
                .subscribe(this::updateMainLightStateOnView);

        subscriptions.add(rightTurnSub);
        subscriptions.add(leftTurnSub);
        subscriptions.add(engineSub);
        subscriptions.add(frontFogSub);
        subscriptions.add(backFogSub);
        subscriptions.add(mainLightSub);
        subscriptions.add(statisticsSub);
        subscriptions.add(mileageSub);
    }

    private void updateMainLightStateOnView(LightsMode lightsMode) {

        view.setParkingLight(false);
        view.setLowBeamLight(false);
        view.setHighBeamLight(false);

        switch (lightsMode) {
            case HIGH_BEAM:
                view.setHighBeamLight(true);
            case LOW_BEAM:
                view.setLowBeamLight(true);
            case PARKING:
                view.setParkingLight(true);
                break;
        }

    }

    private void updateFogBackLightStateOnView(Boolean backFogState) {
        view.setBackFogLightState(backFogState);
    }

    private void updateFogFrontLightStateOnView(Boolean frontFogState) {
        view.setFrontFogLightState(frontFogState);
    }

    private void updateRightTurnStateOnView(Boolean rightTurnState) {
        view.setRightTurnSignalLight(rightTurnState);
    }

    private void updateLeftTurnStateOnView(Boolean leftTurnState) {
        view.setLeftTurnSignalLight(leftTurnState);
    }

    private void updateStatisticsOnView(TravelStatistics engineStats) {
        view.updateEngineStats(engineStats);
    }

    private void updateStateOnView(EngineState engineState) {
        view.updateSpeed(engineState.getSpeed());
    }

    private void updateMileageOnView(Mileage mileage) {
        view.updateMileage(mileage);
    }

    /**
     * Notifies presenter about user intent to change acceleration state.
     * @param isOn Determines if user wants to turn on/off acceleration.
     */
    public void setEngineAcceleration(boolean isOn) {
        engine.setAcceleration(isOn);
    }

    /**
     * Notifies presenter about user intent to change brakes state.
     * @param isOn Determines if user wants to turn on/off brakes.
     */
    public void setEngineBrake(boolean isOn) {
        engine.setBrake(isOn);
    }

    /**
     * Notifies presenter about user intent to reset Mileage
     * @param n Number of mileage to reset.
     */
    public void resetMileage(int n) {
        odometer.resetMileage(n);
    }

    /**
     * Notifies presenter about user intent to change lights mode.
     * @param mode Determines mode of light.
     */
    public void changeLightMode(LightsMode mode) {

        lightsController.setMainLightMode(mode);
    }

    /**
     * Notifies presenter about user intent to switch left turn signal state.
     */
    public void triggerLeftTurnSignal() {
        lightsController.triggerLeftTurnSignal();
    }

    /**
     * Notifies presenter about user intent to switch right turn signal state.
     */
    public void triggerRightTurnSignal() {
        lightsController.triggerRightTurnSignal();
    }

    /**
     * Notifies presenter about user intent to turn on/off back fog lights.
     */
    public void toggleFogBackLight() {
        lightsController.setFogBackLights(
                !lightsController.isFogBackLightOn()
        );
    }

    /**
     * Notifies presenter about user intent to turn on/off front fog lights.
     */
    public void toggleFogFrontLight() {
        lightsController.setFogFrontLights(
                !lightsController.isFogFrontLightOn()
        );
    }

    /**
     * Notifies presenter about user intent to activate cruise control with given speed.
     * @param speed Speed limit for cruise control in km/h.
     */
    public void activateCruiseControl(double speed) {
        // TODO
    }

    /**
     * Notifies presenter about user intent to deactivate cruise control.
     */
    public void deactivateCruiseControl() {
        // TODO
    }

    /**
     * Stops engine, and unsubscribes all data sources.
     */
    @Override
    public void detach() {
        super.detach();

        engine.stop();
        odometer.closeAndSave();
        subscriptions.forEach(Disposable::dispose);
    }

    /**
     * Notifies presenter about user intent to close a view.
     */
    public void closeView() {
        view.close();
    }
}
