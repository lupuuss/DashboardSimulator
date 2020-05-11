package p.lodz.dashboardsimulator.modules.dashboard;

import eu.hansolo.medusa.Gauge;
import io.reactivex.Scheduler;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
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
    @FXML private ImageView turnLeftLight;
    @FXML private ImageView turnRightLight;
    @FXML private ImageView positionLeftLight;
    @FXML private ImageView fogLeftLight;
    @FXML private ImageView fogRightLight;
    @FXML private ImageView lowBeamLight;
    @FXML private ImageView positionRightLight;
    @FXML private ImageView roadLight;
    @FXML private Label odometer;
    @FXML private Label dailyOdometerOne;
    @FXML private Label dailyOdometerTwo;
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
    public void setRightTurnSignalLight(boolean isOn) { turnRightLight.setVisible(isOn); }

    @Override
    public void setMainLightsMode(boolean isOn) { }

    @Override
    public void setBackFogLightState(boolean isOn) {fogRightLight.setVisible(isOn); }

    @Override
    public void setFrontFogLightState(boolean isOn) { fogLeftLight.setVisible(isOn); }

    @Override
    public Scheduler getViewScheduler() {
        return JavaFxScheduler.platform();
    }
}
