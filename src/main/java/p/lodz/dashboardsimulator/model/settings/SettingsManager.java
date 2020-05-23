package p.lodz.dashboardsimulator.model.settings;

import p.lodz.dashboardsimulator.model.serialize.Serializer;
import p.lodz.dashboardsimulator.model.serialize.exceptions.DeserializationException;
import p.lodz.dashboardsimulator.model.serialize.exceptions.SerializationException;

import java.util.function.Consumer;

/**
 * Manages current application settings and persists state using serializer.
 */
public class SettingsManager {

    private Serializer serializer;
    private Settings settings = new Settings();

    private final String serializationKey = "global";

    /**
     * Injects serializer and loads current settings from serializer.
     * @param serializer Serializer that is used to persisting settings.
     */
    public SettingsManager(Serializer serializer) {
        this.serializer = serializer;
        loadSettings();
    }

    /**
     * Loads settings from serializer. Should be called once at the start of the app.
     */
    public final void loadSettings() {

        try {


            Settings tmpSettings = serializer.deserialize(serializationKey, Settings.class);

            if (tmpSettings != null) {
                settings = tmpSettings;
            }

        } catch (DeserializationException e) {
            System.out.println("Settings could not be loaded!");
            e.printStackTrace();

            saveSettings();
        }
    }

    /**
     * Returns current settings
     * @return Instance of {@link Settings} that contains current settings.
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * Replace current settings object with given reference.
     * Also, settings state is persisted.
     * @param settings Settings to set.
     */
    public void setSettings(Settings settings) {
        this.settings = settings;
        saveSettings();
    }

    /**
     * Changes current settings using given lambda.
     * Also, settings state is persisted.
     * @param edit Lambda that edits current settings.
     */
    public void edit(Consumer<Settings> edit) {
        edit.accept(settings);
        saveSettings();
    }

    private void saveSettings() {

        try {
            serializer.serialize(serializationKey, settings);
        } catch (SerializationException e) {
            System.out.println("Settings could not be saved!");
            e.printStackTrace();
        }
    }
}
