package p.lodz.dashboardsimulator.base;


import io.reactivex.Scheduler;

/**
 * Super class for every presenter (MVP).
 * @param <T> Determines type of {@link View} that is bounded to presenter.
 */
public abstract class Presenter<T extends View<?>> {

    /**
     * Contains view bounded with this presenter.
     */
    protected T view;

    /**
     * Contains {@link Scheduler} that provides a hook to appropriate thread (or threads) to call user interface.
     */
    protected Scheduler currentScheduler;

    /**
     * Injects instance of {@link View} to the presenter. After this action, view is available for every subclass in {@link Presenter#view } field.
     * This method might be overridden but its version from super class must be called.
     * @param view Instance of {@link View} that is bounded to this presenter.
     */
    public void attach(T view) {
        this.view = view;
        this.currentScheduler = view.getViewScheduler();
    }

    /**
     * Detaches the view from the presenter. After this action {@link Presenter#view } is null.
     * This method might be overridden but its version from super class must be called.
     */
    public void detach() {
        this.view = null;
    }
}
