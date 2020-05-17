package p.lodz.dashboardsimulator.base;

import io.reactivex.Scheduler;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.scene.Scene;
import p.lodz.dashboardsimulator.base.Presenter;
import p.lodz.dashboardsimulator.base.View;

public abstract class JavaFxView<T extends Presenter<?>> implements View<T> {

    protected Scene scene;

    public void attachScene(Scene scene) {
        this.scene = scene;
    }

    @Override
    public Scheduler getViewScheduler() {
        return JavaFxScheduler.platform();
    }

    public abstract void notifyCloseEvent();
}
