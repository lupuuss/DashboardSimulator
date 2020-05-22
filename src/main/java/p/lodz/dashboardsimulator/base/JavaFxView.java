package p.lodz.dashboardsimulator.base;

import io.reactivex.Scheduler;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import p.lodz.dashboardsimulator.modules.FxModulesRunner;

import java.util.Optional;
import java.util.function.Consumer;

import static javafx.scene.control.ButtonType.OK;

/**
 * Provides useful methods for views that are implemented using JavaFX.
 * @param <T> Determines type of presenter associated with this view.
 */
public abstract class JavaFxView<T extends Presenter<?>> implements View<T> {

    protected Scene scene;
    protected FxModulesRunner runner;

    /**
     * Injects associated {@link Scene} and {@link FxModulesRunner}
     * @param scene {@link Scene} associated with this view.
     * @param runner {@link FxModulesRunner} that created this view. It allows to run other modules.
     */
    public void attachFx(Scene scene, FxModulesRunner runner) {
        this.scene = scene;
        this.runner = runner;
    }

    /**
     * Returns instance of {@link JavaFxScheduler}.
     * @return Instance of {@link JavaFxScheduler}
     */
    @Override
    public Scheduler getViewScheduler() {
        return JavaFxScheduler.platform();
    }

    /**
     * Notifies view about close event.
     */
    public abstract void notifyCloseEvent();

    /**
     * Displays a message using {@link Alert}.
     * @param message {@link String} that contains message to be shown.
     * @param type Type of message described with {@link p.lodz.dashboardsimulator.base.View.MessageType}
     */
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

    @Override
    public void askUser(String message, Consumer<Boolean> onUserDecision) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setContentText(message);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.isPresent() && buttonType.get() == OK) {
            onUserDecision.accept(true);
        } else {
            onUserDecision.accept(false);
        }
    }

    @Override
    public void close() {
        ((Stage) scene.getWindow()).close();
    }
}
