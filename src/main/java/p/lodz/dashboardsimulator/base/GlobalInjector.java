package p.lodz.dashboardsimulator.base;

import p.lodz.dashboardsimulator.model.repositories.JdbcTravelDataRepository;
import p.lodz.dashboardsimulator.model.repositories.TravelDataRepository;
import p.lodz.dashboardsimulator.model.repositories.SerializedTravelDataRepository;
import p.lodz.dashboardsimulator.model.serialize.Serializer;
import p.lodz.dashboardsimulator.model.serialize.XmlSerializer;
import p.lodz.dashboardsimulator.model.settings.Settings;
import p.lodz.dashboardsimulator.model.settings.SettingsManager;

/**
 * Contains instances shared between other injectors
 */
public class GlobalInjector implements Injector {

    private Serializer serializer;
    private TravelDataRepository travelDataRepository;
    private SettingsManager settingsManager;

    /**
     * Creates shared instances.
     * @param parentInjector Parameter is ignored/expected to be null.
     */
    @Override
    public void init(Injector parentInjector) {
        serializer = new XmlSerializer(".\\serializable\\");
        settingsManager = new SettingsManager(serializer);

        Settings settings = settingsManager.getSettings();

        switch (settings.getDatabaseType()) {

            case JDBC:
                travelDataRepository = new JdbcTravelDataRepository(
                        settings.getDatabaseUser(),
                        settings.getDatabasePassword(),
                        settings.getDatabaseHost(),
                        settings.getDatabaseName()
                );
                break;
            case SERIALIZED:
                travelDataRepository = new SerializedTravelDataRepository(serializer);
                break;
        }
    }

    /**
     * Returns xml implementation of {@link Serializer}.
     * @return Instance of {@link XmlSerializer}
     */
    public Serializer getSerializer() {
        return serializer;
    }

    /**
     * Returns {@link TravelDataRepository} implemented in JDBC using MS SQL
     * @return Instance of {@link JdbcTravelDataRepository}
     */
    public TravelDataRepository getTravelDataRepository() {
        return travelDataRepository;
    }

    /**
     * Returns {@link SettingsManager}
     * @return {@link SettingsManager}
     */
    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}
