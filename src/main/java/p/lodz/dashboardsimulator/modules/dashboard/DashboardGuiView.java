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
import p.lodz.dashboardsimulator.modules.FxModulesRunner;
import p.lodz.dashboardsimulator.modules.FxModule;

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
     * @param injector Expected to be instance of {@link DashboardInjector} that provides model classes to the presenter.
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

        Menu fileMenu = new Menu("File");

        MenuItem saveStatsItem = new MenuItem("Save statistics");
        MenuItem statsHistoryItem = new MenuItem("Statistics history");
        MenuItem settingsItem = new MenuItem("Settings");

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

    /**
     * Updates mileages labels. Total mileages is set on {@link Label} with id = odometer.
     * Two resettable mileages are set on labels with ids dailyOdometerOne and dailyOdometerTwo
     * @param total Total mileage {@link String}.
     * @param resettable Resettable mileages {@link String}s.
     */
    @Override
    public void updateMileage(String total, String... resettable) {


        odometer.setText(total);
        dailyOdometerOne.setText(resettable[0]);
        dailyOdometerTwo.setText(resettable[1]);
    }

    /**
     * Updates average speed on {@link Label} with id = averageSpeed.
     * @param avgSpeed Well formatted {@link String} that contains average speed.
     */
    @Override
    public void setAverageSpeed(String avgSpeed) {

        this.avgSpeed.setText(avgSpeed);

    }

    /**
     * Updates maximum speed on {@link Label} with id = maxSpeed.
     * @param maximumSpeed Well formatted {@link String} that contains maximum speed.
     */
    @Override
    public void setMaximumSpeed(String maximumSpeed) {
        maxSpeed.setText(maximumSpeed);
    }

    /**
     * Updates distance on {@link Label} with id = distancePassed.
     * @param distance Well formatted {@link String} that contains distance.
     */
    @Override
    public void setDistance(String distance) {
        distancePassed.setText(distance);
    }

    /**
     * Updates travel time on {@link Label} with id = travelTime.
     * @param formatDuration Well formatted {@link String} that contains travel duration.
     */
    @Override
    public void setTravelTime(String formatDuration) {
        travelTime.setText(formatDuration);
    }

    /**
     * Updates average fuel consumption on {@link Label} with id = avgConsumption.
     * @param fuelConsumption Well formatted {@link String} that contains average fuel consumption.
     */
    @Override
    public void setAvgFuelConsumption(String fuelConsumption) {
        avgConsumption.setText(fuelConsumption);
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
            runner.runModule(FxModule.HISTORY);
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
            runner.runModule(FxModule.SETTINGS);
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
            runner.runModule(FxModule.PLAYER);
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
