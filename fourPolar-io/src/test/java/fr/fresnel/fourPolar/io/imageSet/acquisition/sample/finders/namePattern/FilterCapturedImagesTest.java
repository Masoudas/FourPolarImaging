package fr.fresnel.fourPolar.io.imageSet.acquisition.sample.finders.namePattern;
        
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.io.*;

public class FilterCapturedImagesTest {

    @Test
    public void testFilter_OneCameraCase_ReturnsTwoFiles() {
        System.out.println(1);
        
        File oneCameraRoot = new File(FilterCapturedImagesTest.class.getResource("OneCamera").getPath());
        FilterCapturedImage filterImage = new FilterCapturedImage(null, "C2", "tif");
        
        assertTrue(oneCameraRoot.listFiles(filterImage).length == 2);
    }

    @Test
    public void testFilter_TwoCamerCase_ReturnsFourFiles() {
        File twoCameraRoot = new File(FilterCapturedImagesTest.class.getResource("TwoCamera").getPath());
        FilterCapturedImage filterImage = new FilterCapturedImage("Pol0_90", "C1", "tif");
        
        assertTrue(twoCameraRoot.listFiles(filterImage).length == 3);
    }

    @Test
    public void testFilter_FourCamerCase_ReturnsFourFiles() {
        File fourCameraRoot = new File(FilterCapturedImagesTest.class.getResource("FourCamera").getPath());
        FilterCapturedImage filterImage = new FilterCapturedImage("Pol0", "C1", "tif");

        assertTrue(fourCameraRoot.listFiles(filterImage).length == 3);
    }

}