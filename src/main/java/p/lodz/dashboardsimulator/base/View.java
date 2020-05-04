package p.lodz.dashboardsimulator.base;

import io.reactivex.Scheduler;

/**
 * Super class for every view (MVP)
 * @param <T> Determines type of presenter bounded particular view.
 */
public abstract class View<T extends Presenter<?>> {

    protected T presenter;

    /**
     * Injects instance of {@link Presenter} to the view. After this action, view is available for every subclass in {@link View#presenter } field.
     * This method might be overridden but its version from super class must be called.
     * @param presenter Instance of {@link Presenter} that is bounded to this view.
     */
    public void attach(T presenter) {
        this.presenter = presenter;
    }

    /**
     * Detaches the presenter from the view. After this action {@link View#presenter } is null.
     * This method might be overridden but its version from super class must be called.
     */
    public void detach() {
        presenter = null;
    }

    /**
     * Closes the view.
     */
    public abstract void close();

    /**
     * Some implementations of user interface requires certain {@link Scheduler}
     * for callbacks e.g. JavaFx requires {@link io.reactivex.rxjavafx.schedulers.JavaFxScheduler}
     * @return {@link Scheduler} required for every method call on view.
     */
    public abstract Scheduler getViewScheduler();
}
