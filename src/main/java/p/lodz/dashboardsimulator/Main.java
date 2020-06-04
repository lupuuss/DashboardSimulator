package p.lodz.dashboardsimulator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import p.lodz.dashboardsimulator.base.GlobalInjector;
import p.lodz.dashboardsimulator.modules.FxModulesRunner;
import p.lodz.dashboardsimulator.modules.FxModule;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardInjector;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardView;
import p.lodz.dashboardsimulator.modules.dashboard.console.ConsoleCommandsReader;
import p.lodz.dashboardsimulator.modules.dashboard.console.DashboardConsoleView;

import java.io.IOException;

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

        Platform.exit();
    }

    @Override
    public void start(Stage primaryStage) {

        FxModulesRunner initializer = new FxModulesRunner(primaryStage, FxModule.DASHBOARD);

        try {
            initializer.runModule(FxModule.DASHBOARD);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
