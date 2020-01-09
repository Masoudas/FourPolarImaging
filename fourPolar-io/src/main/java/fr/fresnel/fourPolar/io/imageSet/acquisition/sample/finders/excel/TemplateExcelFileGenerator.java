package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.excel;

import fr.fresnel.fourPolar.core.imagingSetup.imageFormation.Cameras;

/**
 * Using this class, we can generate a template file, for defining the sample
 * set images.
 */
public class TemplateExcelFileGenerator {
    public TemplateExcelFileGenerator(Cameras camera){
        
    }
    
    private void createFile() {
        // Use path factory with the root!
    }
    
    private void writeComments() {
        
    } 

    private void writeTitleRow() {
        
    }

    /**
     * Returns the row in the excel files that defines the labels.
     * @return row number
     */
    public static int getTitleRow() {
        return 3;
    }
    
}