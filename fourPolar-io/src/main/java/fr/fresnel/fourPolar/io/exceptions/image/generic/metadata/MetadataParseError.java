package fr.fresnel.fourPolar.io.exceptions.image.generic.metadata;

/**
 * Exception thrown in case there's a problem in parsing metadata.
 */
public class MetadataParseError extends Exception {
    private static final long serialVersionUID = 5374378579477003124L;

    /**
     * This error message is printed in case the axis order defined in the metadata
     * does not match any of those defined in {@link AxisOrder}.
     */
    public static final String UNDEFINED_AXIS_ORDER = "Axis order can't be set.";

    public MetadataParseError(String message){
        super(message);
    }
}