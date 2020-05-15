package fr.fresnel.fourPolar.io.exceptions.image.generic.metadata;

/**
 * Exception thrown in case there's a problem in parsing metadata.
 */
public class MetadataParseError extends Exception {
    private static final long serialVersionUID = 5374378579477003124L;

    /**
     * This error message is printed in case an axis in metadata does not correspond
     * to x, y, z, time or channel.
     */
    public static final String UNDEFINED_AXIS = "Undefined axis label.";

    /**
     * This error message is printed in case all axis in the image are defined, but
     * no proper axis order can be deduced from {@link AxisOrder} (including
     * NoOrder).
     */
    public static final String UNDEFINED_AXIS_ORDER = "Axis order can't be set.";

    public MetadataParseError(String message) {
        super(message);
    }
}