package fr.fresnel.fourPolar.io.exceptions.image.vector;

import java.io.IOException;

/**
 * An exception that is thrown in case of IO issues when reading or writing
 * vector images.
 */
public class VectorImageIOIssues extends IOException {
    private static final long serialVersionUID = -43278974123234L;

    public VectorImageIOIssues(String message) {
        super(message);
    }
}