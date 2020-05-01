package p.lodz.dashboardsimulator.model.serialize.exceptions;

public class DeserializationException extends Exception {

    public DeserializationException(Throwable cause) {
        super(cause);
    }

    public DeserializationException(String msg) {
        super(msg);
    }
}