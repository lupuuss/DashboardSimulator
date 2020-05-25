package p.lodz.dashboardsimulator.modules.settings;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
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
     * @param injector Expected instance of {@link GlobalInjector}.
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
     * Notifies presenter about user intent to close the view.
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
     * @return Returns database login value passed by user as {@link String}
     * from textField with id loginValue
     */
    @Override
    public String getDatabaseUser() {
        return loginValue.getText();
    }

    /**
     * Shows current value of database user in {@link TextField} with id = loginValue
     * @param databaseUser Database user name to be displayed.
     */
    @Override
    public void setDatabaseUser(String databaseUser) {
        loginValue.setText(databaseUser);
    }

    /**
     * Returns database password value passed by user as {@link String} from {@link PasswordField} with id =  passwordValue
     * @return Password to the database for the user specified by {@link SettingsGuiView#setDatabaseUser(String)}.
     */
    @Override
    public String getDatabasePassword() {
        return passwordValue.getText();
    }

    /**
     * Shows current value of user's password in {@link PasswordField} with id = passwordValue
     * @param databasePassword User's password to be shown.
     */
    @Override
    public void setDatabasePassword(String databasePassword) {
        passwordValue.setText(databasePassword);
    }

    /**
     * Returns database host value passed by user as {@link String} from {@link TextField} with id = hostValue.
     * @return Database host name specified by user.
     */
    @Override
    public String getDatabaseHost() {
        return hostValue.getText();
    }

    /**
     * Shows current value of database host in {@link TextField} with id = hostValue
     * @param databaseHost Database host name specified by user.
     */
    @Override
    public void setDatabaseHost(String databaseHost) {
        hostValue.setText(databaseHost);
    }

    /**
     * Returns database name value passed by user as {@link String} from {@link TextField} with id = baseNameValue
     * @return Database name specified by user.
     */
    @Override
    public String getDatabaseName() {
        return baseNameValue.getText();
    }

    /**
     * SHows database name in {@link TextField} with id = baseNameValue
     * @param databaseName Database name to be shown.
     */
    @Override
    public void setDatabaseName(String databaseName) {
        baseNameValue.setText(databaseName);
    }

    /**
     * Returns time between engine ticks value in ms passed by user (might be invalid/not a number) as {@link String} from textField with id = ticksPerSecValue
     * @return {@link String} that contains time between engine ticks in ms specified by the user (might be invalid/not a number).
     */
    @Override
    public String getBetweenEngineTicks() {
        return ticksPerSecValue.getText();
    }

    /**
     * Shows current time between engine ticks (in ms) in {@link TextField} with id = ticksPerSecValue.
     * @param betweenEngineTicks Time between engine ticks in ms to be shown.
     */
    @Override
    public void setBetweenEngineTicks(String betweenEngineTicks) {
        ticksPerSecValue.setText(betweenEngineTicks);
    }

    /**
     * Returns acceleration value in km/s^2 passed by user (might be invalid/not a number) as {@link String} from {@link TextField} with id = accValue.
     * @return {@link String} that contains acceleration value in km/s^2.
     */
    @Override
    public String getAccelerationConst() {
        return accValue.getText();
    }

    /**
     * Shows current acceleration value (in km/s^2) in {@link TextField} with id = accValue
     * @param accelerationConst Acceleration const in km/s^2 to be shown.
     */
    @Override
    public void setAccelerationConst(String accelerationConst) {
        accValue.setText(accelerationConst);
    }
    /**
     * Returns maximum speed value passed by user (might be invalid/not a number) as {@link String} from {@link TextField} with id = maxSpeedValue.
     * @return {@link String} that contains maximum speed in km/h specified by the user (might be invalid/not a number).
     */
    @Override
    public String getMaximumSpeed() {
        return maxSpeedValue.getText();
    }

    /**
     * Shows current maximum speed in {@link TextField} with id = maxSpeedValue.
     * @param maximumSpeed Maximum speed in km/h to be shown.
     */
    @Override
    public void setMaximumSpeed(String maximumSpeed) {
        maxSpeedValue.setText(maximumSpeed);
    }

    /**
     * Returns database type enum according to option selected by user in {@link ToggleGroup} with id = savingMethods
     * @return Enum describing database type specified by user.
     */
    @Override
    public TravelDataRepository.Type getDatabaseType() {

        return TravelDataRepository.Type.valueOf(savingMethods.getSelectedToggle().getUserData().toString());
    }

    /**
     * Shows current database type in {@link ToggleGroup} with id = savingMethods
     * @param databaseType Database type to be shown.
     */
    @Override
    public void setDatabaseType(TravelDataRepository.Type databaseType) {
        savingMethods.
                getToggles()
                .filtered(toggle -> toggle.getUserData().toString().equals(databaseType.name()))
                .get(0)
                .setSelected(true);
    }

    @FXML
    private void notifyPresenterSaveSettings() {
        presenter.saveSettings();
    }
}
