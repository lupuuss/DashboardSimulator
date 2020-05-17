package p.lodz.dashboardsimulator.modules.dashboard;

import p.lodz.dashboardsimulator.base.GlobalInjector;
import p.lodz.dashboardsimulator.base.Injector;
import p.lodz.dashboardsimulator.model.engine.Engine;
import p.lodz.dashboardsimulator.model.engine.EngineSimulator;
import p.lodz.dashboardsimulator.model.light.LightsController;
import p.lodz.dashboardsimulator.model.light.LightsControllerSimulator;
import p.lodz.dashboardsimulator.model.monitor.odometer.BasicOdometer;
import p.lodz.dashboardsimulator.model.monitor.odometer.Odometer;
import p.lodz.dashboardsimulator.model.monitor.statistics.BasicStatisticsMonitor;
import p.lodz.dashboardsimulator.model.monitor.statistics.StatisticsMonitor;
import p.lodz.dashboardsimulator.model.repositories.TravelDataRepository;

public class DashboardInjector implements Injector {

    private Engine engine;
    private StatisticsMonitor statisticsMonitor;
    private Odometer odometer;
    private LightsController lightsController;
    private TravelDataRepository travelDataRepository;

    @Override
    public void init(Injector parentInjector) {

        GlobalInjector globalInjector = (GlobalInjector) parentInjector;

        engine = new EngineSimulator(20, 300, 50);
        statisticsMonitor = new BasicStatisticsMonitor();
        odometer = new BasicOdometer(globalInjector.getSerializer(), 2);
        lightsController = new LightsControllerSimulator();
        travelDataRepository = globalInjector.getTravelDataRepository();
    }

    public Engine getEngine() {
        return engine;
    }

    public StatisticsMonitor getStatisticsMonitor() {
        return statisticsMonitor;
    }

    public Odometer getOdometer() {
        return odometer;
    }

    public LightsController getLightsController() {
        return lightsController;
    }

    public TravelDataRepository getTravelDataRepository() {
        return travelDataRepository;
    }
}
