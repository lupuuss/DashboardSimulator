package p.lodz.dashboardsimulator.modules.dashboard;

import io.reactivex.disposables.Disposable;
import p.lodz.dashboardsimulator.base.Presenter;
import p.lodz.dashboardsimulator.model.engine.Engine;
import p.lodz.dashboardsimulator.model.engine.EngineState;
import p.lodz.dashboardsimulator.model.light.LightsController;
import p.lodz.dashboardsimulator.model.monitor.StatisticsMonitor;
import p.lodz.dashboardsimulator.model.monitor.EngineStats;

import java.util.ArrayList;
import java.util.List;

public class DashboardPresenter extends Presenter<DashboardView> {

    private Engine engine;
    private StatisticsMonitor engineMonitor;
    private LightsController lightsController;

    private List<Disposable> subscriptions = new ArrayList<>();

    public DashboardPresenter(Engine engine, StatisticsMonitor engineMonitor, LightsController lightsController) {
        this.engine = engine;
        this.engineMonitor = engineMonitor;
        this.lightsController = lightsController;
    }

    @Override
    public void attach(DashboardView view) {
        super.attach(view);

        engine.launch();

        engineMonitor.watch(engine);

        Disposable engineSub = engine.getEngineState()
                .observeOn(currentScheduler)
                .subscribe(this::onNewEngineState);

        Disposable monitorSub = engineMonitor.getCurrentStats()
                .observeOn(currentScheduler)
                .subscribe(this::onNewEngineStats);

        subscriptions.add(engineSub);
        subscriptions.add(monitorSub);

        engine.setAcceleration(true);
    }

    private void onNewEngineStats(EngineStats engineStats) {
        view.updateEngineStats(engineStats);
    }

    private void onNewEngineState(EngineState engineState) {
        view.updateSpeed(engineState.getSpeed());
    }

    @Override
    public void detach() {
        super.detach();

        engine.stop();
        subscriptions.forEach(Disposable::dispose);
    }
}
