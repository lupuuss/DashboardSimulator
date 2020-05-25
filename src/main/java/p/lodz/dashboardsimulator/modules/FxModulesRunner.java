package p.lodz.dashboardsimulator.modules;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import p.lodz.dashboardsimulator.base.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Initializes modules and shares model instances between them using injectors ({@link Injector}).
 */
public class FxModulesRunner {

    private Stage primaryStage;
    private FxModule primaryStageFxModule;
    private GlobalInjector globalInjector = new GlobalInjector();

    private Map<FxModule, Stage> singleModules = new HashMap<>();

    /**
     * Requires JavaFx primary {@link Stage} and module that is supposed to be run on this stage.
     * @param primaryStage Primary {@link Stage} from JavaFX.
     * @param primaryStageFxModule Module to be run on the primary {@link Stage}.
     */
    public FxModulesRunner(Stage primaryStage, FxModule primaryStageFxModule) {

        this.primaryStage = primaryStage;
        this.primaryStageFxModule = primaryStageFxModule;

        globalInjector.init(null);
    }

    /**
     * Runs passed module.
     * @param fxModule Module to be run.
     * @throws IOException If error occurs during loading fxml.
     */
    public void runModule(FxModule fxModule) throws IOException {

        if (fxModule.isSingle() && singleModules.containsKey(fxModule)) {

            Stage stage = singleModules.get(fxModule);

            stage.setIconified(false);
            stage.getScene().getWindow().requestFocus();
            return;
        }


        FXMLLoader fxml = new FXMLLoader(getClass().getResource(fxModule.getFxmlPath()));
        Parent root = fxml.load();

        JavaFxView<?> view = fxml.getController();

        Stage stage;

        if (primaryStageFxModule == fxModule) {
            stage = primaryStage;
        } else {
            stage = new Stage();
        }

        stage.setTitle(fxModule.getTitle());
        stage.setScene(new Scene(root));

        Injector injector;

        if (fxModule.getInjectorSupplier() != null) {
            injector = fxModule.getInjectorSupplier().get();
            injector.init(globalInjector);
        } else {
            injector = globalInjector;
        }

        view.attachFx(stage.getScene(), this);
        view.start(injector);

        stage.show();
        stage.setOnCloseRequest(event -> {

            if (fxModule.isSingle()) {
                singleModules.remove(fxModule);
            }

            view.notifyCloseEvent();
            event.consume();
        });

        if (fxModule.isSingle()) {
            singleModules.put(fxModule, stage);
        }
    }

}
