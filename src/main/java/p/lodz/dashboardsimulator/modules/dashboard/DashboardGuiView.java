package p.lodz.dashboardsimulator.modules.dashboard;

import eu.hansolo.medusa.Gauge;
import io.reactivex.Scheduler;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
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

import java.io.IOException;

public class DashboardGuiView extends JavaFxView<DashboardPresenter> implements DashboardView {

    @FXML private Gauge speedometer;
    @FXML private Gauge tachometer;
    @FXML private ImageView turnLeftLight;
    @FXML private ImageView turnRightLight;
    @FXML private ImageView parkingLeftLight;
    @FXML private ImageView fogFrontLight;
    @FXML private ImageView fogBackLight;
    @FXML private ImageView lowBeamLight;
    @FXML private ImageView positionRightLight;
    @FXML private ImageView highBeamLight;
    @FXML private Label odometer;
    @FXML private Label dailyOdometerOne;
    @FXML private Label dailyOdometerTwo;
    @FXML private Label maxSpeed;
    @FXML private Label avgSpeed;
    @FXML private Label travelTime;
    @FXML private Label distancePassed;
    @FXML private Label avgConsumption;

    DashboardPresenter presenter;

    private MenuBar menuBar;

    @Override
    public void attachFx(Scene scene, FxModulesRunner runner) {
        super.attachFx(scene, runner);

        this.menuBar = new MenuBar();
        ((VBox) scene.getRoot()).getChildren().add(0, menuBar);
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

    @Override
    public void notifyCloseEvent() {
        presenter.closeView();
    }

    @Override
    public void start(Injector injector) {

        DashboardInjector dashboardInjector = (DashboardInjector) injector;

        presenter = new DashboardPresenter(
                dashboardInjector.getEngine(),
                dashboardInjector.getStatisticsMonitor(),
                dashboardInjector.getLightsController(),
                dashboardInjector.getOdometer(),
                dashboardInjector.getTravelDataRepository()
        );

        Menu fileMenu = new Menu("Plik");

        MenuItem saveStatsItem = new MenuItem("Zapisz statystyki");
        MenuItem statsHistoryItem = new MenuItem("Historia statystyk");
        MenuItem settingsItem = new MenuItem("Ustawienia");

        saveStatsItem.setOnAction(event -> presenter.saveCurrentStatsToDatabase());
        statsHistoryItem.setOnAction(event -> presenter.openStatsHistory());

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

    @Override
    public void close() {
        presenter.detach();
    }

    @Override
    public void updateSpeed(double speed) {
        speedometer.setValue(speed);
    }

    @Override
    public void updateMileage(Mileage mileage) {

        double odometerValue =  Math.ceil(mileage.getTotalMileage() * 100)/100;
        double dailyOdometerOneValue =  Math.ceil(mileage.getResettableMileages().get(0) * 100)/100;
        double dailyOdometerTwoValue =  Math.ceil(mileage.getResettableMileages().get(1) * 100)/100;
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

    @Override
    public void setLeftTurnSignalLight(boolean isOn) {
        turnLeftLight.setVisible(isOn);
    }

    @Override
    public void setRightTurnSignalLight(boolean isOn) {
        turnRightLight.setVisible(isOn);
    }

    @Override
    public void setBackFogLightState(boolean isOn) {
        fogBackLight.setVisible(isOn);
    }

    @Override
    public void setFrontFogLightState(boolean isOn) {
        fogFrontLight.setVisible(isOn);
    }

    @Override
    public void setParkingLight(boolean isOn) {
        parkingLeftLight.setVisible(isOn);
        positionRightLight.setVisible(isOn);
    }

    @Override
    public void setLowBeamLight(boolean isOn) {
        lowBeamLight.setVisible(isOn);
    }

    @Override
    public void setHighBeamLight(boolean isOn) {
        highBeamLight.setVisible(isOn);
    }

    @Override
    public void openStatsHistory() {

        try {
            runner.runModule(Module.HISTORY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Scheduler getViewScheduler() {
        return JavaFxScheduler.platform();
    }
}
