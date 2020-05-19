package p.lodz.dashboardsimulator.model.settings;

import p.lodz.dashboardsimulator.model.serialize.Serializer;
import p.lodz.dashboardsimulator.model.serialize.exceptions.DeserializationException;
import p.lodz.dashboardsimulator.model.serialize.exceptions.SerializationException;

import java.util.function.Consumer;

public class SettingsManager {

    private Serializer serializer;
    private Settings settings = new Settings();

    private final String serializationKey = "global";

    public SettingsManager(Serializer serializer) {
        this.serializer = serializer;
        loadSettings();
    }

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

    public Settings getSettings() {
        return settings;
    }

    public void edit(Consumer<Settings> edit) {
        edit.accept(settings);
    }

    public void saveSettings() {

        try {
            serializer.serialize(serializationKey, settings);
        } catch (SerializationException e) {
            System.out.println("Settings could not be saved!");
            e.printStackTrace();
        }
    }
}
