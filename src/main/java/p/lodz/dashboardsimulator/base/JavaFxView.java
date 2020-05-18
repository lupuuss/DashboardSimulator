package p.lodz.dashboardsimulator.base;

import io.reactivex.Scheduler;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import p.lodz.dashboardsimulator.modules.FxModulesRunner;

public abstract class JavaFxView<T extends Presenter<?>> implements View<T> {

    protected Scene scene;
    protected FxModulesRunner runner;

    public void attachFx(Scene scene, FxModulesRunner runner) {
        this.scene = scene;
        this.runner = runner;
    }

    @Override
    public Scheduler getViewScheduler() {
        return JavaFxScheduler.platform();
    }

    public abstract void notifyCloseEvent();

    @Override
    public void showMessage(String message, MessageType type) {
        Alert.AlertType alertType = Alert.AlertType.NONE;

        switch (type) {

            case INFO:
                alertType = Alert.AlertType.INFORMATION;
                break;
            case WARNING:
                alertType = Alert.AlertType.WARNING;
                break;
            case ERROR:
                alertType = Alert.AlertType.ERROR;
                break;
        }

        Alert alert = new Alert(alertType, message);

        alert.show();
    }
}
