package p.lodz.dashboardsimulator.modules.dashboard;

import p.lodz.dashboardsimulator.base.GlobalInjector;
import p.lodz.dashboardsimulator.base.Injector;
import p.lodz.dashboardsimulator.model.control.ActiveCruiseControl;
import p.lodz.dashboardsimulator.model.control.ActiveCruiseControlSimulator;
import p.lodz.dashboardsimulator.model.engine.Engine;
import p.lodz.dashboardsimulator.model.engine.EngineSimulator;
import p.lodz.dashboardsimulator.model.light.LightsController;
import p.lodz.dashboardsimulator.model.light.LightsControllerSimulator;
import p.lodz.dashboardsimulator.model.monitor.odometer.BasicOdometer;
import p.lodz.dashboardsimulator.model.monitor.odometer.Odometer;
import p.lodz.dashboardsimulator.model.monitor.statistics.BasicStatisticsMonitor;
import p.lodz.dashboardsimulator.model.monitor.statistics.StatisticsMonitor;
import p.lodz.dashboardsimulator.model.repositories.TravelDataRepository;
import p.lodz.dashboardsimulator.model.settings.Settings;

/**
 * Contains model classes for dashboard module
 */
public class DashboardInjector implements Injector {

    private Engine engine;
    private StatisticsMonitor statisticsMonitor;
    private Odometer odometer;
    private LightsController lightsController;
    private TravelDataRepository travelDataRepository;
    private ActiveCruiseControl activeCruiseControl;

    /**
     * Initializes DashboardInjector. Expects {@link GlobalInjector} as parameter.
     * @param parentInjector Parent injector that shares some instances with this injector or is requried to correct creation of its instances.
     */
    @Override
    public void init(Injector parentInjector) {

        GlobalInjector globalInjector = (GlobalInjector) parentInjector;

        Settings settings = globalInjector
                .getSettingsManager()
                .getSettings();

        engine = new EngineSimulator(
                settings.getAccelerationConst(),
                settings.getMaximumSpeed(),
                settings.getBetweenEngineTicks()
        );
        statisticsMonitor = new BasicStatisticsMonitor(globalInjector.getSerializer());
        odometer = new BasicOdometer(globalInjector.getSerializer(), 2);
        lightsController = new LightsControllerSimulator();
        activeCruiseControl = new ActiveCruiseControlSimulator();
        travelDataRepository = globalInjector.getTravelDataRepository();
    }

    /**
     * Returns engine implementation -{@link EngineSimulator}.
     * @return Instance of {@link EngineSimulator}.
     */
    public Engine getEngine() {
        return engine;
    }

    /**
     * Returns statistics monitor - {@link BasicStatisticsMonitor}
     * @return Instance of {@link BasicStatisticsMonitor}.
     */
    public StatisticsMonitor getStatisticsMonitor() {
        return statisticsMonitor;
    }

    /**
     * Returns odometer implementation - {@link BasicOdometer}
     * @return Instance of {@link BasicOdometer}.
     */
    public Odometer getOdometer() {
        return odometer;
    }

    /**
     * Returns lights controller implementation - {@link LightsControllerSimulator}
     * @return Instance of {@link LightsControllerSimulator}.
     */
    public LightsController getLightsController() {
        return lightsController;
    }
    /**
     * Returns implementation of {@link TravelDataRepository} received from {@link GlobalInjector}
     * @return Implementation of {@link TravelDataRepository}
     */
    public TravelDataRepository getTravelDataRepository() {
        return travelDataRepository;
    }
    /**
     * Returns active cruise control implementation - {@link ActiveCruiseControlSimulator}
     * @return Instance of {@link ActiveCruiseControlSimulator}.
     */
    public ActiveCruiseControl getActiveCruiseControl() {
        return activeCruiseControl;
    }
}
