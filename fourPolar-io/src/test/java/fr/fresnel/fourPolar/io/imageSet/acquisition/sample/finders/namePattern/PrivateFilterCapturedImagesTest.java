package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.*;

import org.junit.jupiter.api.Test;
 
public class PrivateFilterCapturedImagesTest {
    private static File _root = new File(PrivateFilterCapturedImagesTest.class.getResource("FilterCapturedImages").getPath());
 
    @Test
    public void testFilter_OneCameraCase_ReturnsTwoFiles() {    
        File oneCameraRoot = new File(_root, "OneCamera");
        FilterCapturedImage filterImage = new FilterCapturedImage(null, "C2", "tif");
        
        assertTrue(oneCameraRoot.listFiles(filterImage).length == 4);
    }

    @Test
    public void testFilter_TwoCamerCase_ReturnsFourFiles() {
        File twoCameraRoot = new File(_root, "TwoCamera");
        FilterCapturedImage filterImage = new FilterCapturedImage("Pol0_90", "C1", "tif");
        
        assertTrue(twoCameraRoot.listFiles(filterImage).length == 6);
    }

    @Test
    public void testFilter_FourCamerCase_ReturnsFourFiles() {
        File fourCameraRoot = new File(_root, "FourCamera");
        FilterCapturedImage filterImage = new FilterCapturedImage("Pol0", "C1", "tif");

        assertTrue(fourCameraRoot.listFiles(filterImage).length == 6);
    }

}