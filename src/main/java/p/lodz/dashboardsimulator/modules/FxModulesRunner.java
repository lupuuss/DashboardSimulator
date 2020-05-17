package p.lodz.dashboardsimulator.modules;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import p.lodz.dashboardsimulator.base.*;

import java.io.IOException;

public class FxModulesRunner {

    private Stage primaryStage;
    private Module primaryStageModule;
    private GlobalInjector globalInjector = new GlobalInjector();

    public FxModulesRunner(Stage primaryStage, Module primaryStageModule) {

        this.primaryStage = primaryStage;
        this.primaryStageModule = primaryStageModule;

        globalInjector.init(null);
    }

    public void runModule(Module module) throws IOException {

        FXMLLoader fxml = new FXMLLoader(getClass().getResource(module.getFxmlName()));
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

        Injector injector = module.getInjectorSupplier();

        injector.init(globalInjector);

        view.attachScene(stage.getScene());
        view.start(injector);

        stage.show();
        stage.setOnCloseRequest(event -> view.notifyCloseEvent());
    }

}
