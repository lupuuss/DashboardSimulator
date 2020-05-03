package p.lodz.dashboardsimulator.modules.dashboard;

import io.reactivex.disposables.Disposable;
import p.lodz.dashboardsimulator.base.Presenter;
import p.lodz.dashboardsimulator.model.engine.Engine;
import p.lodz.dashboardsimulator.model.engine.EngineState;
import p.lodz.dashboardsimulator.model.light.LightsController;
import p.lodz.dashboardsimulator.model.light.LightsMode;
import p.lodz.dashboardsimulator.model.monitor.Mileage;
import p.lodz.dashboardsimulator.model.monitor.MileageMonitor;
import p.lodz.dashboardsimulator.model.monitor.StatisticsMonitor;
import p.lodz.dashboardsimulator.model.monitor.EngineStatistics;

import java.util.ArrayList;
import java.util.List;

public class DashboardPresenter extends Presenter<DashboardView> {

    private Engine engine;

    private StatisticsMonitor engineMonitor;
    private LightsController lightsController;
    private MileageMonitor mileageMonitor;

    private List<Disposable> subscriptions = new ArrayList<>();

    public DashboardPresenter(
            Engine engine,
            StatisticsMonitor engineMonitor,
            LightsController lightsController,
            MileageMonitor mileageMonitor
    ) {
        this.engine = engine;
        this.engineMonitor = engineMonitor;
        this.lightsController = lightsController;
        this.mileageMonitor = mileageMonitor;
    }

    @Override
    public void attach(DashboardView view) {
        super.attach(view);

        engine.launch();

        engineMonitor.watch(engine);
        mileageMonitor.watch(engine);

        Disposable engineSub = engine.getEngineState()
                .observeOn(currentScheduler)
                .subscribe(this::updateStateOnView);

        Disposable statisticsSub = engineMonitor.getCurrentStats()
                .observeOn(currentScheduler)
                .subscribe(this::updateStatisticsOnView);

        Disposable mileageSub = mileageMonitor.getMileage()
                .observeOn(currentScheduler)
                .subscribe(this::updateMileageOnView);

        subscriptions.add(engineSub);
        subscriptions.add(statisticsSub);
        subscriptions.add(mileageSub);
    }

    private void updateStatisticsOnView(EngineStatistics engineStats) {
        view.updateEngineStats(engineStats);
    }

    private void updateStateOnView(EngineState engineState) {
        view.updateSpeed(engineState.getSpeed());
    }

    private void updateMileageOnView(Mileage mileage) {
        view.updateMileage(mileage);
    }

    public void setEngineAcceleration(boolean isOn) {
        engine.setAcceleration(isOn);
    }

    public void setEngineBrake(boolean isOn) {
        engine.setBrake(isOn);
    }

    public void resetMileage(int n) {
        mileageMonitor.resetMileage(n);
    }

    public void changeLightMode(LightsMode mode) {
        lightsController.setMainLightMode(mode);
    }

    public void triggerLeftTurnSignal() {
        lightsController.triggerLeftTurnSignal();
    }

    public void triggerRightTurnSignal() {
        lightsController.triggerRightTurnSignal();
    }

    public void setFogBackLight(boolean areOn) {
        lightsController.setFogBackLights(areOn);
    }

    public void setFogFrontLight(boolean areOn) {
        lightsController.setFogFrontLights(areOn);
    }

    public void activateCruiseControl(double speed) {
        // TODO
    }

    public void deactivateCruiseControl() {
        // TODO
    }

    @Override
    public void detach() {
        super.detach();

        engine.stop();
        mileageMonitor.closeAndSave();
        subscriptions.forEach(Disposable::dispose);
    }

    public void closeView() {
        view.close();
    }
}
