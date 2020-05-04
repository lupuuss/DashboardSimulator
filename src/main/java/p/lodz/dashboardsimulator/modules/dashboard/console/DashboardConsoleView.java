package p.lodz.dashboardsimulator.modules.dashboard.console;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import p.lodz.dashboardsimulator.model.monitor.statistics.TravelStatistics;
import p.lodz.dashboardsimulator.model.monitor.odometer.Mileage;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardPresenter;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardView;

public class DashboardConsoleView extends DashboardView {

    private double speed;
    private Mileage mileage;
    private TravelStatistics stats;

    private Observable<String> commands;
    private Disposable commandsSubscription;

    public DashboardConsoleView(Observable<String> commands) {
        this.commands = commands;
    }

    @Override
    public void attach(DashboardPresenter presenter) {
        super.attach(presenter);

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

    private void printStatus() {

        System.out.print("\rSpeed: " + speed + " | Mileage: " + mileage + " | " + stats);
    }

    @Override
    public void close() {
        commandsSubscription.dispose();
    }

    @Override
    public Scheduler getViewScheduler() {
        return Schedulers.single();
    }
}
