package p.lodz.dashboardsimulator.modules;

import p.lodz.dashboardsimulator.base.Injector;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardInjector;

import java.util.function.Supplier;

/**
 * Enumerates JavaFx modules available in a program.
 */
public enum Module {

    DASHBOARD(
            DashboardInjector::new,
            "/dashboard.fxml",
            "Dashboard"
    ),
    HISTORY(
            null,
            "/history.fxml",
            "Historia statystyk"
    ),
    SETTINGS(
            null,
            "/settings.fxml",
            "Ustawienia"
    );

    private Supplier<Injector> injectorSupplier;
    private String fxmlPath;
    private String title;

    /**
     * Requires {@link java.util.function.Supplier} that creates {@link Injector} associated with this module, name
     * of view's fxml and title of a view. If supplier is null, {@link p.lodz.dashboardsimulator.base.GlobalInjector}
     * will be used in {@link FxModulesRunner}
     * @param injectorSupplier {@link java.util.function.Supplier} that creates required {@link Injector}
     * @param fxmlPath Path to fxml file that contains JavaFx view.
     * @param title {@link String} that will be displayed as a window title.
     */
    Module(Supplier<Injector> injectorSupplier, String fxmlPath, String title) {
        this.injectorSupplier = injectorSupplier;
        this.fxmlPath = fxmlPath;
        this.title = title;
    }

    /**
     * Returns {@link Supplier} that provides {@link Injector} for this module.
     * @return {@link Supplier} that provides {@link Injector} for this module.
     */
    public Supplier<Injector> getInjectorSupplier() {
        return injectorSupplier;
    }

    /**
     * Returns path to fxml file that contains JavaFx view.
     * @return Path to fxml file that contains JavaFx view.
     */
    public String getFxmlPath() {
        return fxmlPath;
    }

    /**
     * Returns {@link String} that will be displayed as a window title.
     * @return {@link String} that will be displayed as a window title.
     */
    public String getTitle() {
        return title;
    }
}
