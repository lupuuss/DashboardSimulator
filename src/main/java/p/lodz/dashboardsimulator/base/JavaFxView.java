package p.lodz.dashboardsimulator.base;

import io.reactivex.Scheduler;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;

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
