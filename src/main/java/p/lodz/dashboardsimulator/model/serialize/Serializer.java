package p.lodz.dashboardsimulator.model.serialize;

import p.lodz.dashboardsimulator.model.serialize.exceptions.DeserializationException;
import p.lodz.dashboardsimulator.model.serialize.exceptions.SerializationException;

public interface Serializer {

    void serialize(String key, Object object) throws SerializationException;

    <T> T deserialize(String key, Class<T> classObject) throws DeserializationException;
}
