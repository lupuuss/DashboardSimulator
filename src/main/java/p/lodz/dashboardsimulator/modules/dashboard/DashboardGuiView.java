package p.lodz.dashboardsimulator.modules.dashboard;

import eu.hansolo.medusa.Gauge;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import p.lodz.dashboardsimulator.base.Injector;
import p.lodz.dashboardsimulator.base.JavaFxView;
import p.lodz.dashboardsimulator.model.light.LightsMode;
import p.lodz.dashboardsimulator.model.monitor.odometer.Mileage;
import p.lodz.dashboardsimulator.model.monitor.statistics.TravelStatistics;
import p.lodz.dashboardsimulator.modules.FxModulesRunner;
import p.lodz.dashboardsimulator.modules.Module;
import p.lodz.dashboardsimulator.utils.Utils;

import java.io.IOException;

/**
 * Implements {@link DashboardView} using JavaFX. Speed and rpm is shown using gauges.
 * All lights indicators are instances of {@link ImageView} and are hidden if light is turned off.
 * All travel statistics and odometers are shown using {@link Label}s. User controls Engine using up arrow and down arrow.
 * Cruise control is managed using text fields and checkbox. Also, user can open other views (settings, mp3 player, statistics history).
 */
public class DashboardGuiView extends JavaFxView<DashboardPresenter> implements DashboardView {

    @FXML private TextField cruiseControlActiveLimit;
    @FXML private ToggleButton cruiseControlActiveButton;
    @FXML private TextField cruiseControlSpeed;
    @FXML private CheckBox cruiseControlCheckbox;
    @FXML private Gauge speedometer;
    @FXML private Gauge tachometer;
    @FXML private ImageView turnLeftLight;
    @FXML private ImageView turnRightLight;
    @FXML private ImageView parkingLeftLight;
    @FXML private ImageView fogFrontLight;
    @FXML private ImageView fogBackLight;
    @FXML private ImageView lowBeamLight;
    @FXML private ImageView parkingRightLight;
    @FXML private ImageView highBeamLight;
    @FXML private Label odometer;
    @FXML private Label dailyOdometerOne;
    @FXML private Label dailyOdometerTwo;
    @FXML private Label maxSpeed;
    @FXML private Label avgSpeed;
    @FXML private Label travelTime;
    @FXML private Label distancePassed;
    @FXML private Label avgConsumption;
    @FXML private Label gearValue;

    DashboardPresenter presenter;

    private MenuBar menuBar;

    /**
     * Injects current {@link Scene} and {@link FxModulesRunner} to this view.
     * Also, it initializes {@link MenuBar}.
     * @param scene {@link Scene} associated with this view.
     * @param runner {@link FxModulesRunner} that created this view. It allows to run other modules.
     */
    @Override
    public void attachFx(Scene scene, FxModulesRunner runner) {
        super.attachFx(scene, runner);

        this.menuBar = new MenuBar();
        ((VBox) scene.getRoot()).getChildren().add(0, menuBar);
    }

    /**
     * Initializes {@link DashboardPresenter} using passed {@link DashboardInjector} as
     * a parameter and attaches this view to it. Sets onClicks and prepares view to interactions with user.
     * @param injector Implementation of injector that provides necessary components for presenter.
     */
    @Override
    public void start(Injector injector) {

        DashboardInjector dashboardInjector = (DashboardInjector) injector;

        scene.getRoot().requestFocus();

        presenter = new DashboardPresenter(
                dashboardInjector.getEngine(),
                dashboardInjector.getStatisticsMonitor(),
                dashboardInjector.getLightsController(),
                dashboardInjector.getOdometer(),
                dashboardInjector.getActiveCruiseControl(),
                dashboardInjector.getTravelDataRepository()
        );

        Menu fileMenu = new Menu("Plik");

        MenuItem saveStatsItem = new MenuItem("Zapisz statystyki");
        MenuItem statsHistoryItem = new MenuItem("Historia statystyk");
        MenuItem settingsItem = new MenuItem("Ustawienia");

        saveStatsItem.setOnAction(event -> presenter.saveCurrentStatsToDatabase());
        statsHistoryItem.setOnAction(event -> presenter.openStatsHistory());
        settingsItem.setOnAction(event -> presenter.openSettings());

        fileMenu.getItems().addAll(
                saveStatsItem,
                statsHistoryItem,
                settingsItem
        );

        menuBar.getMenus().add(fileMenu);

        presenter.attach(this);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> delegatePressedKeys(event.getCode()));

        scene.addEventFilter(KeyEvent.KEY_RELEASED, event -> delegateReleasedKeys(event.getCode()));

    }

    private void delegateReleasedKeys(KeyCode code) {

        switch (code) {
            case UP:
                presenter.setEngineAcceleration(false);
                break;
            case DOWN:
                presenter.setEngineBrake(false);
                break;
        }
    }

    private void delegatePressedKeys(KeyCode code) {

        switch (code) {
            case UP:
                presenter.setEngineAcceleration(true);
                break;
            case DOWN:
                presenter.setEngineBrake(true);
                break;
            case LEFT:
                presenter.triggerLeftTurnSignal();
                break;
            case RIGHT:
                presenter.triggerRightTurnSignal();
                break;
        }
    }

    /**
     * Close view and detaches presenter.
     */
    @Override
    public void close() {
        presenter.detach();
        super.close();
    }

    /**
     * Updates rpm in the associated {@link Gauge}.
     * @param rpm Engines rpm.
     */
    @Override
    public void updateRpm(int rpm) {
        tachometer.setValue(rpm);
    }

    /**
     * Updates gear value in the associated {@link Label} with id = gearValue.
     * @param gear Current gear.
     */
    @Override
    public void updateGear(int gear) {
        gearValue.setText(String.valueOf(gear));
    }

    /**
     * Updates speed in the associated {@link Gauge} with id = speedometer.
     * @param speed Engines speed.
     */
    @Override
    public void updateSpeed(double speed) {
        speedometer.setValue(speed);
    }

    @Override
    public void updateMileage(Mileage mileage) {

        double odometerValue = Utils.round(mileage.getTotalMileage(),2);
        double dailyOdometerOneValue =  Utils.round(mileage.getResettableMileages().get(0), 2);
        double dailyOdometerTwoValue = Utils.round(mileage.getResettableMileages().get(1), 2);

        odometer.setText(odometerValue + " km");
        dailyOdometerOne.setText(dailyOdometerOneValue + " km");
        dailyOdometerTwo.setText(dailyOdometerTwoValue + " km");
    }

    @Override
    public void updateEngineStats(TravelStatistics travelStatistics) {

        int currentMaxSpeed = (int)Math.ceil(travelStatistics.getMaxSpeed());
        int currentAvgSpeed = (int)Math.ceil(travelStatistics.getAvgSpeed());

        double currentDistancePassed = Math.ceil(travelStatistics.getDistance()*100)/100 ;
        double currentAvgFuelConsumption = Math.ceil(travelStatistics.getAvgFuelConsumption()*10)/10;
        int hours = (int)travelStatistics.getTravelTime()/1000/60/60;
        int minutes = (int)travelStatistics.getTravelTime()/1000/60%60;

        maxSpeed.setText(currentMaxSpeed + " km/h");
        avgSpeed.setText(currentAvgSpeed + "km/h");
        travelTime.setText(hours+ ":" + minutes +" h");
        distancePassed.setText(currentDistancePassed+ " km");
        avgConsumption.setText(currentAvgFuelConsumption+ " l/km");
    }

    /**
     * Updates left turn signal state. If true sets its indicator ({@link ImageView} with id = turnLeftLight) visible.
     * Otherwise, it is invisible.
     * @param isOn Determines if left turn signal is on/off.
     */
    @Override
    public void setLeftTurnSignalLight(boolean isOn) {
        turnLeftLight.setVisible(isOn);
    }

    /**
     * Updates right turn signal state. If true sets its indicator ({@link ImageView} with id = turnRightLight) visible.
     * Otherwise, it is invisible.
     * @param isOn Determines if right turn signal is on/off.
     */
    @Override
    public void setRightTurnSignalLight(boolean isOn) {
        turnRightLight.setVisible(isOn);
    }

    /**
     * Updates back fog lights state. If true sets its indicator ({@link ImageView} with id = fogBackLight) visible.
     * Otherwise, it is invisible.
     * @param isOn Determines if back fog lights are on/off.
     */
    @Override
    public void setBackFogLightState(boolean isOn) {
        fogBackLight.setVisible(isOn);
    }

    /**
     * Updates front fog lights state. If true sets its indicator ({@link ImageView} with id = fogFrontLight) visible.
     * Otherwise, it is invisible.
     * @param isOn Determines if front fog lights are on/off.
     */
    @Override
    public void setFrontFogLightState(boolean isOn) {
        fogFrontLight.setVisible(isOn);
    }

    /**
     * Updates parking lights state. If true sets its
     * indicators ({@link ImageView} with id = parkingLeftLight and {@link ImageView} with id = parkingRightLight) visible.
     * Otherwise, it is invisible.
     * @param isOn Determines if parking lights are on/off.
     */
    @Override
    public void setParkingLight(boolean isOn) {
        parkingLeftLight.setVisible(isOn);
        parkingRightLight.setVisible(isOn);
    }

    /**
     * Updates low beam lights state. If true sets its indicator ({@link ImageView} with id = lowBeamLight) visible.
     * Otherwise, it is invisible.
     * @param isOn Determines if low beam lights are on/off.
     */
    @Override
    public void setLowBeamLight(boolean isOn) {
        lowBeamLight.setVisible(isOn);
    }

    /**
     * Updates high beam lights state. If true sets its indicator ({@link ImageView} with id = highBeamLight) visible.
     * Otherwise, it is invisible.
     * @param isOn Determines if high beam lights are on/off.
     */
    @Override
    public void setHighBeamLight(boolean isOn) {
        highBeamLight.setVisible(isOn);
    }

    /**
     * Updates cruise control state in the {@link CheckBox} with id = cruiseControlCheckbox.
     * {@link CheckBox} is selected if cruise control is on.
     * @param isOn Determines if cruise control is on/off.
     */
    @Override
    public void setCruiseControlState(boolean isOn) {
        cruiseControlCheckbox.setSelected(isOn);
    }

    /**
     * Opens new window with statistics history using {@link FxModulesRunner}
     */
    @Override
    public void openStatsHistory() {

        try {
            runner.runModule(Module.HISTORY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens new window with settings using {@link FxModulesRunner}
     */
    @Override
    public void openSettings() {
        try {
            runner.runModule(Module.SETTINGS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens new window with MP3 Player using {@link FxModulesRunner}
     */
    @Override
    public void openMp3() {
        try {
            runner.runModule(Module.PLAYER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void notifyPresenterLeftTurn() {
        presenter.triggerLeftTurnSignal();
    }

    @FXML
    private void notifyPresenterRightTurn() {
        presenter.triggerRightTurnSignal();
    }

    @FXML
    private void notifyPresenterOdometerOneReset() {
        presenter.resetMileage(0);
    }

    @FXML
    private void notifyPresenterOdometerTwoReset() {
        presenter.resetMileage(1);
    }

    @FXML
    private void notifyPresenterFrontFogToggle() {

        presenter.toggleFogFrontLight();
    }

    @FXML
    private void notifyPresenterBackFogToggle() {
        presenter.toggleFogBackLight();
    }

    @FXML
    private void notifyPresenterPositionClick() {
        presenter.changeLightMode(LightsMode.PARKING);
    }

    @FXML
    private void notifyPresenterLowBeamClick() {
        presenter.changeLightMode(LightsMode.LOW_BEAM);
    }

    @FXML
    private void notifyPresenterHighBeamClick() {
        presenter.changeLightMode(LightsMode.HIGH_BEAM);
    }

    @FXML
    private void notifyPresenterOpenMp3() {
        presenter.openMp3();
    }

    @Override
    public void notifyCloseEvent() {
        presenter.closeView();
    }

    @FXML
    private void notifyPresenterCruiseControl() {

        if (cruiseControlCheckbox.isSelected()) {
            presenter.activateCruiseControl(cruiseControlSpeed.getText());
        } else {
            presenter.deactivateCruiseControl();
        }
    }

    @FXML
    private void notifyPresenterSpeedChanged() {
        presenter.updateCruiseControlSpeed(cruiseControlSpeed.getText());
    }

    @FXML
    private void notifyPresenterVehicleActive() {
        presenter.updateActiveCruiseControlVehicle(
                cruiseControlActiveButton.isSelected(),
                cruiseControlActiveLimit.getText()
        );
    }
}
