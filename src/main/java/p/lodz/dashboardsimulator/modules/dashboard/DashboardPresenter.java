package p.lodz.dashboardsimulator.modules.dashboard;

import io.reactivex.disposables.Disposable;
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
import p.lodz.dashboardsimulator.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes whole logic layer behind DashboardView.
 * Communicates the view with {@link Engine}, engine monitors, and all data sources.
 * Interprets user intent and response to them.
 */
public class DashboardPresenter extends Presenter<DashboardView> {

    private final String speedMetric = "km/h";
    private final String distanceMetric = "km";
    private final String fuelConsumptionMetric = "L/100km";
    private final int decimalPlaces = 2;

    private Engine engine;

    private StatisticsMonitor engineMonitor;
    private LightsController lightsController;
    private Odometer odometer;
    private ActiveCruiseControl cruiseControl;
    private TravelDataRepository repository;

    private Vehicle vehicle = new Vehicle();

    private List<Disposable> subscriptions = new ArrayList<>();

    /**
     * Initializes presenter with passed implementation of used interfaces.
     * @param engine Implementation of {@link Engine} to be used by presenter.
     * @param engineMonitor Implementation of {@link StatisticsMonitor} to be used by presenter.
     * @param lightsController Implementation of {@link LightsController} to be used by presenter.
     * @param odometer Implementation of {@link Odometer} to be used by presenter.
     * @param cruiseControl Implementation of {@link ActiveCruiseControl} to be used by presenter.
     * @param repository Implementation of {@link TravelDataRepository} to be used by presenter.
     */
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

        view.setAverageSpeed(Utils.round(engineStats.getAvgSpeed(), decimalPlaces) + " " + speedMetric);
        view.setMaximumSpeed(Utils.round(engineStats.getMaxSpeed(), decimalPlaces) + speedMetric);
        view.setDistance(Utils.round(engineStats.getDistance(), decimalPlaces) + distanceMetric);
        view.setTravelTime(Utils.formatDuration(engineStats.getTravelTime()));
        view.setAvgFuelConsumption(Utils.round(engineStats.getAvgFuelConsumption(), 2) + " " + fuelConsumptionMetric);
    }

    private void updateStateOnView(EngineState engineState) {

        view.updateSpeed(engineState.getSpeed());
        view.updateGear(engineState.getGear());
        view.updateRpm(engineState.getRpm());
    }

    private void updateMileageOnView(Mileage mileage) {

        double odometerValue = Utils.round(mileage.getTotalMileage(),2);
        double dailyOdometerOneValue =  Utils.round(mileage.getResettableMileages().get(0), 2);
        double dailyOdometerTwoValue = Utils.round(mileage.getResettableMileages().get(1), 2);

        view.updateMileage(
                odometerValue + " " + distanceMetric,
                dailyOdometerOneValue + " " + distanceMetric,
                dailyOdometerTwoValue + " " + distanceMetric
        );
    }

    /**
     * Sets acceleration on {@link Engine}.
     * @param isOn Determines if user wants to turn on/off acceleration.
     */
    public void setEngineAcceleration(boolean isOn) {
        engine.setAcceleration(isOn);
    }

    /**
     * Sets brake on {@link Engine}.
     * @param isOn Determines if user wants to turn on/off brakes.
     */
    public void setEngineBrake(boolean isOn) {
        engine.setBrake(isOn);
    }

    /**
     * Resets {@link Odometer} resettable mileage with given index.
     * @param n Number of mileage to reset.
     */
    public void resetMileage(int n) {
        odometer.resetMileage(n);
    }

    /**
     * Changes lights mode in {@link LightsController}.
     * @param mode Determines mode of light.
     */
    public void changeLightMode(LightsMode mode) {

        lightsController.setMainLightMode(mode);
    }

    /**
     * Triggers left turn in {@link LightsController}.
     */
    public void triggerLeftTurnSignal() {
        lightsController.triggerLeftTurnSignal();
    }

    /**
     * Triggers left turn in {@link LightsController}.
     */
    public void triggerRightTurnSignal() {
        lightsController.triggerRightTurnSignal();
    }

    /**
     * Switches back fog lights state left in {@link LightsController}.
     */
    public void toggleFogBackLight() {
        lightsController.setFogBackLights(
                !lightsController.isFogBackLightOn()
        );
    }

    /**
     * Switches front fog lights state left in {@link LightsController}.
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
     * Parses passed speed. If speed is valid activates cruise control in {@link ActiveCruiseControl}.
     * If passed speed is invalid, shows message to the user.
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

    /**
     * Updates cruise control speed if passed speed is valid.
     */
    public void updateCruiseControlSpeed(String speed) {

        if (parseSpeed(speed) <= 0) {
            view.showMessage("Speed input must be a positive numeric value in km/h!", View.MessageType.ERROR);
            return;
        }

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

    /**
     * Saves current statistics to repository and notifies user about the results.
     */
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
     * Closes the view.
     */
    public void closeView() {
        view.close();
    }

    /**
     *  Opens stats history using view.
     */
    public void openStatsHistory() {
        view.openStatsHistory();
    }

    /**
     *  Opens settings using view.
     */
    public void openSettings() {
        view.openSettings();
    }

    /**
     *  Opens mp3 player using view.
     */
    public void openMp3() {
        view.openMp3();
    }

    /**
     * Updates vehicle in front of a car.
     * @param selected Determines if vehicle appears.
     * @param speedInput Vehicle speed input in km/h from user (might be invalid).
     */
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
