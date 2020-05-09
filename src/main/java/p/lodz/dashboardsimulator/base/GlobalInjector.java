package p.lodz.dashboardsimulator.base;

import p.lodz.dashboardsimulator.model.serialize.Serializer;
import p.lodz.dashboardsimulator.model.serialize.XmlSerializer;

public class GlobalInjector implements Injector {

    Serializer serializer;

    @Override
    public void init(Injector parentInjector) {
        serializer = new XmlSerializer(".\\serializable\\");
    }

    public Serializer getSerializer() {
        return serializer;
    }
}
