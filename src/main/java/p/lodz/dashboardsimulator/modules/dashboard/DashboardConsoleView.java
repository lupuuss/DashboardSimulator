package p.lodz.dashboardsimulator.modules.dashboard;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import p.lodz.dashboardsimulator.model.monitor.EngineStatistics;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DashboardConsoleView implements DashboardView {

    private double speed;
    private double mileage;
    private EngineStatistics stats;

    private int toClean = 0;

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
    public Scheduler getViewScheduler() {
        return Schedulers.single();
    }
}
