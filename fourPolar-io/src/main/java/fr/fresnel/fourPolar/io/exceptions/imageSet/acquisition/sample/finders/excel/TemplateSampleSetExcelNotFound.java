package fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.excel;

/**
 * The exception thrown when the template excel file asked from the user is not found.
 */
public class TemplateSampleSetExcelNotFound extends Exception {
    private static final long serialVersionUID = 4076553896674038855L;

    public TemplateSampleSetExcelNotFound(String message) {
        super(message);
    }
}