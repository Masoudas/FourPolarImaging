package fr.fresnel.fourPolar.core.imageSet.acquisition.sample.finders;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class FilterPolarizationFileTest {

    @Test    
    public void list_FilterPol90_ReturnsImg1_C1_Pol90() {
        // Start with the Pol0 file
        File root = new File("/home/masoud/Documents/four-polar/fourPolar-core/src/test/java/fr/fresnel/fourPolar/core/imageSet/acquisition/sample/finders/TestFiles/FourCamera/");
        File pol0File = new File(root, "Img1_C1_Pol0.tiff");

        FilterPolarizationFile filter = new FilterPolarizationFile(pol0File, "Pol0", "Pol90");
        
        assertTrue(root.list(filter)[0].equals("Img1_C1_Pol90.tiff"));


    }
}