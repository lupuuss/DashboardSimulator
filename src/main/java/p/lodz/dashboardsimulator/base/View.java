package p.lodz.dashboardsimulator.base;

import io.reactivex.Scheduler;

/**
 * Super class for every view (MVP)
 * @param <T> Determines type of presenter bounded particular view.
 */
public interface View<T extends Presenter<?>> {

    enum MessageType {
        INFO, WARNING, ERROR
    }

    /**
     * Initializing method for view.
     * @param injector Implementation of injector that provides necessary components for presenter.
     */
    void start(Injector injector);

    /**
     * Closes the view.
     */
    void close();

    /**
     * Some implementations of user interface requires certain {@link Scheduler}
     * for callbacks e.g. JavaFx requires {@link io.reactivex.rxjavafx.schedulers.JavaFxScheduler}
     * @return {@link Scheduler} required for every method call on view.
     */
    Scheduler getViewScheduler();

    /**
     * Shows message.
     * @param message {@link String} that contains message to be shown.
     */
    void showMessage(String message, MessageType type);
}
