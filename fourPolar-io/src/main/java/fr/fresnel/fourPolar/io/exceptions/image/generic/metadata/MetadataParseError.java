package fr.fresnel.fourPolar.io.exceptions.image.generic.metadata;

/**
 * Exception thrown in case there's a problem in parsing metadata.
 */
public class MetadataParseError extends Exception {
    private static final long serialVersionUID = 5374378579477003124L;

    public static final String IO_ISSUES = "Metadata can't be read due to low-level IO Issues.";

    public MetadataParseError(String message) {
        super(message);
    }
}