package fr.fresnel.fourPolar.core.imageSet.acquisition.sample.finders.namePattern;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.io.*;

public class FilterCapturedImagesTest {
    File root = new File("fourPolar-core/src/test/java/fr/fresnel/fourPolar/core/imageSet/acquisition/sample/finders/TestFiles/");

    @Test
    public void testFilter_OneChannelOneCamerCase_ReturnsFourFiles() {
        File oneCameraRoot = new File(root, "OneCamera");
        FilterCapturedImage filterImage = new FilterCapturedImage(null, null, "tiff");
        
        assertTrue(oneCameraRoot.listFiles(filterImage).length == 6);    
    }

    @Test
    public void testFilter_OneCamerCase_ReturnsFourFiles() {
        File oneCameraRoot = new File(root, "OneCamera");
        FilterCapturedImage filterImage = new FilterCapturedImage("C2", null, "tiff");
        
        assertTrue(oneCameraRoot.listFiles(filterImage).length == 3);
    }

    @Test
    public void testFilter_TwoCamerCase_ReturnsFourFiles() {
        File oneCameraRoot = new File(root, "TwoCamera");
        FilterCapturedImage filterImage = new FilterCapturedImage("C1", "Pol0_90", "tiff");
        
        assertTrue(oneCameraRoot.listFiles(filterImage).length == 3);
    }

    @Test
    public void testFilter_FourCamerCase_ReturnsFourFiles() {
        File oneCameraRoot = new File(root, "FourCamera");
        FilterCapturedImage filterImage = new FilterCapturedImage("C1", "Pol0", "tiff");

        assertTrue(oneCameraRoot.listFiles(filterImage).length == 3);
    }

}