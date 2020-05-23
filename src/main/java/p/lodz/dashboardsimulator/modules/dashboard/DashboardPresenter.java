package p.lodz.dashboardsimulator.modules.dashboard;

import io.reactivex.disposables.Disposable;
import javafx.scene.control.TextField;
import p.lodz.dashboardsimulator.base.Presenter;
import p.lodz.dashboardsimulator.base.View;
import p.lodz.dashboardsimulator.model.control.ActiveCruiseControl;
import p.lodz.dashboardsimulator.model.control.Vehicle;
import p.lodz.dashboardsimulator.model.engine.Engine;
import p.lodz.dashboardsimulator.model.engine.EngineState;
import p.lodz.dashboardsimulator.model.light.LightsController;
import p.lodz.dashboardsimulator.model.light.LightsMode;
import p.lodz.dashboardsimulator.model.monitor.odometer.Mileage;
import p.lodz.dashboardsimulator.model.monitor.odometer.Odometer;
import p.lodz.dashboardsimulator.model.monitor.statistics.StatisticsMonitor;
import p.lodz.dashboardsimulator.model.monitor.statistics.TravelStatistics;
import p.lodz.dashboardsimulator.model.repositories.TravelDataRepository;

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
    private ActiveCruiseControl cruiseControl;
    private TravelDataRepository repository;

    private Vehicle vehicle = new Vehicle();

    private List<Disposable> subscriptions = new ArrayList<>();

    public DashboardPresenter(
            Engine engine,
            StatisticsMonitor engineMonitor,
            LightsController lightsController,
            Odometer odometer,
            ActiveCruiseControl cruiseControl,
            TravelDataRepository repository
    ) {
        this.engine = engine;
        this.engineMonitor = engineMonitor;
        this.lightsController = lightsController;
        this.odometer = odometer;
        this.cruiseControl = cruiseControl;
        this.repository = repository;
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
        view.updateGear(engineState.getGear());
        view.updateRpm(engineState.getRpm());
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

    private double parseSpeed(String speedInput) {

        double speed = -1;

        try {
            speed = Double.parseDouble(speedInput);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return speed;
    }


    /**
     * Notifies presenter about user intent to activate cruise control with given speed.
     * @param speedInput Speed limit input in km/h from user (might be invalid).
     */
    public void activateCruiseControl(String speedInput) {

        double speed = parseSpeed(speedInput);

        if (speed <= 0) {
            view.setCruiseControlState(false);
            view.showMessage("Speed input must be a positive numeric value in km/h!", View.MessageType.ERROR);
        } else {
            view.setCruiseControlState(true);
            cruiseControl.keepEngineSpeed(engine, speed);
        }
    }

    public void updateCruiseControlSpeed(String speed) {

        if (cruiseControl.isOn()) {
            deactivateCruiseControl();
        }

        activateCruiseControl(speed);
    }

    /**
     * Notifies presenter about user intent to deactivate cruise control.
     */
    public void deactivateCruiseControl() {
        cruiseControl.dropControl();
    }

    public void saveCurrentStatsToDatabase() {

        TravelStatistics tmp = engineMonitor.getLastStatistics();

        if (tmp == null) {

            view.showMessage("No data to save!", DashboardView.MessageType.ERROR);
            return;
        }

        Disposable addToDatabase = repository.addTravelStatistics(tmp)
                .observeOn(currentScheduler)
                .doOnNext(result -> {

                    if (result) {
                        view.showMessage("Statistics were successfully saved!", DashboardView.MessageType.INFO);
                    } else {
                        view.showMessage("Statistics couldn't be saved!", DashboardView.MessageType.WARNING);
                    }

                })
                .doOnError(error -> view.showMessage(
                        "An error occurred while saving the data! \n\n" + error.getMessage(),
                        DashboardView.MessageType.ERROR
                ))
                .subscribe();

        subscriptions.add(addToDatabase);
    }

    /**
     * Stops engine, and unsubscribes all data sources.
     */
    @Override
    public void detach() {
        super.detach();

        engine.stop();
        odometer.closeAndSave();
        engineMonitor.closeAndSave();
        subscriptions.forEach(Disposable::dispose);
    }

    /**
     * Notifies presenter about user intent to close a view.
     */
    public void closeView() {
        view.close();
    }

    public void openStatsHistory() {
        view.openStatsHistory();
    }

    public void openSettings() {
        view.openSettings();
    }

    public void openMp3() {
        view.openMp3();
    }

    public void updateActiveCruiseControlVehicle(boolean selected, String speedInput) {

        if (selected) {

            double speed = parseSpeed(speedInput);

            if (speed >= 0) {
                vehicle.setSpeed(speed);
                cruiseControl.setFrontVehicle(vehicle);
            } else {
                view.showMessage("Speed input must be a numeric value in km/h!", View.MessageType.ERROR);
            }

        } else {

            cruiseControl.setFrontVehicle(null);
        }

    }
}
