package p.lodz.dashboardsimulator.model.serialize.exceptions;

/**
 * Every exception that occurs during serialization should extends this class.
 * Other possibility is set occurred exception as cause of new SerializationException.
 */
public class SerializationException extends Exception {

    /**
     * @param cause of occurred exception.
     */
    public SerializationException(Throwable cause) {
        super(cause);
    }

    /**
     * @param msg describing occurred exception.
     */
    public SerializationException(String msg) {
        super(msg);
    }
}
