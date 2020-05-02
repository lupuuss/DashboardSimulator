package p.lodz.dashboardsimulator.base;

import io.reactivex.Scheduler;

public abstract class View<T extends Presenter<?>> {

    protected T presenter;


    public void attach(T presenter) {
        this.presenter = presenter;
    }

    public void detach() {
        presenter = null;
    }

    public abstract void close();

    public abstract Scheduler getViewScheduler();
}
