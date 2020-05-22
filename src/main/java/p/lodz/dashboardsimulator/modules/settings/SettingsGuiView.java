package p.lodz.dashboardsimulator.modules.settings;

import javafx.fxml.FXML;
import p.lodz.dashboardsimulator.base.GlobalInjector;
import p.lodz.dashboardsimulator.base.Injector;
import p.lodz.dashboardsimulator.base.JavaFxView;


public class SettingsGuiView extends JavaFxView<SettingsPresenter> implements SettingsView {

    SettingsPresenter presenter;

    @Override
    public void start(Injector injector) {
        GlobalInjector globalInjector = (GlobalInjector) injector;

        presenter = new SettingsPresenter(
                globalInjector.getSettingsManager()
        );

        presenter.attach(this);

    }

    @Override
    public void notifyCloseEvent() {
        presenter.closeIfSettingsSaved();
    }

    @Override
    public void close() {
        presenter.detach();
        super.close();
    }

    @FXML
    void notifyPresenterSaveSettings() {
        presenter.saveSettings();
    }

    @Override
    public String getDatabaseUser() {
        return null;
    }

    @Override
    public void setDatabaseUser(String databaseUser) {

    }

    @Override
    public String getDatabasePassword() {
        return null;
    }

    @Override
    public void setDatabasePassword(String databasePassword) {

    }

    @Override
    public String getDatabaseHost() {
        return null;
    }

    @Override
    public void setDatabaseHost(String databaseHost) {

    }

    @Override
    public String getDatabaseName() {
        return null;
    }

    @Override
    public void setDatabaseName(String databaseName) {

    }

    @Override
    public String getBetweenEngineTicks() {
        return null;
    }

    @Override
    public void setBetweenEngineTicks(String betweenEngineTicks) {

    }

    @Override
    public String getAccelerationConst() {
        return null;
    }

    @Override
    public void setAccelerationConst(String accelerationConst) {

    }

    @Override
    public String getMaximumSpeed() {
        return null;
    }

    @Override
    public void setMaximumSpeed(String maximumSpeed) {

    }

    @Override
    public String getDatabaseType() {
        return null;
    }

    @Override
    public void setDatabaseType(String databaseType) {

    }
}
