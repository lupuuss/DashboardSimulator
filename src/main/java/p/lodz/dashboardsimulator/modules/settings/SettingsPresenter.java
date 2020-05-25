package p.lodz.dashboardsimulator.modules.settings;

import p.lodz.dashboardsimulator.base.Presenter;
import p.lodz.dashboardsimulator.base.View;
import p.lodz.dashboardsimulator.model.settings.Settings;
import p.lodz.dashboardsimulator.model.settings.SettingsManager;

/**
 * Describes logic behind {@link SettingsView}. Communicates view with {@link }.
 */
public class SettingsPresenter extends Presenter<SettingsView> {

    static class ParseException extends Exception {
        ParseException(String fieldName) {
            super("Invalid value in " + fieldName + " field");
        }
    }

    static class EmptyFieldException extends Exception {
        EmptyFieldException() {
            super("All fields must be filled!");
        }
    }

    SettingsManager settingsManager;

    /**
     * Injects {@link SettingsManager} to the presenter.
     * @param settingsManager Instance of {@link SettingsManager} that will be used by the presenter.
     */
    SettingsPresenter(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }


    /**
     * Bounds view to the presenter. Shows current settings received from {@link SettingsManager}.
     * @param view Instance of {@link SettingsView} that is bounded to this presenter.
     */
    @Override
    public void attach(SettingsView view) {
        super.attach(view);

        Settings settings = settingsManager.getSettings();

        view.setAccelerationConst(String.valueOf(settings.getAccelerationConst()));
        view.setBetweenEngineTicks(String.valueOf(settings.getBetweenEngineTicks()));
        view.setDatabaseHost(settings.getDatabaseHost());
        view.setDatabasePassword(settings.getDatabasePassword());
        view.setDatabaseName(settings.getDatabaseName());
        view.setDatabaseType(settings.getDatabaseType());
        view.setDatabaseUser(settings.getDatabaseUser());
        view.setMaximumSpeed(String.valueOf(settings.getMaximumSpeed()));
    }

    /**
     * Saves current settings if valid. Otherwise, shows the error message to the user.
     */
    public void saveSettings() {

        Settings fromView = null;
        try {
            fromView = readAndValidateSettingsFromView();
        } catch (EmptyFieldException | ParseException e) {

            view.showMessage("Invalid settings cannot be saved! " + e.getMessage(), View.MessageType.ERROR);
        }

        settingsManager.setSettings(fromView);

        view.showMessage("Application restart required after settings change!", View.MessageType.WARNING);
    }

    private Settings readAndValidateSettingsFromView() throws ParseException, EmptyFieldException {
        Settings fromGui = new Settings();

        if (view.getAccelerationConst().matches("[+-]?([0-9]*[.])?[0-9]+")) {
            fromGui.setAccelerationConst(
                    Double.parseDouble(view.getAccelerationConst())
                    );
        } else {
            throw new ParseException("acceleration const");
        }

        if (view.getBetweenEngineTicks().matches("\\d+")) {
            fromGui.setBetweenEngineTicks(
                    Long.parseLong(view.getBetweenEngineTicks())
            );
        }

        if (view.getMaximumSpeed().matches("[+-]?([0-9]*[.])?[0-9]+")) {
            fromGui.setMaximumSpeed(
                    Double.parseDouble(view.getMaximumSpeed())
            );
        }

        fromGui.setDatabaseType(view.getDatabaseType());

        if (view.getDatabaseHost().isEmpty()
                || view.getDatabaseName().isEmpty()
                || view.getDatabasePassword().isEmpty()
                || view.getDatabaseUser().isEmpty()) {

            throw new EmptyFieldException();
        }

        fromGui.setDatabaseHost(view.getDatabaseHost());
        fromGui.setDatabaseName(view.getDatabaseName());
        fromGui.setDatabasePassword(view.getDatabasePassword());
        fromGui.setDatabaseUser(view.getDatabaseUser());

        return fromGui;
    }

    /**
     * Closes settings window if settings are saved. Otherwise, shows warning that settings might be lost.
     */
    public void closeIfSettingsSaved() {

        Settings fromView = null;

        try {
            fromView = readAndValidateSettingsFromView();
        } catch (EmptyFieldException | ParseException e) {
            // ignored
        }

        if (fromView == null || !fromView.equals(settingsManager.getSettings())) {

            view.askUser("Do you want to leave without saving?", answer -> {

                if (answer) {
                    view.close();
                }
            });

        } else {

            view.close();
        }
    }
}
