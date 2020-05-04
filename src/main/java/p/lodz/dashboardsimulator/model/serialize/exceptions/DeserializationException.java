package p.lodz.dashboardsimulator.model.serialize.exceptions;

/**
 * Every exception that occurs during deserialization should extends this class.
 * Other possibility is set occurred exception as cause of new DeserializationException.
 */
public class DeserializationException extends Exception {

    /**
     * @param cause Cause of occurred exception.
     */
    public DeserializationException(Throwable cause) {
        super(cause);
    }

    /**
     * @param msg Describes occurred exception.
     */
    public DeserializationException(String msg) {
        super(msg);
    }
}