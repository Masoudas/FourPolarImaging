package fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.excel;

/**
 * Exception raised when the excel file does not have the title row.
 */
public class MissingExcelTitleRow extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Exception raised when the excel file does not have the title row.
     * @param message
     */
    public MissingExcelTitleRow(String message) {
        super(message);
    }

}