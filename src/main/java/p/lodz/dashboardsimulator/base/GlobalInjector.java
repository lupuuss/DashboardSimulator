package p.lodz.dashboardsimulator.base;

import p.lodz.dashboardsimulator.model.repositories.JdbcTravelDataRepository;
import p.lodz.dashboardsimulator.model.repositories.TravelDataRepository;
import p.lodz.dashboardsimulator.model.serialize.Serializer;
import p.lodz.dashboardsimulator.model.serialize.XmlSerializer;

/**
 * Contains instances shared between other injectors
 */
public class GlobalInjector implements Injector {

    private Serializer serializer;
    private TravelDataRepository travelDataRepository;

    /**
     * Creates shared instances.
     * @param parentInjector Parameter is ignored/expected to be null.
     */
    @Override
    public void init(Injector parentInjector) {
        serializer = new XmlSerializer(".\\serializable\\");
        travelDataRepository = new JdbcTravelDataRepository();
    }

    /**
     * Returns xml implementation of serializer.
     * @return Instance of {@link XmlSerializer}
     */
    public Serializer getSerializer() {
        return serializer;
    }

    /**
     * Returns TravelDataRepository implemented in JDBC using MS SQL
     * @return Instance of {@link JdbcTravelDataRepository}
     */
    public TravelDataRepository getTravelDataRepository() {
        return travelDataRepository;
    }
}
