package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.Test;


public class FilterPolarizationFileTest {

    @Test    
    public void list_FilterPol90_ReturnsImg1_C1_Pol90() {
        // Start with the Pol0 file
        File root = new File(FilterCapturedImagesTest.class.getResource("FourCamera").getPath());
        File pol0File = new File(root, "Img1_C1_Pol0.tif");

        FilterPolarizationFile filter = new FilterPolarizationFile(pol0File, "Pol0", "Pol90");
        
        assertTrue(root.list(filter)[0].equals("Img1_C1_Pol90.tif"));


    }
}