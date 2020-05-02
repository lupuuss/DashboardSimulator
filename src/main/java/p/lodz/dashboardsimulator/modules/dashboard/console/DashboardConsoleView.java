package p.lodz.dashboardsimulator.modules.dashboard.console;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import p.lodz.dashboardsimulator.model.monitor.EngineStatistics;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardPresenter;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardView;

public class DashboardConsoleView extends DashboardView {

    private double speed;
    private double mileage;
    private EngineStatistics stats;

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
    public synchronized void updateTotalMileage(double mileage) {
        this.mileage = mileage;
        printStatus();
    }

    @Override
    public synchronized void updateEngineStats(EngineStatistics engineStatistics) {
        this.stats = engineStatistics;
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
