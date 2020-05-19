package p.lodz.dashboardsimulator.modules;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import p.lodz.dashboardsimulator.base.*;

import java.io.IOException;

/**
 * Initializes modules and shares model instances between them using injectors ({@link Injector}).
 */
public class FxModulesRunner {

    private Stage primaryStage;
    private Module primaryStageModule;
    private GlobalInjector globalInjector = new GlobalInjector();

    /**
     * Requires JavaFx primary {@link Stage} and module that is supposed to be run on this stage.
     * @param primaryStage Primary {@link Stage} from JavaFX.
     * @param primaryStageModule Module to be run on the primary {@link Stage}.
     */
    public FxModulesRunner(Stage primaryStage, Module primaryStageModule) {

        this.primaryStage = primaryStage;
        this.primaryStageModule = primaryStageModule;

        globalInjector.init(null);
    }

    /**
     * Runs passed module.
     * @param module Module to be run.
     * @throws IOException If error occurs during loading fxml.
     */
    public void runModule(Module module) throws IOException {

        FXMLLoader fxml = new FXMLLoader(getClass().getResource(module.getFxmlPath()));
        Parent root = fxml.load();

        JavaFxView<?> view = fxml.getController();

        Stage stage;

        if (primaryStageModule == module) {
            stage = primaryStage;
        } else {
            stage = new Stage();
        }

        stage.setTitle(module.getTitle());
        stage.setScene(new Scene(root));

        Injector injector;

        if (module.getInjectorSupplier() != null) {
            injector = module.getInjectorSupplier().get();
            injector.init(globalInjector);
        } else {
            injector = globalInjector;
        }

        view.attachFx(stage.getScene(), this);
        view.start(injector);

        stage.show();
        stage.setOnCloseRequest(event -> view.notifyCloseEvent());
    }

}
