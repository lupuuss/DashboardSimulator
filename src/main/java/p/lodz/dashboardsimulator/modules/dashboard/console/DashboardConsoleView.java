package p.lodz.dashboardsimulator.modules.dashboard.console;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import p.lodz.dashboardsimulator.base.Injector;
import p.lodz.dashboardsimulator.model.light.LightsMode;
import p.lodz.dashboardsimulator.model.monitor.statistics.TravelStatistics;
import p.lodz.dashboardsimulator.model.monitor.odometer.Mileage;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardInjector;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardPresenter;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardView;

import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DashboardConsoleView implements DashboardView {

    private double speed;
    private double rpm;
    private Mileage mileage;
    private TravelStatistics stats;

    private Observable<String> commands;
    private Disposable commandsSubscription;

    private DashboardPresenter presenter;

    private int lastStatusLength = 0;

    public DashboardConsoleView(Observable<String> commands) {
        this.commands = commands;
    }

    @Override
    public void start(Injector injector) {

        DashboardInjector dashboardInjector = (DashboardInjector) injector;

        presenter = new DashboardPresenter(
                dashboardInjector.getEngine(),
                dashboardInjector.getStatisticsMonitor(),
                dashboardInjector.getLightsController(),
                dashboardInjector.getOdometer(),
                dashboardInjector.getActiveCruiseControl(),
                dashboardInjector.getTravelDataRepository()
        );

        presenter.attach(this);

        commandsSubscription = commands.subscribe(this::notifyPresenter);
    }

    private void notifyPresenter(String s) {

        switch (s) {
            case "w":
                presenter.setEngineAcceleration(true);
                break;
            case "s":
                presenter.setEngineAcceleration(false);
                break;
            case "r":
                presenter.closeView();
                break;
            case "d":
                presenter.triggerRightTurnSignal();
                break;
            case "a":
                presenter.triggerLeftTurnSignal();
                break;
            case "1":
                presenter.changeLightMode(LightsMode.OFF);
                break;
            case "2":
                presenter.changeLightMode(LightsMode.PARKING);
                break;
            case "3":
                presenter.changeLightMode(LightsMode.LOW_BEAM);
                break;
            case "4":
                presenter.changeLightMode(LightsMode.HIGH_BEAM);
            case "add":
                presenter.saveCurrentStatsToDatabase();
                break;

        }
    }

    @Override
    public void updateGear(int gear) {

    }

    @Override
    public synchronized void updateSpeed(double speed) {
        this.speed = speed;
        reprintStatus();
    }

    @Override
    public void updateMileage(String total, String... resettable) {

    }

    @Override
    public void setAverageSpeed(String avgSpeed) {

    }

    @Override
    public void setMaximumSpeed(String maximumSpeed) {

    }

    @Override
    public void setDistance(String distance) {

    }

    @Override
    public void setTravelTime(String formatDuration) {

    }

    @Override
    public void setAvgFuelConsumption(String fuelConsumption) {

    }

    @Override
    public void setCruiseControlState(boolean isOn) {

    }

    @Override
    public void setLeftTurnSignalLight(boolean isOn) {
        showMessage("Left turn state changed: " + isOn, MessageType.INFO);
    }

    @Override
    public void setRightTurnSignalLight(boolean isOn) {
        showMessage("Right turn state changed: " + isOn, MessageType.INFO);
    }

    @Override
    public void setParkingLight(boolean isOn) {
        showMessage("Parking lights state changed: " + isOn, MessageType.INFO);
    }

    @Override
    public void setLowBeamLight(boolean isOn) {
        showMessage("Low beam lights state changed: " + isOn, MessageType.INFO);
    }

    @Override
    public void setHighBeamLight(boolean isOn) {
        showMessage("High beam lights state changed: " + isOn, MessageType.INFO);
    }

    @Override
    public void setBackFogLightState(boolean isOn) {
        showMessage("Back fog state changed: " + isOn, MessageType.INFO);
    }

    @Override
    public void setFrontFogLightState(boolean isOn) {
        showMessage("Front fog state changed: " + isOn, MessageType.INFO);
    }

    @Override
    public void openStatsHistory() {
        showMessage("Not supported in cli mode!", MessageType.ERROR);
    }

    @Override
    public void openSettings() {
        showMessage("Not supported in cli mode!", MessageType.ERROR);
    }

    @Override
    public void updateRpm(int rpm) {
        this.rpm = rpm;
        reprintStatus();
    }

    @Override
    public void openMp3() {

    }

    @Override
    public void showMessage(String message, MessageType type) {
        clearStatus();
        System.out.println("[" + type  + "] " + message);
        printStatus();
    }

    @Override
    public void askUser(String message, Consumer<Boolean> onUserDecision) {

    }

    private void reprintStatus() {
        clearStatus();
        printStatus();
    }

    private void clearStatus() {
        System.out.print("\r" + Stream.generate(() -> " ").limit(lastStatusLength).collect(Collectors.joining("")));
        System.out.print("\r");
    }

    private void printStatus() {

        String status = "Speed: " + speed + " | Mileage: " + mileage + " | " + stats;

        lastStatusLength = status.length();
        System.out.print("\r" + status);
    }

    @Override
    public void close() {

        commandsSubscription.dispose();
        presenter.detach();
    }

    @Override
    public Scheduler getViewScheduler() {
        return Schedulers.single();
    }
}
