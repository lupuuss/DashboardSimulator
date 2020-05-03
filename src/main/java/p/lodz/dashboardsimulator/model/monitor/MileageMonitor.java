package p.lodz.dashboardsimulator.model.monitor;

import io.reactivex.Observable;

public abstract class MileageMonitor extends EngineMonitor {

    public abstract void closeAndSave();

    public abstract int getResettableCount();

    public abstract Observable<Mileage> getMileage();

    public abstract void resetMileage(int n);

}
