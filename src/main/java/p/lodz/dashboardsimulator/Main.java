package p.lodz.dashboardsimulator;

import p.lodz.dashboardsimulator.model.engine.Engine;
import p.lodz.dashboardsimulator.model.engine.EngineSimulator;
import p.lodz.dashboardsimulator.model.monitor.BasicStatisticsMonitor;
import p.lodz.dashboardsimulator.model.monitor.StatisticsMonitor;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardConsoleView;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardPresenter;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardView;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Thread: " + Thread.currentThread().getName());

        Engine engine = new EngineSimulator(10, 300, 300);
        StatisticsMonitor statisticsMonitor = new BasicStatisticsMonitor();

        DashboardView view = new DashboardConsoleView();
        DashboardPresenter presenter = new DashboardPresenter(engine, statisticsMonitor, null);

        presenter.attach(view);

        Thread.sleep(10_000);

        presenter.detach();
    }
}
