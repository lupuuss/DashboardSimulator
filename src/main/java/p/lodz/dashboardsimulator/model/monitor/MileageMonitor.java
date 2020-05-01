package p.lodz.dashboardsimulator.model.monitor;

import io.reactivex.Observable;

public abstract class MileageMonitor extends EngineMonitor {

    public abstract int getResettableCount();

    public abstract Observable<Double> getTotalMileage();

    public abstract Observable<Double> getResettableMileage(int n);

    public abstract void resetMileage(int n);

}
