package p.lodz.dashboardsimulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import p.lodz.dashboardsimulator.base.GlobalInjector;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardGuiView;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardInjector;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardView;
import p.lodz.dashboardsimulator.modules.dashboard.console.ConsoleCommandsReader;
import p.lodz.dashboardsimulator.modules.dashboard.console.DashboardConsoleView;

public class Main extends Application {

    public static void main(String[] args) {

        if (args.length == 0 || args[0].equals("--gui")) {

            launch(args);

        } else if (args[0].equals("--cli")) {

            launchCli(args);

        }

    }

    private static void launchCli(String[] args) {

        ConsoleCommandsReader reader = new ConsoleCommandsReader();
        DashboardView view = new DashboardConsoleView(reader.read());

        GlobalInjector globalInjector = new GlobalInjector();
        DashboardInjector dashboardInjector = new DashboardInjector();

        globalInjector.init(null);
        dashboardInjector.init(globalInjector);

        view.start(dashboardInjector);

        reader.await();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        GlobalInjector globalInjector = new GlobalInjector();
        DashboardInjector dashboardInjector = new DashboardInjector();

        globalInjector.init(null);
        dashboardInjector.init(globalInjector);

        FXMLLoader fxml = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
        Parent root = fxml.load();

        DashboardGuiView view = fxml.getController();

        view.start(dashboardInjector);

        primaryStage.setTitle("Car Dashboard");
        primaryStage.setScene(new Scene(root, 1280, 400));

        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> view.notifyPresenterCloseEvent());
    }
}
