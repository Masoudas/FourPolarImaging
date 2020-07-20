package fr.fresnel.fourPolar.io.exceptions.image.generic.metadata;

/**
 * Exception thrown in case there's a problem in parsing metadata.
 */
public class MetadataIOIssues extends Exception {
    private static final long serialVersionUID = 5374378579477003124L;

    public static final String READ_ISSUES = "Metadata can't be read due to low-level IO Issues.";
    public static final String INCOMPLETE_METADATA = "Metadata instance has missing fileds.";
    public static final String Write_ISSUES = "Metadata can't be written due to low-level IO Issues.";

    public MetadataIOIssues(String message) {
        super(message);
    }
}