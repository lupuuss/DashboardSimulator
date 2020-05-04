package p.lodz.dashboardsimulator.model.serialize.exceptions;

/**
 * Occurs when anything goes wrong while {@link p.lodz.dashboardsimulator.model.serialize.XmlSerializer} initialization.
 */
public class XmlSerializerInitException extends RuntimeException {

    public XmlSerializerInitException(Throwable cause) {
        super(cause);
    }
}
