package p.lodz.dashboardsimulator.modules;

import p.lodz.dashboardsimulator.base.Injector;
import p.lodz.dashboardsimulator.modules.dashboard.DashboardInjector;

import java.util.function.Supplier;

public enum Module {

    DASHBOARD(
            DashboardInjector::new,
            "/dashboard.fxml",
            "Dashboard"
    ),
    HISTORY(
            null,
            "/history.fxml",
            "Statistics history"
    );

    private Supplier<Injector> injectorSupplier;
    private String fxmlName;
    private String title;

    Module(Supplier<Injector> injectorSupplier, String fxmlName, String title) {
        this.injectorSupplier = injectorSupplier;
        this.fxmlName = fxmlName;
        this.title = title;
    }

    public Injector getInjectorSupplier() {
        return injectorSupplier.get();
    }

    public String getFxmlName() {
        return fxmlName;
    }

    public String getTitle() {
        return title;
    }
}
