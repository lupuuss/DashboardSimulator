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

        subscriptions.add(engineSub);
        subscriptions.add(statisticsSub);
        subscriptions.add(mileageSub);
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
     * @param areOn Determines if user wants to turn on or off the lights.
     */
    public void setFogBackLight(boolean areOn) {
        lightsController.setFogBackLights(areOn);
    }

    /**
     * Notifies presenter about user intent to turn on/off front fog lights.
     * @param areOn Determines if user wants to turn on or off the lights.
     */
    public void setFogFrontLight(boolean areOn) {
        lightsController.setFogFrontLights(areOn);
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
