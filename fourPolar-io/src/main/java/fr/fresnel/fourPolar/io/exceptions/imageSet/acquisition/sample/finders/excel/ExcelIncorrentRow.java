package fr.fresnel.fourPolar.io.exceptions.imageSet.acquisition.sample.finders.excel;

/**
 * Exception thrown when there's a row in the excel file that does not match the number of cameras
 */
public class ExcelIncorrentRow extends Exception{
    private static final long serialVersionUID = 2L;

    public ExcelIncorrentRow(String message) {
        super(message);
    }
    
}