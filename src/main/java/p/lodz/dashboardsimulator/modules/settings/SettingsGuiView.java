package p.lodz.dashboardsimulator.modules.settings;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import p.lodz.dashboardsimulator.base.GlobalInjector;
import p.lodz.dashboardsimulator.base.Injector;
import p.lodz.dashboardsimulator.base.JavaFxView;
import p.lodz.dashboardsimulator.model.repositories.TravelDataRepository;


public class SettingsGuiView extends JavaFxView<SettingsPresenter> implements SettingsView {

    @FXML private TextField loginValue;
    @FXML private PasswordField passwordValue;
    @FXML  private TextField hostValue;
    @FXML private TextField baseNameValue;
    @FXML  private TextField ticksPerSecValue;
    @FXML private TextField accValue;
    @FXML private TextField maxSpeedValue;
    @FXML private ToggleGroup savingMethods;

    private SettingsPresenter presenter;

    @Override
    public void start(Injector injector) {
        GlobalInjector globalInjector = (GlobalInjector) injector;

        scene.getRoot().requestFocus();

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
        return loginValue.getText();
    }

    @Override
    public void setDatabaseUser(String databaseUser) {
        loginValue.setText(databaseUser);
    }

    @Override
    public String getDatabasePassword() {
        return passwordValue.getText();
    }

    @Override
    public void setDatabasePassword(String databasePassword) {
        passwordValue.setText(databasePassword);
    }

    @Override
    public String getDatabaseHost() {
        return hostValue.getText();
    }

    @Override
    public void setDatabaseHost(String databaseHost) {
        hostValue.setText(databaseHost);
    }

    @Override
    public String getDatabaseName() {
        return baseNameValue.getText();
    }

    @Override
    public void setDatabaseName(String databaseName) {
        baseNameValue.setText(databaseName);
    }

    @Override
    public String getBetweenEngineTicks() {
        return ticksPerSecValue.getText();
    }

    @Override
    public void setBetweenEngineTicks(String betweenEngineTicks) {
        ticksPerSecValue.setText(betweenEngineTicks);
    }

    @Override
    public String getAccelerationConst() {
        return accValue.getText();
    }

    @Override
    public void setAccelerationConst(String accelerationConst) {
        accValue.setText(accelerationConst);
    }

    @Override
    public String getMaximumSpeed() {
        return maxSpeedValue.getText();
    }

    @Override
    public void setMaximumSpeed(String maximumSpeed) {
        maxSpeedValue.setText(maximumSpeed);
    }

    @Override
    public TravelDataRepository.Type getDatabaseType() {

        return TravelDataRepository.Type.valueOf(savingMethods.getSelectedToggle().getUserData().toString());
    }

    @Override
    public void setDatabaseType(TravelDataRepository.Type databaseType) {
        savingMethods.
                getToggles()
                .filtered(toggle -> toggle.getUserData().toString().equals(databaseType.name()))
                .get(0)
                .setSelected(true);
    }
}
