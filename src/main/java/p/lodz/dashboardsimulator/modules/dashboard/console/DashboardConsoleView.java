package p.lodz.dashboardsimulator.modules.dashboard.console;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import p.lodz.dashboardsimulator.base.Injector;
import p.lodz.dashboardsimulator.model.monitor.statistics.TravelStatistics;
import p.lodz.dashboardsimulator.model.monitor.odometer.Mileage;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardInjector;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardPresenter;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardView;

public class DashboardConsoleView implements DashboardView {

    private double speed;
    private Mileage mileage;
    private TravelStatistics stats;

    private Observable<String> commands;
    private Disposable commandsSubscription;

    private DashboardPresenter presenter;

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
        }
    }

    @Override
    public synchronized void updateSpeed(double speed) {
        this.speed = speed;
        printStatus();
    }

    @Override
    public synchronized void updateMileage(Mileage mileage) {
        this.mileage = mileage;
        printStatus();
    }

    @Override
    public synchronized void updateEngineStats(TravelStatistics travelStatistics) {
        this.stats = travelStatistics;
        printStatus();
    }

    @Override
    public void setLeftTurnSignalLight(boolean isOn) {

    }

    @Override
    public void setRightTurnSignalLight(boolean isOn) {

    }

    @Override
    public void setParkingLight(boolean isOn) {

    }

    @Override
    public void setLowBeamLight(boolean isOn) {

    }

    @Override
    public void setHighBeamLight(boolean isOn) {

    }

    @Override
    public void setBackFogLightState(boolean isOn) {

    }

    @Override
    public void setFrontFogLightState(boolean isOn) {

    }

    @Override
    public void openStatsHistory() {

    }

    @Override
    public void updateRpm(int rpm) {

    }

    @Override
    public void showMessage(String message, MessageType type) {

    }

    private void printStatus() {

        System.out.print("\rSpeed: " + speed + " | Mileage: " + mileage + " | " + stats);
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
