package p.lodz.dashboardsimulator.modules.dashboard;

import eu.hansolo.medusa.Gauge;
import io.reactivex.Scheduler;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import p.lodz.dashboardsimulator.base.Injector;
import p.lodz.dashboardsimulator.model.monitor.odometer.Mileage;
import p.lodz.dashboardsimulator.model.monitor.statistics.TravelStatistics;

public class DashboardGuiView implements DashboardView {

    @FXML private Gauge speedometer;
    @FXML private Gauge tachometer;
    @FXML private ImageView turnLeft;
    @FXML private ImageView turnRight;
    @FXML private ImageView positionLeft;
    @FXML private ImageView fogLeft;
    @FXML private ImageView lowbeamLight;
    @FXML private ImageView positionRight;
    @FXML private ImageView fogRight;
    @FXML private ImageView roadLight;
    @FXML private Label odometer;
    @FXML private Label dailyOdometer;
    @FXML private Label maxSpeed;
    @FXML private Label avgSpeed;
    @FXML private Label travelTime;
    @FXML private Label distancePassed;
    @FXML private Label avgConsumption;

    DashboardPresenter presenter;

    @Override
    public void start(Injector injector) {

        DashboardInjector dashboardInjector = (DashboardInjector) injector;

        presenter = new DashboardPresenter(
                dashboardInjector.getEngine(),
                dashboardInjector.getStatisticsMonitor(),
                dashboardInjector.getLightsController(),
                dashboardInjector.getOdometer()
        );

        presenter.attach(this);

    }

    @Override
    public void close() {
        presenter.detach();
    }

    public void notifyPresenterCloseEvent() {
        presenter.closeView();
    }

    public void onClickLeftTurn(MouseEvent mouseEvent) {
        presenter.triggerLeftTurnSignal();
    }

    @Override
    public void updateSpeed(double speed) {
        speedometer.setValue(speed);
    }

    @Override
    public void updateMileage(Mileage mileage) {
        odometer.setText(mileage.getTotalMileage() + " km");
    }

    @Override
    public void updateEngineStats(TravelStatistics travelStatistics) {

        maxSpeed.setText(travelStatistics.getMaxSpeed() + " km/h");
        travelTime.setText(travelStatistics.getTravelTime() + " ms");
        avgSpeed.setText(travelStatistics.getAvgSpeed() + "km/h");
        distancePassed.setText(travelStatistics.getDistance() + " km");
        avgConsumption.setText(travelStatistics.getAvgFuelConsumption() + " l/km");
    }

    @Override
    public void setLeftTurnSignalLight(boolean isOn) {
        turnLeft.setVisible(isOn);
    }

    @Override
    public void setRightTurnSignalLight(boolean isOn) {

    }

    @Override
    public void setMainLightsMode(boolean isOn) {

    }

    @Override
    public void setBackFogLightState(boolean isOn) {

    }

    @Override
    public void setFrontFogLightState(boolean isOn) {

    }

    @Override
    public Scheduler getViewScheduler() {
        return JavaFxScheduler.platform();
    }
}
