package p.lodz.dashboardsimulator.modules.dashboard;

import p.lodz.dashboardsimulator.base.Activity;
import p.lodz.dashboardsimulator.model.engine.Engine;
import p.lodz.dashboardsimulator.model.engine.EngineSimulator;
import p.lodz.dashboardsimulator.model.monitor.BasicMileageMonitor;
import p.lodz.dashboardsimulator.model.monitor.BasicStatisticsMonitor;
import p.lodz.dashboardsimulator.model.monitor.MileageMonitor;
import p.lodz.dashboardsimulator.model.monitor.StatisticsMonitor;
import p.lodz.dashboardsimulator.model.serialize.Serializer;
import p.lodz.dashboardsimulator.model.serialize.XmlSerializer;
import p.lodz.dashboardsimulator.modules.dashboard.console.ConsoleCommandsReader;
import p.lodz.dashboardsimulator.modules.dashboard.console.DashboardConsoleView;

public class DashboardActivity implements Activity {

    private DashboardView view;

    private Engine engine = new EngineSimulator(20, 300, 200);
    private StatisticsMonitor statisticsMonitor = new BasicStatisticsMonitor();

    private Serializer serializer = new XmlSerializer(".\\serializable\\");

    private MileageMonitor mileageMonitor = new BasicMileageMonitor(serializer, 2);
    private DashboardPresenter presenter = new DashboardPresenter(engine, statisticsMonitor, null, mileageMonitor);

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
