package fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample;

/**
 * The exception thrown when the template excel file asked from the user is not found.
 */
public class SampleSetExcelNotFound extends Exception {
    private static final long serialVersionUID = 4076553896674033855L;

    public SampleSetExcelNotFound(String message) {
        super(message);
    }
}