package p.lodz.dashboardsimulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import p.lodz.dashboardsimulator.model.engine.Engine;
import p.lodz.dashboardsimulator.model.engine.EngineSimulator;
import p.lodz.dashboardsimulator.model.monitor.odometer.BasicOdometer;
import p.lodz.dashboardsimulator.model.monitor.odometer.Odometer;
import p.lodz.dashboardsimulator.model.monitor.statistics.BasicStatisticsMonitor;
import p.lodz.dashboardsimulator.model.monitor.statistics.StatisticsMonitor;
import p.lodz.dashboardsimulator.model.serialize.Serializer;
import p.lodz.dashboardsimulator.model.serialize.XmlSerializer;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardPresenter;
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

        Engine engine = new EngineSimulator(20, 300, 200);
        StatisticsMonitor statisticsMonitor = new BasicStatisticsMonitor();

        Serializer serializer = new XmlSerializer(".\\serializable\\");

        Odometer odometer = new BasicOdometer(serializer, 2);

        DashboardPresenter presenter = new DashboardPresenter(engine, statisticsMonitor, null, odometer);
        ConsoleCommandsReader reader = new ConsoleCommandsReader();

        DashboardView view = new DashboardConsoleView(reader.read());

        view.attach(presenter);
        presenter.attach(view);

        reader.await();

        presenter.detach();
        view.detach();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root;
        root = FXMLLoader.load(getClass().getResource("/dashboard.fxml"));
        primaryStage.setTitle("Car Dashboard");
        primaryStage.setScene(new Scene(root, 1280, 400));
        primaryStage.show();

    }
}
