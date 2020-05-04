package p.lodz.dashboardsimulator.modules.dashboard;

import p.lodz.dashboardsimulator.base.Activity;
import p.lodz.dashboardsimulator.model.engine.Engine;
import p.lodz.dashboardsimulator.model.engine.EngineSimulator;
import p.lodz.dashboardsimulator.model.monitor.odometer.BasicOdometer;
import p.lodz.dashboardsimulator.model.monitor.statistics.BasicStatisticsMonitor;
import p.lodz.dashboardsimulator.model.monitor.odometer.Odometer;
import p.lodz.dashboardsimulator.model.monitor.statistics.StatisticsMonitor;
import p.lodz.dashboardsimulator.model.serialize.Serializer;
import p.lodz.dashboardsimulator.model.serialize.XmlSerializer;
import p.lodz.dashboardsimulator.modules.dashboard.console.ConsoleCommandsReader;
import p.lodz.dashboardsimulator.modules.dashboard.console.DashboardConsoleView;

public class DashboardActivity implements Activity {

    private DashboardView view;

    private Engine engine = new EngineSimulator(20, 300, 200);
    private StatisticsMonitor statisticsMonitor = new BasicStatisticsMonitor();

    private Serializer serializer = new XmlSerializer(".\\serializable\\");

    private Odometer odometer = new BasicOdometer(serializer, 2);
    private DashboardPresenter presenter = new DashboardPresenter(engine, statisticsMonitor, null, odometer);

    @Override
    public void start() {

        ConsoleCommandsReader reader = new ConsoleCommandsReader();

        view = new DashboardConsoleView(reader.read());

        view.attach(presenter);
        presenter.attach(view);

        reader.await();

        stop();
    }

    @Override
    public void stop() {

        presenter.detach();
        view.detach();
    }
}
