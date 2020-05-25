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

/**
 * Implements {@link SettingsView}. Displays a text fields to change settings.
 * Button is placed in the end to save chosen settings.
 */

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

    /**
     * Initializes presenter using {@link GlobalInjector} passed as parameter.
     * @param injector Implementation of injector that provides necessary components for presenter.
     */

    @Override
    public void start(Injector injector) {
        GlobalInjector globalInjector = (GlobalInjector) injector;

        scene.getRoot().requestFocus();

        presenter = new SettingsPresenter(
                globalInjector.getSettingsManager()
        );

        presenter.attach(this);

    }

    /**
     * Notifes presenter about user intent to close the view.
     */
    @Override
    public void notifyCloseEvent() {
        presenter.closeIfSettingsSaved();
    }

    /**
     * Closes the view and detaches the presenter.
     */

    @Override
    public void close() {
        presenter.detach();
        super.close();
    }
    /**
     * Notifes presenter about user changes in settings.
     */
    @FXML
    void notifyPresenterSaveSettings() {
        presenter.saveSettings();
    }
    /**
     * @return Returns database login value passed by user as {@link String}
     * from textField with id loginValue
     */
    @Override
    public String getDatabaseUser() {
        return loginValue.getText();
    }

    /**
     * Set database user passed by user.
     */
    @Override
    public void setDatabaseUser(String databaseUser) {
        loginValue.setText(databaseUser);
    }

    /**
     * @return Returns database password value passed by user as {@link String}
     * from passwordField with id passwordValue
     */
    @Override
    public String getDatabasePassword() {
        return passwordValue.getText();
    }

    /**
     * Set passwordField with id = passwordValue, value with passed value.
     */
    @Override
    public void setDatabasePassword(String databasePassword) {
        passwordValue.setText(databasePassword);
    }

    /**
     * @return Returns database host value passed by user as {@link String}
     * from passwordField with id = hostValue
     */
    @Override
    public String getDatabaseHost() {
        return hostValue.getText();
    }

    /**
     * Set textField with id = hostValue value with passed value.
     */
    @Override
    public void setDatabaseHost(String databaseHost) {
        hostValue.setText(databaseHost);
    }

    /**
     * @return Returns database name value passed by user as {@link String}
     * from textField with id = baseNameValue
     */
    @Override
    public String getDatabaseName() {
        return baseNameValue.getText();
    }
    /**
     * Set textField with id = baseNameValue value with passed value.
     */
    @Override
    public void setDatabaseName(String databaseName) {
        baseNameValue.setText(databaseName);
    }

    /**
     * @return Returns ticks between engine value passed by user as {@link String}
     * from textField with id = baseNameValue
     */
    @Override
    public String getBetweenEngineTicks() {
        return ticksPerSecValue.getText();
    }

    /**
     * Set textField with id = ticksPerSecValue value with passed value.
     */
    @Override
    public void setBetweenEngineTicks(String betweenEngineTicks) {
        ticksPerSecValue.setText(betweenEngineTicks);
    }

    /**
     * @return Returns acceleration value passed by user as {@link String}
     * from textField with id = accValue
     */
    @Override
    public String getAccelerationConst() {
        return accValue.getText();
    }

    /**
     * Set textField with id = accValue value with passed value.
     */
    @Override
    public void setAccelerationConst(String accelerationConst) {
        accValue.setText(accelerationConst);
    }
    /**
     * @return Returns Maximum speed value passed by user as {@link String}
     * from textField with id = maxSpeedValue
     */
    @Override
    public String getMaximumSpeed() {
        return maxSpeedValue.getText();
    }
    /**
     * Set textField with id = maxSpeedValue value with passed value.
     */
    @Override
    public void setMaximumSpeed(String maximumSpeed) {
        maxSpeedValue.setText(maximumSpeed);
    }

    /**
     * @return Returns database type according to toggled option is Radio Menu
     */
    @Override
    public TravelDataRepository.Type getDatabaseType() {

        return TravelDataRepository.Type.valueOf(savingMethods.getSelectedToggle().getUserData().toString());
    }

    /**
     * Set toggled value of MenuButton accordingly to used saving method passed as argument.
     */
    @Override
    public void setDatabaseType(TravelDataRepository.Type databaseType) {
        savingMethods.
                getToggles()
                .filtered(toggle -> toggle.getUserData().toString().equals(databaseType.name()))
                .get(0)
                .setSelected(true);
    }
}
