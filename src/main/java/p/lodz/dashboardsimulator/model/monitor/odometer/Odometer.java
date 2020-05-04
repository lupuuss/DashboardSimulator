package p.lodz.dashboardsimulator.model.monitor.odometer;

import io.reactivex.Observable;
import p.lodz.dashboardsimulator.model.monitor.EngineMonitor;

/**
 * Super class for every odometer. Also, it extends EngineMonitor so all data that is produced by any subclass
 * should base on {@link p.lodz.dashboardsimulator.model.engine.EngineState}. It should support total mileage
 * and resettable mileages.
 */
public abstract class Odometer extends EngineMonitor {

    /**
     * As Odometer is supposed to persist its data, this method should save all data and perform any other cleanup necessary.
     */
    public abstract void closeAndSave();

    /**
     * Returns an amount of resettable internal odometers.
     * @return An amount of resettable internal odometers.
     */
    public abstract int getResettableCount();

    /**
     * Returns observable object that propagate any change in mileage.
     * @return Instance of {@link Observable} that watch {@link Mileage}
     */
    public abstract Observable<Mileage> getMileage();

    /**
     * Sets state of chosen odometer to 0 km.
     * @param n Number of resettable odometer that we want to reset.
     */
    public abstract void resetMileage(int n);

}
