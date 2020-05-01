package p.lodz.dashboardsimulator.model.serialize.exceptions;

public class SerializationException extends Exception {

    public SerializationException(Throwable cause) {
        super(cause);
    }

    public SerializationException(String msg) {
        super(msg);
    }
}
