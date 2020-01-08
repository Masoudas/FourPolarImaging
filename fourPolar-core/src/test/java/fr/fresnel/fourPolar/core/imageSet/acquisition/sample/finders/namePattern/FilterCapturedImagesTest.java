package fr.fresnel.fourPolar.core.imageSet.acquisition.sample.finders.namePattern;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.io.*;

public class FilterCapturedImagesTest {
    File root = new File("fourPolar-core/src/test/java/fr/fresnel/fourPolar/core/imageSet/acquisition/sample/finders/namePattern/TestFiles/");

    @Test
    public void testFilter_OneCameraCase_ReturnsTwoFiles() {
        File oneCameraRoot = new File(root, "OneCamera");
        FilterCapturedImage filterImage = new FilterCapturedImage(null, "C2", "tif");
        
        assertTrue(oneCameraRoot.listFiles(filterImage).length == 2);
    }

    @Test
    public void testFilter_TwoCamerCase_ReturnsFourFiles() {
        File oneCameraRoot = new File(root, "TwoCamera");
        FilterCapturedImage filterImage = new FilterCapturedImage("Pol0_90", "C1", "tif");
        
        assertTrue(oneCameraRoot.listFiles(filterImage).length == 3);
    }

    @Test
    public void testFilter_FourCamerCase_ReturnsFourFiles() {
        File oneCameraRoot = new File(root, "FourCamera");
        FilterCapturedImage filterImage = new FilterCapturedImage("Pol0", "C1", "tif");

        assertTrue(oneCameraRoot.listFiles(filterImage).length == 3);
    }

}