package fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.excel;

/**
 * Exception raised when the excel file provided by the user does not have the
 * correct format.
 */
public class IncorrectSampleSetTemplateExcelFormat extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Exception raised when the excel file provided by the user does not have the
     * correct format.
     * @param message
     */
    public IncorrectSampleSetTemplateExcelFormat(String message) {
        super(message);
    }

}