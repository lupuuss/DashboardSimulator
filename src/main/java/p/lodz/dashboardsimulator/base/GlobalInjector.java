package p.lodz.dashboardsimulator.base;

import p.lodz.dashboardsimulator.model.repositories.JdbcTravelDataRepository;
import p.lodz.dashboardsimulator.model.repositories.TravelDataRepository;
import p.lodz.dashboardsimulator.model.serialize.Serializer;
import p.lodz.dashboardsimulator.model.serialize.XmlSerializer;

public class GlobalInjector implements Injector {

    private Serializer serializer;
    private TravelDataRepository repository;

    @Override
    public void init(Injector parentInjector) {
        serializer = new XmlSerializer(".\\serializable\\");
        repository = new JdbcTravelDataRepository();
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public TravelDataRepository getTravelDataRepository() {
        return repository;
    }
}
