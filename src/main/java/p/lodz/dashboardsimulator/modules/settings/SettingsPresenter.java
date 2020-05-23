package p.lodz.dashboardsimulator.modules.settings;

import p.lodz.dashboardsimulator.base.Presenter;
import p.lodz.dashboardsimulator.base.View;
import p.lodz.dashboardsimulator.model.repositories.TravelDataRepository;
import p.lodz.dashboardsimulator.model.settings.Settings;
import p.lodz.dashboardsimulator.model.settings.SettingsManager;

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

    SettingsPresenter(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    @Override
    public void attach(SettingsView view) {
        super.attach(view);
    }

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

        try {
            TravelDataRepository.Type.valueOf(view.getDatabaseType());
        } catch (IllegalArgumentException e) {
            throw new ParseException("database type");
        }

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
