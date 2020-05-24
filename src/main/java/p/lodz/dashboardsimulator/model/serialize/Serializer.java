package p.lodz.dashboardsimulator.model.serialize;

import p.lodz.dashboardsimulator.model.serialize.exceptions.DeserializationException;
import p.lodz.dashboardsimulator.model.serialize.exceptions.SerializationException;

/**
 * Describes interface for any serializer. We consider serializer that doesn't bother its user with files.
 * Requires only key that is unique across classes with the same name.
 */
public interface Serializer {

    /**
     * @param key Unique key that identifies object (more about uniqueness in interface description).
     * @param object Object to serialization.
     * @throws SerializationException if exception occurs during serialization.
     */
    void serialize(String key, Object object) throws SerializationException;

    /**
     * @param key Unique key that identifies the object (more about uniqueness in interface description).
     * @param classObject Class of serialized object.
     * @param <T> Describes a type of serialized object. It will be inferred from the {@link Class} object.
     * @return Deserialized object of a given class.
     * @throws DeserializationException if exception occurs during deserialization.
     */
    <T> T deserialize(String key, Class<T> classObject) throws DeserializationException;

    /**
     * Removes serialized object from the storage.
     * @param key Key of the serialized object to be deleted.
     * @throws DeserializationException If object doesn't exit.
     */
    <T> void removeSerialization(String key, Class<T> classObject) throws DeserializationException;
}
