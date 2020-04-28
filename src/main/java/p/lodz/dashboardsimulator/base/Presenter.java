package p.lodz.dashboardsimulator.base;


import io.reactivex.Scheduler;

public abstract class Presenter<T extends View> {

    protected T view;
    protected Scheduler currentScheduler;

    public void attach(T view) {
        this.view = view;
        this.currentScheduler = view.getViewScheduler();
    }

    public void detach() {
        this.view = null;
    }
}
